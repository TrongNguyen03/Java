package com.example.TestApp.mapper;

import com.example.TestApp.dto.QuestionDTO;
import com.example.TestApp.model.Question;

import java.util.Arrays;
import java.util.List;

public class QuestionMapper {

    // Từ entity => DTO
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

    // Từ DTO => entity
    public static Question toEntity(QuestionDTO dto) {
        Question question = new Question();
        question.setId(dto.getId()); // Để cập nhật
        question.setContent(dto.getContent());
        question.setCorrectAnswer(dto.getCorrectAnswer());

        List<String> options = dto.getOptions();
        if (options != null && options.size() >= 4) {
            question.setOptionA(options.get(0));
            question.setOptionB(options.get(1));
            question.setOptionC(options.get(2));
            question.setOptionD(options.get(3));
        }

        return question;
    }
}
