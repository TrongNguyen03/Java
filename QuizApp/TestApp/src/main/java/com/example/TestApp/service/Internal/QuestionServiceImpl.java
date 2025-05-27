package com.example.TestApp.service.Internal;

import com.example.TestApp.model.Question;
import com.example.TestApp.repository.QuestionRepository;
import com.example.TestApp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepo;

    @Override
    public List<Question> getRandomQuestions(int count) {
        return questionRepo.getRandomQuestions(count);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionRepo.findById(id);
    }

    @Override
    public Question createQuestion(Question question) {
        return questionRepo.save(question);
    }

    @Override
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

    @Override
    public boolean deleteQuestion(Long id) {
        if (questionRepo.existsById(id)) {
            questionRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
