package com.anshuman.EC2.and.S3.controller;

import com.anshuman.EC2.and.S3.entity.Job;
import com.anshuman.EC2.and.S3.enums.Status;
import com.anshuman.EC2.and.S3.service.BucketFileService;
import com.anshuman.EC2.and.S3.service.JobService;
import com.anshuman.EC2.and.S3.service.S3BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class S3BucketController {

    @Autowired
    private JobService jobService;

    @Autowired
    private S3BucketService s3BucketService;

    @Autowired
    private BucketFileService bucketFileService;

    //4
    @GetMapping("/getS3BucketObjects")
    public ResponseEntity<String> getS3BucketObjects(@RequestParam() String bucketName){
        String jobId = jobService.saveJob();
        s3BucketService.saveListOfS3ObjectsToDB(jobId,bucketName);
        return ResponseEntity.status(HttpStatus.OK).body(jobId);
    }

    //5
    @GetMapping("/getS3BucketObjectCount")
    public ResponseEntity<Integer> getS3BucketObjectCount(@RequestParam() String bucketName){
        int res = bucketFileService.countByBucketName(bucketName);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //6
    @GetMapping("/getS3BucketObjectLike")
    public ResponseEntity<List<String>> getS3BucketObjectLike(@RequestParam String bucketName, @RequestParam String pattern){
        List<String> fileNames = bucketFileService.getAllFilesByBucketNameAndPattern(bucketName,pattern);
        return ResponseEntity.status(HttpStatus.OK).body(fileNames);
    }
}
