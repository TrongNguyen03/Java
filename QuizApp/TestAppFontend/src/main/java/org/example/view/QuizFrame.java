package org.example.view;

import org.example.api.QuizApi;
import org.example.model.Question;
import org.example.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class QuizFrame extends JFrame {
    private List<Question> questions;
    private Map<Long, String> userAnswers = new HashMap<>();
    private int currentQuestionIndex = 0;
    private JLabel lblQuestion;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton btnNext;

    public QuizFrame(User user) {
        setTitle("Làm bài thi - Xin chào " + user.getFullName());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        questions = QuizApi.getRandomQuestions(5);
        lblQuestion = new JLabel("Câu hỏi sẽ hiện ở đây");
        lblQuestion.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel centerPanel = new JPanel(new GridLayout(4, 1));
        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            centerPanel.add(optionButtons[i]);
            buttonGroup.add(optionButtons[i]);
        }

        btnNext = new JButton("Câu tiếp");
        btnNext.addActionListener(e -> nextQuestion());

        add(lblQuestion, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(btnNext, BorderLayout.SOUTH);

        showQuestion(currentQuestionIndex);
    }

    private void showQuestion(int index) {
        if (index >= questions.size()) {
            finishQuiz();
            return;
        }

        Question q = questions.get(index);
        lblQuestion.setText("Câu " + (index + 1) + ": " + q.getContent());

        List<String> options = q.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options.get(i));
            optionButtons[i].setSelected(false);
        }

        buttonGroup.clearSelection();
    }

    private void nextQuestion() {
        Question current = questions.get(currentQuestionIndex);

        for (JRadioButton btn : optionButtons) {
            if (btn.isSelected()) {
                userAnswers.put(current.getId(), btn.getText());
                break;
            }
        }

        currentQuestionIndex++;
        showQuestion(currentQuestionIndex);
    }

    private void finishQuiz() {
        int correct = 0;
        for (Question q : questions) {
            String userAnswer = userAnswers.get(q.getId());
            if (userAnswer != null && userAnswer.equals(q.getCorrectAnswer())) {
                correct++;
            }
        }
        JOptionPane.showMessageDialog(this, "Hoàn thành! Bạn đúng " + correct + "/" + questions.size());
        dispose();
        new ResultFrame(correct, questions.size()).setVisible(true);
    }
}

