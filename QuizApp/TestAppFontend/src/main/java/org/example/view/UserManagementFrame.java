package org.example.view;

import org.example.api.UserApi;
import org.example.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn; // Import TableColumn
import java.awt.*;
import java.util.List;

public class UserManagementFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private User currentUser;

    public UserManagementFrame(JFrame previousFrame, User user) {
        this.currentUser = user;

        setTitle("Quản lý tài khoản người dùng");
        setSize(800, 500);
        setMinimumSize(new Dimension(650, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        model = new DefaultTableModel(new Object[]{"ID", "Tên đăng nhập", "Tên đầy đủ", "Mật khẩu", "Vai trò"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableHeader.setBackground(new Color(52, 152, 219));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 40));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableHeader.setDefaultRenderer(headerRenderer);

        DefaultTableCellRenderer centerContentRenderer = new DefaultTableCellRenderer();
        centerContentRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerContentRenderer); // ID column
        table.getColumnModel().getColumn(4).setCellRenderer(centerContentRenderer); // Role column


        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);


        TableColumn idColumn = table.getColumnModel().getColumn(0);
        idColumn.setMinWidth(0);
        idColumn.setMaxWidth(0);
        idColumn.setWidth(0);
        idColumn.setPreferredWidth(0);
        idColumn.setResizable(false);

        table.setGridColor(new Color(236, 240, 241));
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        actionButtonPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("Thêm", new Color(40, 167, 69));
        JButton btnEdit = createStyledButton("Sửa", new Color(0, 123, 255));
        JButton btnDelete = createStyledButton("Xóa", new Color(220, 53, 69));

        actionButtonPanel.add(btnAdd);
        actionButtonPanel.add(btnEdit);
        actionButtonPanel.add(btnDelete);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backButtonPanel.setOpaque(false);
        JButton btnBack = createStyledButton("Quay Lại", new Color(108, 117, 125));
        backButtonPanel.add(btnBack);

        bottomPanel.add(backButtonPanel, BorderLayout.WEST);
        bottomPanel.add(actionButtonPanel, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();

        btnAdd.addActionListener(e -> UserDialog.showAddDialog(this, model));
        btnEdit.addActionListener(e -> UserDialog.showEditDialog(this, model, table));
        btnDelete.addActionListener(e -> UserDialog.deleteSelectedUser(this, model, table));

        btnBack.addActionListener(e -> {
            dispose();
            new AdminQuestionManager(currentUser).setVisible(true);
        });
    }

    public UserManagementFrame() {
        this(null, null);
    }

    private void loadData() {
        List<User> users = UserApi.getAllUsers();
        model.setRowCount(0);

        if (users != null && !users.isEmpty()) {
            for (User u : users) {
                model.addRow(new Object[]{
                        u.getId(), u.getUsername(), u.getFullName(), u.getPassword(), u.getRole()
                });
            }
        } else {
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Không có tài khoản người dùng nào trong hệ thống.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
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
}
