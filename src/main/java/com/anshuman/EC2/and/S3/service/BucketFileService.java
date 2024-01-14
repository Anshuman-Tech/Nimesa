package com.anshuman.EC2.and.S3.service;

import com.anshuman.EC2.and.S3.entity.BucketFile;
import com.anshuman.EC2.and.S3.repository.BucketFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BucketFileService {

    @Autowired
    private BucketFileRepository bucketFileRepository;

    public void saveBucketFile(BucketFile bucketFile){
        bucketFileRepository.save(bucketFile);
    }
    public int countByBucketName(String bucketName){
        return bucketFileRepository.countByBucketName(bucketName);
    }

    public List<String> getAllFilesByBucketNameAndPattern(String bucketName,String pattern){
        return bucketFileRepository.findAllByBucketNameAndFileKeyLike(bucketName,pattern);
    }

}
