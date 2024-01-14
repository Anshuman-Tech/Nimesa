package com.anshuman.EC2.and.S3.controller;

import com.anshuman.EC2.and.S3.entity.Job;
import com.anshuman.EC2.and.S3.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class JobController {

    @Autowired
    private JobService jobService;

    //2
    @GetMapping("/getJobResult/{jobId}")
    public ResponseEntity<String> getJobResult(@PathVariable String jobId){
        Optional<Job> job = jobService.getJobById(jobId);
        if(!job.isPresent()){
            throw new RuntimeException("Not present");
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(job.get().getStatus());
        }
    }
}
