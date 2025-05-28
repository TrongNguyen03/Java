package org.example.view.components;

import org.example.api.QuizApi;
import org.example.model.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class QuestionDialog extends JDialog {
    private JTextField txtContent, txtA, txtB, txtC, txtD, txtCorrect;
    private boolean isQuestionSaved = false;

    public QuestionDialog(JFrame parent, Question q) {
        super(parent, true);
        setTitle(q == null ? "Thêm câu hỏi mới" : "Sửa câu hỏi");
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(15, 15, 10, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel lblContent = new JLabel("Nội dung:");
        JLabel lblA = new JLabel("Đáp án A:");
        JLabel lblB = new JLabel("Đáp án B:");
        JLabel lblC = new JLabel("Đáp án C:");
        JLabel lblD = new JLabel("Đáp án D:");
        JLabel lblCorrect = new JLabel("Đáp án đúng:");


        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font textFieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        lblContent.setFont(labelFont);
        lblA.setFont(labelFont);
        lblB.setFont(labelFont);
        lblC.setFont(labelFont);
        lblD.setFont(labelFont);
        lblCorrect.setFont(labelFont);

        txtContent = new JTextField(25);
        txtA = new JTextField(25);
        txtB = new JTextField(25);
        txtC = new JTextField(25);
        txtD = new JTextField(25);
        txtCorrect = new JTextField(25);

        txtContent.setFont(textFieldFont);
        txtA.setFont(textFieldFont);
        txtB.setFont(textFieldFont);
        txtC.setFont(textFieldFont);
        txtD.setFont(textFieldFont);
        txtCorrect.setFont(textFieldFont);


        gbc.gridx = 0; // Cột 0
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0; contentPanel.add(lblContent, gbc);
        gbc.gridy = 1; contentPanel.add(lblA, gbc);
        gbc.gridy = 2; contentPanel.add(lblB, gbc);
        gbc.gridy = 3; contentPanel.add(lblC, gbc);
        gbc.gridy = 4; contentPanel.add(lblD, gbc);
        gbc.gridy = 5; contentPanel.add(lblCorrect, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gbc.gridy = 0; contentPanel.add(txtContent, gbc);
        gbc.gridy = 1; contentPanel.add(txtA, gbc);
        gbc.gridy = 2; contentPanel.add(txtB, gbc);
        gbc.gridy = 3; contentPanel.add(txtC, gbc);
        gbc.gridy = 4; contentPanel.add(txtD, gbc);
        gbc.gridy = 5; contentPanel.add(txtCorrect, gbc);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        JButton btnSave = createStyledButton("Lưu", new Color(40, 167, 69));
        JButton btnCancel = createStyledButton("Hủy", new Color(108, 117, 125));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);


        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);


        if (q != null) {
            txtContent.setText(q.getContent());
            List<String> options = q.getOptions();
            if (options != null && options.size() == 4) {
                txtA.setText(options.get(0));
                txtB.setText(options.get(1));
                txtC.setText(options.get(2));
                txtD.setText(options.get(3));
            }
            txtCorrect.setText(q.getCorrectAnswer());
        }


        btnSave.addActionListener(e -> {
            String content = txtContent.getText().trim();
            String a = txtA.getText().trim();
            String b = txtB.getText().trim();
            String c = txtC.getText().trim();
            String d = txtD.getText().trim();
            String correct = txtCorrect.getText().trim();


            if (content.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty() || correct.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin vào tất cả các trường.", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!correct.equals(a) && !correct.equals(b) && !correct.equals(c) && !correct.equals(d)) {
                JOptionPane.showMessageDialog(this, "Đáp án đúng phải trùng khớp với một trong các đáp án A, B, C, hoặc D.", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }


            Question newQ = new Question();
            newQ.setContent(content);
            newQ.setOptions(Arrays.asList(a, b, c, d));
            newQ.setCorrectAnswer(correct);

            boolean success;
            if (q == null) {
                success = QuizApi.createQuestion(newQ);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Thêm câu hỏi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    isQuestionSaved = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm câu hỏi thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                newQ.setId(q.getId());
                success = QuizApi.updateQuestion(q.getId(), newQ);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Cập nhật câu hỏi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    isQuestionSaved = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật câu hỏi thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancel.addActionListener(e -> {
            isQuestionSaved = false;
            dispose();
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    public boolean isQuestionSaved() {
        return isQuestionSaved;
    }
}
