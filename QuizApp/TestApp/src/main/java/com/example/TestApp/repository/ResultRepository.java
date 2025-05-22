package com.example.TestApp.repository;

import com.example.TestApp.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByUserId(Long userId);
}


