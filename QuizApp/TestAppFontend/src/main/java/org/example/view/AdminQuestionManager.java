package org.example.view;

import org.example.api.QuizApi;
import org.example.model.Question;
import org.example.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.util.List;

public class AdminQuestionManager extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;


    public AdminQuestionManager(User user) {
        setTitle("Quản lý câu hỏi - Admin");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] columnNames = {"ID", "Nội dung", "Đáp án A", "B", "C", "D", "Đúng"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));


        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(60, 141, 188));
        table.getTableHeader().setForeground(Color.WHITE);


        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID column
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Correct Answer column


        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(6).setPreferredWidth(70);

        // Ẩn ID (column index 0)
        TableColumn idColumn = table.getColumnModel().getColumn(0);
        idColumn.setMinWidth(0);
        idColumn.setMaxWidth(0);
        idColumn.setWidth(0);
        idColumn.setPreferredWidth(0);
        idColumn.setResizable(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionButtonPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("Thêm", new Color(40, 167, 69));
        JButton btnEdit = createStyledButton("Sửa", new Color(0, 123, 255));
        JButton btnDelete = createStyledButton("Xóa", new Color(220, 53, 69));
        JButton btnUser = createStyledButton("Quản lý nick", new Color(63, 181, 59));
        JButton btnScore = createStyledButton("Xem điểm", new Color(5, 68, 120));
        JButton btnBack = createStyledButton("Đăng xuất", new Color(108, 117, 125));
        JButton btnImportExcel = createStyledButton("Import Excel", new Color(255, 193, 7));


        actionButtonPanel.add(btnImportExcel);
        actionButtonPanel.add(btnAdd);
        actionButtonPanel.add(btnEdit);
        actionButtonPanel.add(btnDelete);
        actionButtonPanel.add(btnScore);
        actionButtonPanel.add(btnUser);


        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(btnBack);

        bottomPanel.add(backButtonPanel, BorderLayout.WEST);
        bottomPanel.add(actionButtonPanel, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadQuestions();

        btnUser.addActionListener(e -> {
            dispose();
            new UserManagementFrame().setVisible(true);
        });

        btnScore.addActionListener(e -> {

            new ScoreView(user).setVisible(true);
            dispose();
        });

        btnAdd.addActionListener(e -> {
            QuestionDialog dialog = new QuestionDialog(this, null);
            dialog.setVisible(true);
            if (dialog.isQuestionSaved()) {
                loadQuestions();
            }
        });

        btnImportExcel.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    File selectedFile = fileChooser.getSelectedFile();
                    List<Question> questions = ExcelImporter.readQuestionsFromExcel(selectedFile);

                    int successCount = 0;
                    for (Question q : questions) {
                        if (QuizApi.createQuestion(q)) {
                            successCount++;
                        }
                    }

                    JOptionPane.showMessageDialog(this,
                            "Đã import thành công " + successCount + "/" + questions.size() + " câu hỏi.",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                    loadQuestions();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Lỗi khi đọc file Excel: " + ex.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        btnEdit.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {

                Long id = (Long) tableModel.getValueAt(selected, 0);
                Question q = QuizApi.getQuestionById(id);
                if (q != null) {
                    QuestionDialog dialog = new QuestionDialog(this, q);
                    dialog.setVisible(true);
                    if (dialog.isQuestionSaved()) {
                        loadQuestions();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy câu hỏi để sửa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một câu hỏi để sửa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {

                Long id = (Long) tableModel.getValueAt(selected, 0);
                String questionContent = (String) tableModel.getValueAt(selected, 1);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc chắn muốn xóa câu hỏi:\n\"" + questionContent + "\"?",
                        "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (QuizApi.deleteQuestion(id)) {
                        JOptionPane.showMessageDialog(this, "Xóa câu hỏi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        loadQuestions();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa câu hỏi thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một câu hỏi để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> {
            new LoginFrame().setVisible(true);
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

    private void loadQuestions() {
        tableModel.setRowCount(0);
        List<Question> questions = QuizApi.getAllQuestions();
        if (questions != null && !questions.isEmpty()) {
            for (Question q : questions) {
                List<String> opts = q.getOptions();

                Object[] rowData = new Object[]{
                        q.getId(),
                        q.getContent(),
                        opts != null && opts.size() > 0 ? opts.get(0) : "",
                        opts != null && opts.size() > 1 ? opts.get(1) : "",
                        opts != null && opts.size() > 2 ? opts.get(2) : "",
                        opts != null && opts.size() > 3 ? opts.get(3) : "",
                        q.getCorrectAnswer()
                };
                tableModel.addRow(rowData);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không có câu hỏi nào trong hệ thống.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }


}
