package com.example.TestApp.service.Internal;

import com.example.TestApp.model.Result;
import com.example.TestApp.repository.ResultRepository;
import com.example.TestApp.service.ResultService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepo;

    @Override
    public Result saveResult(Result result) {
        return resultRepo.save(result);
    }

    @Override
    public List<Result> getResultsByUser(Long userId) {
        return resultRepo.findByUserId(userId);
    }

    @Override
    public List<Result> getAllResults() {
        return resultRepo.findAll();
    }

    @Override
    @Transactional
    public void deleteAllResults() {
        resultRepo.deleteAll();
    }
}
