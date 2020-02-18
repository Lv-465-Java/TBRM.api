package com.softserve.rms.repository;

import com.softserve.rms.entities.S3BucketTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//TODO change name to generated tableNameRepository from dynamic db
public interface S3BucketTestRepository extends JpaRepository<S3BucketTest, Long>  {
    //TODO change S3BucketTest to our table entity (with fileName field)

    S3BucketTest save(S3BucketTest s3BucketTest);

    Optional<S3BucketTest> findById(Long id);

}
