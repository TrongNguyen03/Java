package org.example.model;

import lombok.Data;
import java.util.List;

@Data
public class Question {
    private Long id;
    private String content;
    private List<String> options;
    private String correctAnswer;

    // Getters và setters
}

