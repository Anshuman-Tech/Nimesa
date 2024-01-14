package com.anshuman.EC2.and.S3.service;

import com.anshuman.EC2.and.S3.entity.EC2Instance;
import com.anshuman.EC2.and.S3.repository.EC2InstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EC2InstanceService {

    @Autowired
    private EC2InstanceRepository ec2InstanceRepository;

    public EC2Instance saveEC2Instance(EC2Instance ec2Instance){
        return ec2InstanceRepository.save(ec2Instance);
    }

    public Optional<List<String>> getListOfEC2InstanceIds(){
        Optional result = Optional.of(ec2InstanceRepository.getAllInstanceIds());
        return result;
    }
}
