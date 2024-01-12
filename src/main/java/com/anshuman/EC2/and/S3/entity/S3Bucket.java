package com.anshuman.EC2.and.S3.entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Builder
public class S3Bucket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator",sequenceName = "sequenceGenerator")
    private Long bucketId;
    private String name;
    private LocalDateTime createdDate;

    @ManyToOne
    private Owner owner;

    public S3Bucket() {
    }

    public S3Bucket(String name, LocalDateTime createdDate, Owner owner) {
        this.name = name;
        this.createdDate = createdDate;
        this.owner = owner;
    }

    public S3Bucket(Long bucketId, String name, LocalDateTime createdDate, Owner owner) {
        this.bucketId = bucketId;
        this.name = name;
        this.createdDate = createdDate;
        this.owner = owner;
    }

    public Long getBucketId() {
        return bucketId;
    }

    public void setBucketId(Long bucketId) {
        this.bucketId = bucketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Bucket{" +
                "bucketId='" + bucketId + '\'' +
                ", name='" + name + '\'' +
                ", createdDate=" + createdDate +
                ", owner=" + owner +
                '}';
    }
}
