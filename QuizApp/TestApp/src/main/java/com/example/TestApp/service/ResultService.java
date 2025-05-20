package com.example.TestApp.service;

import com.example.TestApp.model.Result;
import com.example.TestApp.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepo;

    public Result saveResult(Result result) {
        return resultRepo.save(result);
    }

    public List<Result> getResultsByUser(Long userId) {
        return resultRepo.findByUserId(userId);
    }
}
