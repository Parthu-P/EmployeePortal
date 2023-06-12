package com.parthu.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.parthu.entity.EnqStatusEntity;

public interface EnqStatusRepo extends JpaRepository<EnqStatusEntity, Integer> {

}
