package com.anshuman.EC2.and.S3.service;

import com.anshuman.EC2.and.S3.entity.Owner;
import com.anshuman.EC2.and.S3.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    public Owner saveOwner(Owner owner){
       return ownerRepository.save(owner);
    }
}
