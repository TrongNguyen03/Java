package com.example.TestApp.service;

import com.example.TestApp.model.Question;
import com.example.TestApp.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepo;

    // Lấy danh sách câu hỏi ngẫu nhiên
    public List<Question> getRandomQuestions(int count) {
        return questionRepo.getRandomQuestions(count);
    }

    // Lấy tất cả câu hỏi
    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    // Lấy 1 câu hỏi theo ID
    public Optional<Question> getQuestionById(Long id) {
        return questionRepo.findById(id);
    }

    // Thêm mới câu hỏi
    public Question createQuestion(Question question) {
        return questionRepo.save(question);
    }

    // Cập nhật câu hỏi
    public Question updateQuestion(Long id, Question newQuestion) {
        return questionRepo.findById(id).map(q -> {
            q.setContent(newQuestion.getContent());
            q.setOptionA(newQuestion.getOptionA());
            q.setOptionB(newQuestion.getOptionB());
            q.setOptionC(newQuestion.getOptionC());
            q.setOptionD(newQuestion.getOptionD());
            q.setCorrectAnswer(newQuestion.getCorrectAnswer());
            return questionRepo.save(q);
        }).orElse(null);
    }


    // Xóa câu hỏi
    public boolean deleteQuestion(Long id) {
        if (questionRepo.existsById(id)) {
            questionRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
