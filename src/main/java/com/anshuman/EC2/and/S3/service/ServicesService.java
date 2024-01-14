package com.anshuman.EC2.and.S3.service;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.snowball.model.Ec2RequestFailedException;
import com.anshuman.EC2.and.S3.entity.EC2Instance;
import com.anshuman.EC2.and.S3.entity.Owner;
import com.anshuman.EC2.and.S3.entity.S3Bucket;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ServicesService {

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private AmazonEC2 ec2Client;

    @Autowired
    private EC2InstanceService ec2InstanceService;

    @Autowired
    private JobService jobService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private S3BucketService s3BucketService;

    @Async("asyncTaskExecutor")
    @Transactional
    public void saveServiceToDB(String jobId, List<String> services){
        AtomicBoolean flag = new AtomicBoolean(true);

        services.parallelStream().forEach(service->{
            if(service.equalsIgnoreCase("S3")){
                List<Bucket> buckets = s3Client.listBuckets();
                buckets.stream().forEach(bucket -> {
                    try{
                        Owner newOwner = Owner.builder()
                                .ownerId(bucket.getOwner().getId())
                                .ownerName(bucket.getOwner().getDisplayName())
                                .build();
                        Owner savedOwner = ownerService.saveOwner(newOwner);
                        S3Bucket newS3bucket = S3Bucket.builder()
                                .name(bucket.getName())
                                .createdDate(LocalDateTime.ofInstant(bucket.getCreationDate().toInstant(), ZoneId.systemDefault()))
                                .owner(savedOwner)
                                .build();
                        S3Bucket savedBucket = s3BucketService.saveS3Bucket(newS3bucket);
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
            else if(service.equalsIgnoreCase("EC2")){
                String nextToken = null;
                try {
                    do {
                        DescribeInstancesRequest request = new DescribeInstancesRequest();
                        request.setMaxResults(6);
                        request.setNextToken(nextToken);
                        DescribeInstancesResult describeInstancesResult = ec2Client.describeInstances();

                        for (Reservation reservation : describeInstancesResult.getReservations()) {
                            for (Instance instance : reservation.getInstances()) {
                                EC2Instance ec2Instance = EC2Instance.builder()
                                        .instanceId(instance.getInstanceId())
                                        .imageId(instance.getImageId())
                                        .instanceType(instance.getInstanceType())
                                        .instanceState(instance.getState().getName())
                                        .monitoringInfo(instance.getMonitoring().getState())
                                        .build();
                                ec2InstanceService.saveEC2Instance(ec2Instance);
                            }
                        }
                        nextToken = describeInstancesResult.getNextToken();
                    } while (nextToken != null);

                } catch (Ec2RequestFailedException e) {
                    flag.set(false);
                    if(!flag.get()){
                        jobService.updateJob(jobId,"failed");
                        System.exit(1);
                    }
                    System.err.println(e.getCause().getMessage());
                }
            }
        });
        if(flag.get()){
            jobService.updateJob(jobId,"success");
        }
        else{
            jobService.updateJob(jobId,"failed");
        }
    }
}
