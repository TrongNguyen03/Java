package org.example.view;

import org.example.api.UserApi;
import org.example.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserDialog {

    public static void showAddDialog(JFrame parent, DefaultTableModel model) {

        JTextField usernameField = new JTextField(20);
        JTextField fullNameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);


        String[] roles = {"USER", "ADMIN"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setSelectedItem("USER");


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Nick (username):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(usernameField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Tên (full name):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(fullNameField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(passwordField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        panel.add(new JLabel("Vai trò:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(roleComboBox, gbc);


        int option = JOptionPane.showConfirmDialog(parent, panel, "Thêm tài khoản người dùng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String fullName = fullNameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = (String) roleComboBox.getSelectedItem();


            if (!username.isEmpty() && !password.isEmpty() && role != null && !role.isEmpty()) {
                User newUser = new User(null, username, fullName, password, role);
                User addedUser = UserApi.addUser(newUser);
                if (addedUser != null) {
                    model.addRow(new Object[]{
                            addedUser.getId(),
                            addedUser.getUsername(),
                            addedUser.getFullName(),
                            addedUser.getPassword(),
                            addedUser.getRole()
                    });
                    JOptionPane.showMessageDialog(parent, "Thêm tài khoản thành công!");
                } else {
                    JOptionPane.showMessageDialog(parent, "Thêm tài khoản thất bại. Tên đăng nhập có thể đã tồn tại.");
                }
            } else {
                JOptionPane.showMessageDialog(parent, "Vui lòng nhập đủ thông tin.");
            }
        }
    }

    public static void showEditDialog(JFrame parent, DefaultTableModel model, JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(parent, "Hãy chọn một tài khoản để sửa.");
            return;
        }


        Long id = (Long) model.getValueAt(row, 0);
        String currentUsername = (String) model.getValueAt(row, 1);
        String currentFullName = (String) model.getValueAt(row, 2);
        String currentPassword = (String) model.getValueAt(row, 3);
        String currentRole = (String) model.getValueAt(row, 4);


        JTextField usernameField = new JTextField(currentUsername, 20);
        JTextField fullNameField = new JTextField(currentFullName, 20);
        JPasswordField passwordField = new JPasswordField(currentPassword, 20);


        String[] roles = {"USER", "ADMIN"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setSelectedItem(currentRole);


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Nick (username):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(usernameField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Tên (full name):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(fullNameField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(passwordField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        panel.add(new JLabel("Vai trò:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(roleComboBox, gbc);


        int option = JOptionPane.showConfirmDialog(parent, panel, "Sửa tài khoản người dùng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String fullName = fullNameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = (String) roleComboBox.getSelectedItem(); // Get selected item from JComboBox


            if (!username.isEmpty() && !password.isEmpty() && role != null && !role.isEmpty()) {
                User updatedUser = new User(id, username, fullName, password, role);
                boolean success = UserApi.updateUser(updatedUser);
                if (success) {

                    model.setValueAt(username, row, 1);
                    model.setValueAt(fullName, row, 2);
                    model.setValueAt(password, row, 3);
                    model.setValueAt(role, row, 4);
                    JOptionPane.showMessageDialog(parent, "Cập nhật tài khoản thành công!");
                } else {
                    JOptionPane.showMessageDialog(parent, "Cập nhật tài khoản thất bại. Tên đăng nhập có thể đã tồn tại.");
                }
            } else {
                JOptionPane.showMessageDialog(parent, "Vui lòng nhập đủ thông tin.");
            }
        }
    }

    public static void deleteSelectedUser(JFrame parent, DefaultTableModel model, JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(parent, "Hãy chọn một tài khoản để xóa.");
            return;
        }


        Long id = (Long) model.getValueAt(row, 0);
        String usernameToDelete = (String) model.getValueAt(row, 1);

        int option = JOptionPane.showConfirmDialog(parent,
                "Bạn có chắc muốn xóa tài khoản '" + usernameToDelete + "'?",
                "Xác nhận xóa tài khoản", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            boolean deleted = UserApi.deleteUser(id);
            if (deleted) {
                model.removeRow(row);
                JOptionPane.showMessageDialog(parent, "Xóa tài khoản thành công!");
            } else {
                JOptionPane.showMessageDialog(parent, "Xóa tài khoản thất bại.");
            }
        }
    }
}
