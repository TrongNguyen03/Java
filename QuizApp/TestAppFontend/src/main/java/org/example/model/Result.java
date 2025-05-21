package org.example.model;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {
    private Long id;

    private Long userId;

    private int score;

    private int totalQuestions;

    private LocalDateTime submitTime;
}
