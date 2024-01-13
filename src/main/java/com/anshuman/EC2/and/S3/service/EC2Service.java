package com.anshuman.EC2.and.S3.service;

import com.anshuman.EC2.and.S3.entity.EC2Instance;
import com.anshuman.EC2.and.S3.repository.EC2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EC2Service {

    @Autowired
    private EC2Repository ec2Repository;

    public EC2Instance saveEC2Instance(EC2Instance ec2Instance){
        return ec2Repository.save(ec2Instance);
    }

    public Optional<List<String>> getListOfEC2InstanceIds(){
        Optional result = Optional.of(ec2Repository.getAllInstanceIds());
        return result;
    }
}
