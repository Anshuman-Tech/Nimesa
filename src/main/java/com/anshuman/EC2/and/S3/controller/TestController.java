package com.anshuman.EC2.and.S3.controller;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.s3.AmazonS3;
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
        DescribeInstanceStatusResult describeInstanceStatusResult = ec2Client.describeInstanceStatus();
        System.out.println(describeInstanceStatusResult.toString());
    }
}
