package com.example.TestApp.controller;

import com.example.TestApp.model.Result;
import com.example.TestApp.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {
    @Autowired
    private ResultService resultService;

    @PostMapping
    public ResponseEntity<Result> submit(@RequestBody Result result) {
        result.setSubmitTime(LocalDateTime.now());
        return ResponseEntity.ok(resultService.saveResult(result));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Result>> getUserResults(@PathVariable Long userId) {
        return ResponseEntity.ok(resultService.getResultsByUser(userId));
    }


    @GetMapping
    public ResponseEntity<List<Result>> getAllResults() {
        return ResponseEntity.ok(resultService.getAllResults());
    }
}

