package com.appcenter.practice.repository;

import com.appcenter.practice.domain.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepository extends JpaRepository<Bucket,Long> {
}
