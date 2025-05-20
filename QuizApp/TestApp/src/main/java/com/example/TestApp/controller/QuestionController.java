package com.example.TestApp.controller;

import com.example.TestApp.dto.QuestionDTO;
import com.example.TestApp.mapper.QuestionMapper;
import com.example.TestApp.model.Question;
import com.example.TestApp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/random")
    public ResponseEntity<List<QuestionDTO>> getRandomQuestions(
            @RequestParam(name = "limit", required = false, defaultValue = "5") int count) {

        List<Question> questions = questionService.getRandomQuestions(count);
        List<QuestionDTO> dtos = questions.stream()
                .map(QuestionMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
