package com.anshuman.EC2.and.S3.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.anshuman.EC2.and.S3.entity.Job;
import com.anshuman.EC2.and.S3.enums.Status;
import com.anshuman.EC2.and.S3.repository.BucketFileRepository;
import com.anshuman.EC2.and.S3.repository.JobRepository;
import com.anshuman.EC2.and.S3.service.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ServicesController {

    @Autowired
    private BucketFileRepository bucketFileRepository;

    private final JobRepository jobRepository;
    public ServicesController(JobRepository jobRepository){
        this.jobRepository = jobRepository;
    }
    @Autowired
    private HelperService service;

    @Autowired
    private AmazonS3 s3Client;

    //1
    @GetMapping(value = "/discoverServices")
    public ResponseEntity<String> discoverServices(@RequestParam List<String> services){
        String res = service.helper1();
        service. helper(res,services);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //2
    @GetMapping("/getJobResult/{jobId}")
    public ResponseEntity<String> getJobResult(@PathVariable String jobId){
        Optional<Job> job = jobRepository.findById(jobId);
        if(!job.isPresent()){
            throw new RuntimeException("Not present");
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(job.get().getStatus());
        }
    }

    //3
    @GetMapping("/getDiscoveryResult")
    public ResponseEntity<List<? extends Object>> getDiscoveryResult(@RequestParam() String service){
        if(service.equalsIgnoreCase("S3")){
            return ResponseEntity.status(HttpStatus.OK).body(s3Client.listBuckets());
//            return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList());
        }
        //Return list of db results for EC2 instances.
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList());
    }

    //4
    @GetMapping("/getS3BucketObjects")
    public ResponseEntity<String> getS3BucketObjects(@RequestParam() String bucketName){
        //run a job to get the list of files for the given bucketName.
        String jobId = UUID.randomUUID().toString();
        jobRepository.save(new Job(jobId,Status.INPROGRESS.toString()));
        service.saveListOfS3ObjectsToDB(jobId,bucketName);

        return ResponseEntity.status(HttpStatus.OK).body(jobId);
    }

    //5
    @GetMapping("/getS3BucketObjectCount")
    public ResponseEntity<Integer> getS3BucketObjectCount(@RequestParam() String bucketName){
        int res = bucketFileRepository.countByBucketName(bucketName);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //6
    @GetMapping("/getS3BucketObjectLike")
    public ResponseEntity<List<String>> getS3BucketObjectLike(@RequestParam String bucketName,@RequestParam String pattern){
        List<String> fileNames = bucketFileRepository.findAllByBucketNameAndFileKeyLike(bucketName,pattern);
        return ResponseEntity.status(HttpStatus.OK).body(fileNames);
    }
}
