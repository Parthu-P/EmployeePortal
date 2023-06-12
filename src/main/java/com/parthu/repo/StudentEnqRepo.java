package com.parthu.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parthu.entity.StudentEnqEntity;

public interface StudentEnqRepo extends JpaRepository<StudentEnqEntity, Integer> {


}
