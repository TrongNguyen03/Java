package com.example.TestApp.service;

import com.example.TestApp.model.Result;

import java.util.List;

public interface ResultService {

    Result saveResult(Result result);

    List<Result> getResultsByUser(Long userId);

    List<Result> getAllResults();

    void deleteAllResults();
}
