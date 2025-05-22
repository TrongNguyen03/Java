//package org.example.view;
//
//import org.example.api.ResultApi;
//import org.example.model.Result;
//import org.example.model.User;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.JTableHeader;
//import java.awt.*;
//import java.util.List;
//
//public class ScoreView extends JFrame {
//    private JTable resultTable;
//    private JFrame previousFrame; // Thêm tham chiếu đến frame trước đó
//
//    // Constructor mới để nhận frame trước đó
//    public ScoreView(JFrame previousFrame) {
//        this.previousFrame = previousFrame; // Lưu trữ frame trước đó
//        setTitle("Bảng Điểm Kết Quả Thi");
//        setSize(700, 450);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLayout(new BorderLayout(10, 10));
//
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String[] columnNames = {"Mã Người Dùng", "Điểm Số", "Thời Gian Nộp Bài"};
//        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        resultTable = new JTable(tableModel);
//
//        JTableHeader tableHeader = resultTable.getTableHeader();
//        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        tableHeader.setBackground(new Color(60, 141, 188));
//        tableHeader.setForeground(Color.WHITE);
//        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 30));
//
//        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
//        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
//        tableHeader.setDefaultRenderer(headerRenderer);
//
//        resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        resultTable.setRowHeight(25);
//        resultTable.setGridColor(new Color(146, 186, 198));
//        resultTable.setSelectionBackground(new Color(255, 255, 255));
//
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//        resultTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
//
//        JScrollPane scrollPane = new JScrollPane(resultTable);
//        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        add(scrollPane, BorderLayout.CENTER);
//
//        // --- Thêm nút "Quay lại" ---
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); // Canh giữa, thêm padding trên/dưới
//        buttonPanel.setBackground(new Color(142, 173, 200)); // Màu nền cho panel nút
//
//        JButton backButton = new JButton("Quay Lại");
//        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        backButton.setBackground(new Color(92, 184, 92)); // Màu xanh lá cây
//        backButton.setForeground(Color.WHITE);
//        backButton.setFocusPainted(false); // Bỏ viền focus
//        backButton.setPreferredSize(new Dimension(120, 35)); // Kích thước nút
//
//        // Xử lý sự kiện khi nhấn nút "Quay lại"
//        backButton.addActionListener(e -> {
//            dispose(); // Đóng cửa sổ ScoreView hiện tại
////            new AdminQuestionManager().setVisible(true);
//        });
//        buttonPanel.add(backButton);
//        add(buttonPanel, BorderLayout.SOUTH);
//
//        loadResults(tableModel);
//    }
//
//    private void loadResults(DefaultTableModel model) {
//        List<Result> results = ResultApi.getAllResults();
//        model.setRowCount(0);
//        for (Result result : results) {
//            Object[] rowData = {
//                    result.getUserId(),
//                    result.getScore(),
//                    result.getSubmitTime()
//            };
//            model.addRow(rowData);
//        }
//    }
//
//
//}