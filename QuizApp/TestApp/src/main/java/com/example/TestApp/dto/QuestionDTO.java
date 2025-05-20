package com.example.TestApp.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String content;
    private List<String> options;
    private String correctAnswer;
}

