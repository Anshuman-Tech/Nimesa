package com.anshuman.EC2.and.S3.controller;

import com.anshuman.EC2.and.S3.service.EC2InstanceService;
import com.anshuman.EC2.and.S3.service.JobService;
import com.anshuman.EC2.and.S3.service.S3BucketService;
import com.anshuman.EC2.and.S3.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ServicesController {

    @Autowired
    private EC2InstanceService ec2InstanceService;

    @Autowired
    private S3BucketService s3BucketService;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private JobService jobService;

    //1
    @GetMapping(value = "/discoverServices")
    public ResponseEntity<String> discoverServices(@RequestParam List<String> services){
        String jobId = jobService.saveJob();
        servicesService.saveServiceToDB(jobId,services);
        return ResponseEntity.status(HttpStatus.OK).body(jobId);
    }

    //3
    @GetMapping("/getDiscoveryResult")
    public ResponseEntity<List<? extends Object>> getDiscoveryResult(@RequestParam() String service) {
        if (service.equalsIgnoreCase("S3")) {
            return ResponseEntity.status(HttpStatus.OK).body(s3BucketService.getAllS3Bucket());
        }
        Optional<List<String>> result = ec2InstanceService.getListOfEC2InstanceIds();
        return ResponseEntity.status(HttpStatus.OK).body(result.get());
    }
}
