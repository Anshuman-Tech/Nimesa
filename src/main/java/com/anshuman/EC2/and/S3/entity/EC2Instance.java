package com.anshuman.EC2.and.S3.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EC2Instance {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator",sequenceName = "sequenceGenerator")
    private Long id;
    private String instanceId;
    private String imageId;
    private String instanceType;
    private String instanceState;
    private String monitoringInfo;
}
