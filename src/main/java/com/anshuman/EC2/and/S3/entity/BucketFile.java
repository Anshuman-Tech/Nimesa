package com.anshuman.EC2.and.S3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"fileKey","bucketName"})
})
public class BucketFile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator",sequenceName = "sequenceGenerator")
    private Long fileId;

    private String fileKey;

    private Long size;

    private LocalDateTime lastModified;

    private String storageClass;

    private String bucketName;

    @ManyToOne
    private Owner owner;

    @ManyToOne
    private S3Bucket bucket;

}
