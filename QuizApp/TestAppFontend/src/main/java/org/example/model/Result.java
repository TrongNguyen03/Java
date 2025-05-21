package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Long id;
    private User user;
    private int score;
    private int totalQuestions;
    private long examDate;


}
