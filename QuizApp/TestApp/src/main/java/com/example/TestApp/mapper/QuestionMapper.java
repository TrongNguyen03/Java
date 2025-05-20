package com.example.TestApp.mapper;

import com.example.TestApp.dto.QuestionDTO;
import com.example.TestApp.model.Question;

import java.util.Arrays;
import java.util.List;

public class QuestionMapper {
    public static QuestionDTO toDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setContent(question.getContent());
        dto.setCorrectAnswer(question.getCorrectAnswer());
        dto.setOptions(Arrays.asList(
                question.getOptionA(),
                question.getOptionB(),
                question.getOptionC(),
                question.getOptionD()
        ));
        return dto;
    }
}
