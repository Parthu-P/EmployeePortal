package com.parthu.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.parthu.entity.CourseEntity;

public interface CourseRepo extends JpaRepository<CourseEntity, Integer> {

}
