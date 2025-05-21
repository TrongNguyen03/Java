package com.example.TestApp.controller;

import com.example.TestApp.dto.QuestionDTO;
import com.example.TestApp.mapper.QuestionMapper;
import com.example.TestApp.model.Question;
import com.example.TestApp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*") // Cho phép frontend gọi API nếu cần
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // Lấy danh sách câu hỏi ngẫu nhiên
    @GetMapping("/random")
    public ResponseEntity<List<QuestionDTO>> getRandomQuestions(
            @RequestParam(name = "limit", required = false, defaultValue = "5") int count) {
        List<Question> questions = questionService.getRandomQuestions(count);
        List<QuestionDTO> dtos = questions.stream()
                .map(QuestionMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Lấy tất cả câu hỏi
    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        List<QuestionDTO> dtos = questions.stream()
                .map(QuestionMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Lấy 1 câu hỏi theo ID
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        Optional<Question> question = questionService.getQuestionById(id);
        return question.map(q -> ResponseEntity.ok(QuestionMapper.toDTO(q)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Thêm mới câu hỏi
    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO dto) {
        Question question = QuestionMapper.toEntity(dto);
        Question saved = questionService.createQuestion(question);
        return ResponseEntity.ok(QuestionMapper.toDTO(saved));
    }

    // Cập nhật câu hỏi
    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO dto) {
        Question updated = questionService.updateQuestion(id, QuestionMapper.toEntity(dto));
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(QuestionMapper.toDTO(updated));
    }

    // Xoá câu hỏi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        boolean deleted = questionService.deleteQuestion(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
