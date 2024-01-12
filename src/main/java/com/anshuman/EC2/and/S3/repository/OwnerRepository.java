package com.anshuman.EC2.and.S3.repository;

import com.anshuman.EC2.and.S3.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner,String> {
}
