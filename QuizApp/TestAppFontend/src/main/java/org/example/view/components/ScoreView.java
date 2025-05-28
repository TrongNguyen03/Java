package org.example.view.components;

import org.example.api.ResultApi;
import org.example.api.UserApi;
import org.example.model.Result;
import org.example.model.User;
import org.example.view.AdminQuestionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScoreView extends JFrame {
    private JTable resultTable;
    private User currentUser;

    public ScoreView(User user) {
        this.currentUser = user;
        setTitle("Bảng Điểm Kết Quả Thi");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        JLabel titleLabel = new JLabel("KẾT QUẢ THI CỦA NGƯỜI DÙNG");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Tên Người Dùng", "Điểm Số", "Thời Gian Nộp Bài"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);

        JTableHeader tableHeader = resultTable.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableHeader.setBackground(new Color(52, 152, 219));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 40));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableHeader.setDefaultRenderer(headerRenderer);

        resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultTable.setRowHeight(30);
        resultTable.setGridColor(new Color(200, 200, 200));
        resultTable.setSelectionBackground(new Color(174, 214, 241));
        resultTable.setSelectionForeground(Color.BLACK);
        resultTable.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);

        // --- Panel chứa các nút ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        buttonPanel.setBackground(new Color(236, 240, 241));

        JButton backButton = new JButton("Quay Lại");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        backButton.setBackground(new Color(46, 204, 113)); // Màu xanh lá cây tươi
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(150, 45));

        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(true);
        backButton.setOpaque(true);
        backButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));


        JButton clearAllButton = new JButton("Xóa tất cả kết quả");
        clearAllButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        clearAllButton.setBackground(new Color(220, 53, 69)); // Màu đỏ
        clearAllButton.setForeground(Color.WHITE);
        clearAllButton.setFocusPainted(false);
        clearAllButton.setPreferredSize(new Dimension(180, 45)); // Kích thước lớn hơn

        clearAllButton.setBorderPainted(false);
        clearAllButton.setContentAreaFilled(true);
        clearAllButton.setOpaque(true);
        clearAllButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));


        backButton.addActionListener(e -> {
            dispose();
            new AdminQuestionManager(currentUser).setVisible(true);
        });


        clearAllButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa TẤT CẢ kết quả thi?\nHành động này không thể hoàn tác!",
                    "Xác nhận xóa tất cả", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {

                boolean success = ResultApi.truncateResults();
                if (success) {
                    JOptionPane.showMessageDialog(this, "Đã xóa tất cả kết quả thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadResults(tableModel);
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa tất cả kết quả thất bại. Vui lòng kiểm tra kết nối server và log lỗi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(clearAllButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadResults(tableModel);
    }

    private String getUserFullName(Long userId) {
        User user = UserApi.getUserById(userId);
        if (user != null) {
            return user.getFullName();
        } else {
            return "Người dùng #" + userId + " (Không tìm thấy)";
        }
    }

    private void loadResults(DefaultTableModel model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        List<Result> results = ResultApi.getAllResults();
        model.setRowCount(0);


        if (results != null && !results.isEmpty()) {
            for (Result result : results) {

                if (result == null || result.getUserId() == null) {
                    System.err.println("Cảnh báo: Phát hiện đối tượng Result hoặc userId là null.");
                    continue;
                }

                String formattedSubmitTime = "N/A";
                Object submitTimeObject = result.getSubmitTime();

                if (submitTimeObject != null) {
                    try {
                        LocalDateTime submitDateTime = (LocalDateTime) submitTimeObject;
                        formattedSubmitTime = submitDateTime.format(formatter);
                    } catch (ClassCastException e) {
                        System.err.println("Lỗi: submitTime không phải là kiểu LocalDateTime. Kiểu hiện tại: " + submitTimeObject.getClass().getName());
                        formattedSubmitTime = submitTimeObject.toString();
                    }
                }

                String userFullName = getUserFullName(result.getUserId());

                Object[] rowData = {
                        userFullName,
                        result.getScore(),
                        formattedSubmitTime
                };
                model.addRow(rowData);
            }
        } else {

            System.out.println("Không có kết quả nào để hiển thị.");
        }
    }
}