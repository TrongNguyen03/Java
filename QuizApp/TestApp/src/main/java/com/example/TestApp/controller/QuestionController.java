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
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    private List<QuestionDTO> mapToDTOList(List<Question> questions) {
        return questions.stream().map(QuestionMapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/random")
    public ResponseEntity<List<QuestionDTO>> getRandomQuestions(
            @RequestParam(name = "limit", defaultValue = "5") int count) {
        return ResponseEntity.ok(mapToDTOList(questionService.getRandomQuestions(count)));
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        return ResponseEntity.ok(mapToDTOList(questionService.getAllQuestions()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id)
                .map(q -> ResponseEntity.ok(QuestionMapper.toDTO(q)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO dto) {
        return ResponseEntity.ok(QuestionMapper.toDTO(
                questionService.createQuestion(QuestionMapper.toEntity(dto))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO dto) {
        Question updated = questionService.updateQuestion(id, QuestionMapper.toEntity(dto));
        return updated != null
                ? ResponseEntity.ok(QuestionMapper.toDTO(updated))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        return questionService.deleteQuestion(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
