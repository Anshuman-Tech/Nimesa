package com.anshuman.EC2.and.S3.controller;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.snowball.model.Ec2RequestFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private AmazonEC2 ec2Client;

//    @GetMapping("/test")
//    public ResponseEntity<List<S3Bucket>> testMethod(){
//        List<Bucket> buckets = s3Client.listBuckets();
////        buckets.stream().forEach(bucket -> {
////            bucket.
//////        });
////        return ResponseEntity.status(HttpStatus.OK).body(buckets);
//    }

    @GetMapping("/test1")
    public void testMethod1(){
        String nextToken = null;
        try {
            do {
                DescribeInstancesRequest request = new DescribeInstancesRequest();
                request.setMaxResults(6);
                request.setNextToken(nextToken);
                DescribeInstancesResult describeInstancesResult = ec2Client.describeInstances();

                for (Reservation reservation : describeInstancesResult.getReservations()) {
                    for (Instance instance : reservation.getInstances()) {
                        System.out.println("Instance Id is " + instance.getInstanceId());
                        System.out.println("Image id is "+  instance.getInstanceId());
                        System.out.println("Instance type is "+  instance.getInstanceType());
                        System.out.println("Instance state name is "+  instance.getState().getName());
                        System.out.println("monitoring information is "+  instance.getMonitoring().getState());

                    }
                }
                nextToken = describeInstancesResult.getNextToken();
            } while (nextToken != null);

        } catch (Ec2RequestFailedException e) {
            System.err.println(e.getCause().getMessage());
            System.exit(1);
        }
    }
}
