package com.anshuman.EC2.and.S3.repository;

import com.anshuman.EC2.and.S3.entity.EC2Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EC2InstanceRepository extends JpaRepository<EC2Instance,Long> {

    @Query(value = "select instance_id from ec2instance",nativeQuery = true)
    List<String> getAllInstanceIds();

}
