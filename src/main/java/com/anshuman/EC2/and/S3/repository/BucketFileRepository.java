package com.anshuman.EC2.and.S3.repository;

import com.anshuman.EC2.and.S3.entity.BucketFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BucketFileRepository extends JpaRepository<BucketFile,Long> {

    int countByBucketName(String bucketName);

    @Query(value = "select f.file_key from bucket_file f where f.bucket_name=?1 and f.file_key like %?2%",nativeQuery = true)
    List<String> findAllByBucketNameAndFileKeyLike(String bucketName, String pattern);
}
