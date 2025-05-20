package com.example.TestApp.service;

import com.example.TestApp.model.Question;
import com.example.TestApp.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepo;
    public List<Question> getRandomQuestions(int count) {
        return questionRepo.getRandomQuestions(count);
    }
}
