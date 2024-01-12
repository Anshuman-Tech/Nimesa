package com.anshuman.EC2.and.S3.service;

import com.amazonaws.services.dynamodbv2.xspec.B;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.anshuman.EC2.and.S3.entity.BucketFile;
import com.anshuman.EC2.and.S3.entity.Job;
import com.anshuman.EC2.and.S3.entity.Owner;
import com.anshuman.EC2.and.S3.entity.S3Bucket;
import com.anshuman.EC2.and.S3.enums.Status;
import com.anshuman.EC2.and.S3.repository.BucketFileRepository;
import com.anshuman.EC2.and.S3.repository.BucketRepository;
import com.anshuman.EC2.and.S3.repository.JobRepository;
import com.anshuman.EC2.and.S3.repository.OwnerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class HelperService {
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private BucketFileRepository bucketFileRepository;

    @Autowired
    private BucketRepository bucketRepository;

    @Autowired
    private AmazonS3 s3Client;

    private final JobRepository jobRepository;
    public HelperService(JobRepository jobRepository){
        this.jobRepository =jobRepository;
    }

    public String helper1(){
        String uuid = UUID.randomUUID().toString();
        //Create a job object and save it to the db;
        Job newJob = new Job(uuid, Status.INPROGRESS.toString());
        newJob.setCreatedDate(LocalDateTime.now());
        //Call the save method to store the object to the DB.
        jobRepository.save(newJob);
        return uuid;
    }

    @Async("asyncTaskExecutor")
    @Transactional
    public void helper(String jobId,List<String> services){
        AtomicBoolean flag = new AtomicBoolean(true);

            services.parallelStream().forEach(service->{
//            try {
//                Thread.sleep(20000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
                if(service.equalsIgnoreCase("S3")){
                    //call the method to get the list of S3
                    List<Bucket> buckets = s3Client.listBuckets();
                    buckets.stream().forEach(bucket -> {
                        try{
                            Owner newOwner = Owner.builder()
                                    .ownerId(bucket.getOwner().getId())
                                    .ownerName(bucket.getOwner().getDisplayName())
                                    .build();
                            Owner savedOwner = ownerRepository.save(newOwner);
                            S3Bucket newS3bucket = S3Bucket.builder()
                                    .name(bucket.getName())
                                    .createdDate(LocalDateTime.ofInstant(bucket.getCreationDate().toInstant(),ZoneId.systemDefault()))
                                    .owner(savedOwner)
                                    .build();
                            S3Bucket savedBucket = bucketRepository.save(newS3bucket);
                            if(savedBucket == null){
                                flag.set(false);
                                throw new RuntimeException("Bucket not saved");
                            }
                        }
                        catch(Exception e){
                            flag.set(false);
                            throw new RuntimeException("Exception occurred");
                        }
                    });
                }
                else if(service.equalsIgnoreCase("EC3")){
                    //call the method to get the list of EC2.
                }
            });
        //Update the jobId as success.
        if(flag.get()){
            jobRepository.updateJob(jobId,"success");
        }
        else{
            jobRepository.updateJob(jobId,"failed");
        }
    }


    @Async("asyncTaskExecutor")
    @Transactional
    public void saveListOfS3ObjectsToDB(String jobId,String bucketName){
        try{
            Thread.sleep(10000);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

        ObjectListing objectListing = s3Client.listObjects(bucketName);
        if (objectListing != null) {
            List<S3ObjectSummary> s3ObjectSummariesList = objectListing.getObjectSummaries();
            if (!s3ObjectSummariesList.isEmpty()) {
                for (S3ObjectSummary objectSummary : s3ObjectSummariesList) {
                    Owner newOwner = new Owner(objectSummary.getOwner().getId(),objectSummary.getOwner().getDisplayName());
                    int index = objectSummary.getKey().lastIndexOf("/");
                    BucketFile newFile = BucketFile.builder()
                            .fileKey(objectSummary.getKey().substring(index+1))
//                            .fileKey(objectSummary.getKey())
                            .size(objectSummary.getSize())
                            .lastModified(LocalDateTime.ofInstant(objectSummary.getLastModified().toInstant(),ZoneId.systemDefault()))
                            .storageClass(objectSummary.getStorageClass())
                            .bucketName(objectSummary.getBucketName())
                            .owner(newOwner)
                            .build();
                    bucketFileRepository.save(newFile);
                }
                jobRepository.updateJob(jobId,"success");
            }
            else{
                jobRepository.updateJob(jobId,"failed");
            }
        }
        else{
            jobRepository.updateJob(jobId,"failed");
        }
    }
}
