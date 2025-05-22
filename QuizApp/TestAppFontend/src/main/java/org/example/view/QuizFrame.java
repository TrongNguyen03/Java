package org.example.view;

import org.example.api.QuizApi;
import org.example.model.Question;
import org.example.model.User;
import org.example.api.ResultApi;
import org.example.model.Result;




import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class QuizFrame extends JFrame {
    private List<Question> questions;
    private Map<Long, String> userAnswers = new HashMap<>();
    private Map<Long, List<String>> shuffledOptionsMap = new HashMap<>();
    private int currentQuestionIndex = 0;
    private User currentUser; // Lưu thông tin người dùng

    private JLabel lblQuestionContent;
    private JLabel lblQuestionCounter;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton btnNext;
    private JButton btnPrevious;
    private JButton btnFinish;
    private JButton btnOutPanel;
    private JProgressBar progressBar; // Thanh tiến độ

    public QuizFrame(User user) {
        this.currentUser = user; // Gán người dùng hiện tại
        setTitle("Làm bài thi - Xin chào " + user.getFullName());
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        questions = QuizApi.getRandomQuestions(10);
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có câu hỏi nào để hiển thị.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            dispose();
            return;
        }

        //Panel chính cho toàn bộ nội dung
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));


        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblWelcome = new JLabel("Chào mừng, " + currentUser.getFullName() + "!");
        lblWelcome.setFont(new Font("Arial", Font.ITALIC, 14));
        lblWelcome.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(lblWelcome, BorderLayout.EAST);

        btnOutPanel = new JButton("Đăng xuất");
        btnOutPanel.setFont(new Font("Arial", Font.BOLD, 12));
        btnOutPanel.setBackground(new Color(255, 165, 0));
        btnOutPanel.setForeground(Color.WHITE);
        btnOutPanel.setFocusPainted(false);
        btnOutPanel.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn đăng xuất không?",
                    "Xác nhận đăng xuất",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                dispose();
            }
            });
        JPanel adminBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        adminBtnPanel.add(btnOutPanel);
        headerPanel.add(adminBtnPanel, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);


        JPanel quizContentPanel = new JPanel(new BorderLayout(0, 15));
        quizContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));


        progressBar = new JProgressBar(0, questions.size());
        progressBar.setValue(0);
        progressBar.setStringPainted(true); // Hiển thị phần trăm/giá trị
        progressBar.setFont(new Font("Arial", Font.PLAIN, 12));
        progressBar.setForeground(new Color(0, 153, 51));
        progressBar.setBackground(new Color(230, 230, 230));
        quizContentPanel.add(progressBar, BorderLayout.NORTH);


        JPanel questionHeaderPanel = new JPanel(new BorderLayout());
        lblQuestionCounter = new JLabel("Câu 1 / " + questions.size());
        lblQuestionCounter.setFont(new Font("Arial", Font.BOLD, 14));
        lblQuestionCounter.setForeground(new Color(100, 100, 100));
        questionHeaderPanel.add(lblQuestionCounter, BorderLayout.EAST);

        lblQuestionContent = new JLabel("Câu hỏi sẽ hiện ở đây");
        lblQuestionContent.setFont(new Font("Arial", Font.BOLD, 18));
        lblQuestionContent.setForeground(new Color(50, 50, 50));
        lblQuestionContent.setVerticalAlignment(SwingConstants.TOP);
        lblQuestionContent.setPreferredSize(new Dimension(500, 70));
        lblQuestionContent.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        questionHeaderPanel.add(lblQuestionContent, BorderLayout.CENTER);
        quizContentPanel.add(questionHeaderPanel, BorderLayout.CENTER);


        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 15));
            optionButtons[i].setBackground(Color.WHITE);
            optionsPanel.add(optionButtons[i]);
            buttonGroup.add(optionButtons[i]);
        }
        quizContentPanel.add(optionsPanel, BorderLayout.SOUTH);
        mainPanel.add(quizContentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        btnFinish = new JButton("Kết thúc");
        btnFinish.setFont(new Font("Arial", Font.BOLD, 16));
        btnFinish.setBackground(new Color(204, 0, 0));
        btnFinish.setForeground(Color.WHITE);
        btnFinish.setFocusPainted(false);
        btnFinish.setPreferredSize(new Dimension(120, 40));
        btnFinish.addActionListener(e -> finishQuizConfirmation());

        btnNext = new JButton("Câu tiếp >");
        btnNext.setFont(new Font("Arial", Font.BOLD, 16));
        btnNext.setBackground(new Color(0, 128, 0));
        btnNext.setForeground(Color.WHITE);
        btnNext.setFocusPainted(false);
        btnNext.setPreferredSize(new Dimension(120, 40));
        btnNext.addActionListener(e -> nextQuestion());

        btnPrevious = new JButton("< Quay lại");
        btnPrevious.setFont(new Font("Arial", Font.BOLD, 16));
        btnPrevious.setBackground(new Color(0, 102, 204));
        btnPrevious.setForeground(Color.WHITE);
        btnPrevious.setFocusPainted(false);
        btnPrevious.setPreferredSize(new Dimension(120, 40));
        btnPrevious.addActionListener(e -> previousQuestion());

        buttonPanel.add(btnNext);
        buttonPanel.add(btnPrevious);
        buttonPanel.add(btnFinish);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        showQuestion(currentQuestionIndex);
    }

    private void showQuestion(int index) {
        progressBar.setValue(index);
        progressBar.setString("Câu " + (index + 1) + " / " + questions.size());

        if (index >= questions.size()) {
            finishQuiz();
            return;
        }

        Question q = questions.get(index);
        lblQuestionContent.setText("<html>" + q.getContent() + "</html>");
        lblQuestionCounter.setText("Câu " + (index + 1) + " / " + questions.size());

        List<String> options;

        // Nếu câu hỏi này chưa shuffle, thì shuffle và lưu lại
        if (!shuffledOptionsMap.containsKey(q.getId())) {
            options = new ArrayList<>(q.getOptions());
            Collections.shuffle(options);
            shuffledOptionsMap.put(q.getId(), options);
        } else {
            options = shuffledOptionsMap.get(q.getId());
        }

        // Hiển thị các phương án
        String previousAnswer = userAnswers.get(q.getId());
        buttonGroup.clearSelection();

        for (int i = 0; i < optionButtons.length; i++) {
            if (i < options.size()) {
                String optionText = options.get(i);
                optionButtons[i].setText(optionText);
                optionButtons[i].setVisible(true);

                if (optionText.equals(previousAnswer)) {
                    optionButtons[i].setSelected(true);
                }
            } else {
                optionButtons[i].setVisible(false);
            }
        }

        // Cập nhật nút
        btnNext.setText(index == questions.size() - 1 ? "Hoàn thành" : "Câu tiếp >");
    }


    private void nextQuestion() {
        saveCurrentAnswer(); // Lưu nếu có chọn

        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            showQuestion(currentQuestionIndex);
        } else {
            finishQuiz();
        }
    }



    private void previousQuestion() {
        if (currentQuestionIndex > 0) {
            saveCurrentAnswer(); // lưu lại nếu người dùng có chọn
            currentQuestionIndex--;
            showQuestion(currentQuestionIndex);
        }
    }

    private void saveCurrentAnswer() {
        Question current = questions.get(currentQuestionIndex);
        for (JRadioButton btn : optionButtons) {
            if (btn.isSelected()) {
                userAnswers.put(current.getId(), btn.getText());
                break;
            }
        }
    }


    private void finishQuizConfirmation() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn kết thúc bài thi không?",
                "Xác nhận kết thúc",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        if (currentQuestionIndex < questions.size()) {
            Question lastQuestion = questions.get(currentQuestionIndex);
            for (JRadioButton btn : optionButtons) {
                if (btn.isSelected()) {
                    userAnswers.put(lastQuestion.getId(), btn.getText());
                    break;
                }
            }
        }

        // Tính câu đúng
        int correct = 0;
        for (Question q : questions) {
            String userAnswer = userAnswers.get(q.getId());
            if (userAnswer != null && userAnswer.equals(q.getCorrectAnswer())) {
                correct++;
            }
        }

        // Gửi kết quả lên server
        Result result = new Result();
        result.setUserId(currentUser.getId());
        result.setScore(correct);
        result.setSubmitTime(java.time.LocalDateTime.now());

        boolean submitted = ResultApi.submitResult(result);

        if (submitted) {
            JOptionPane.showMessageDialog(this,
                    "Bạn đã hoàn thành bài thi!\nSố câu đúng: " + correct + "/" + questions.size(),
                    "Kết quả bài thi",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Bạn đã hoàn thành bài thi, nhưng gửi kết quả thất bại!",
                    "Thông báo",
                    JOptionPane.ERROR_MESSAGE);
        }

        dispose();
        new  LoginFrame().setVisible(true);
//      new ScoreView().setVisible(true);

    }

}