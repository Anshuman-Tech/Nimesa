package com.anshuman.EC2.and.S3.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;

@Entity
public class Job {
    @Id
    private String jobId;
    private String status;
    private LocalDateTime createdDate;

    public Job(){

    }
    public Job(String jobId,String status){
        this.jobId = jobId;
        this.status = status;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
