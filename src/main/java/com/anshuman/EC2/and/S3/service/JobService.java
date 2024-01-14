package com.anshuman.EC2.and.S3.service;

import com.anshuman.EC2.and.S3.entity.Job;
import com.anshuman.EC2.and.S3.enums.Status;
import com.anshuman.EC2.and.S3.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public String saveJob(){
        String jobId = UUID.randomUUID().toString();
        Job newJob = new Job(jobId, Status.INPROGRESS.toString());
        newJob.setCreatedDate(LocalDateTime.now());
        jobRepository.save(newJob);
        return jobId;
    }

    public void updateJob(String jobId,String status){
        jobRepository.updateJob(jobId,status);
    }

    public Optional<Job> getJobById(String jobId){
        return jobRepository.findById(jobId);
    }
}
