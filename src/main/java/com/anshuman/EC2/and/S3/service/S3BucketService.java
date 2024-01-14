package com.anshuman.EC2.and.S3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.anshuman.EC2.and.S3.entity.BucketFile;
import com.anshuman.EC2.and.S3.entity.Owner;
import com.anshuman.EC2.and.S3.entity.S3Bucket;
import com.anshuman.EC2.and.S3.repository.S3BucketRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class S3BucketService {

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private BucketFileService bucketFileService;

    @Autowired
    private JobService jobService;

    @Autowired
    private S3BucketRepository s3BucketRepository;

    @Async("asyncTaskExecutor")
    @Transactional
    public void saveListOfS3ObjectsToDB(String jobId,String bucketName){
        //Intentionally making the thread to sleep for 10 seconds, to test the status update of job.
//        gits 

        ObjectListing objectListing = s3Client.listObjects(bucketName);
        if (objectListing != null) {
            List<S3ObjectSummary> s3ObjectSummariesList = objectListing.getObjectSummaries();
            if (!s3ObjectSummariesList.isEmpty()) {
                for (S3ObjectSummary objectSummary : s3ObjectSummariesList) {
                    Owner newOwner = new Owner(objectSummary.getOwner().getId(),objectSummary.getOwner().getDisplayName());
                    int index = objectSummary.getKey().lastIndexOf("/");
                    BucketFile newFile = BucketFile.builder()
                            .fileKey(objectSummary.getKey().substring(index+1))
                            .size(objectSummary.getSize())
                            .lastModified(LocalDateTime.ofInstant(objectSummary.getLastModified().toInstant(), ZoneId.systemDefault()))
                            .storageClass(objectSummary.getStorageClass())
                            .bucketName(objectSummary.getBucketName())
                            .owner(newOwner)
                            .build();
                    bucketFileService.saveBucketFile(newFile);
                }
                jobService.updateJob(jobId,"success");
            }
            else{
                jobService.updateJob(jobId,"failed");
            }
        }
        else{
            jobService.updateJob(jobId,"failed");
        }
    }

    public S3Bucket saveS3Bucket(S3Bucket s3Bucket){
        return s3BucketRepository.save(s3Bucket);
    }

    public List<S3Bucket> getAllS3Bucket(){
        return s3BucketRepository.findAll();
    }
}
