package com.anshuman.EC2.and.S3.repository;

import com.anshuman.EC2.and.S3.entity.S3Bucket;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BucketRepository extends JpaRepository<S3Bucket,String> {

    //To handle the N+1 query
    @EntityGraph(attributePaths = {
            "owner"
    })
    List<S3Bucket> findAll();
}
