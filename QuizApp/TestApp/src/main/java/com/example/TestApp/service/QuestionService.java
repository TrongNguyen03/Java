package com.example.TestApp.service;

import com.example.TestApp.model.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

    List<Question> getRandomQuestions(int count);

    List<Question> getAllQuestions();

    Optional<Question> getQuestionById(Long id);

    Question createQuestion(Question question);

    Question updateQuestion(Long id, Question newQuestion);

    boolean deleteQuestion(Long id);
}
