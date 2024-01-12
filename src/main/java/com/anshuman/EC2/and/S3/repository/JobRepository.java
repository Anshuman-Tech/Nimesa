package com.anshuman.EC2.and.S3.repository;

import com.anshuman.EC2.and.S3.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job,String> {

    @Modifying
    @Query(value = "update job set status=?2 where job_id=?1",nativeQuery = true)
    int updateJob(String jobId,String status);
}
