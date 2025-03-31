import javax.swing.*;
import java.awt.*;

public class BaiTapB5 {
    static class loginForm {
        private String username;
        private String password;

        public loginForm(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                // Tạo cửa sổ chính
                JFrame frame = new JFrame("Login");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(600, 300);

                // GIAO DIỆN CHÍNH
                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                JLabel formTitle = new JLabel("LOGIN FORM", JLabel.CENTER);
                formTitle.setFont(new Font("Arial", Font.BOLD, 18));
                formTitle.setForeground(Color.BLUE);
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                panel.add(formTitle, gbc);

                // Thêm các thành phần
                gbc.gridwidth = 1;
                gbc.anchor = GridBagConstraints.LINE_END;

                JLabel labelUsername = new JLabel("Username:");
                gbc.gridx = 0;
                gbc.gridy = 1;
                panel.add(labelUsername, gbc);

                JTextField textUsername = new JTextField(30);
                gbc.gridx = 1;
                gbc.anchor = GridBagConstraints.LINE_START;
                panel.add(textUsername, gbc);

                JLabel labelPassword = new JLabel("Password:");
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.anchor = GridBagConstraints.LINE_END;
                panel.add(labelPassword, gbc);

                JPasswordField textPassword = new JPasswordField(30);
                gbc.gridx = 1;
                gbc.anchor = GridBagConstraints.LINE_START;
                panel.add(textPassword, gbc);

                JCheckBox rememberMeCheckBox = new JCheckBox("Remember Me");
                gbc.gridx = 1;
                gbc.gridy = 3;
                gbc.anchor = GridBagConstraints.LINE_START;
                panel.add(rememberMeCheckBox, gbc);

                JButton loginButton = new JButton("Add");
                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.anchor = GridBagConstraints.CENTER;
                panel.add(loginButton, gbc);

                JButton cancelButton = new JButton("Reset");
                gbc.gridx = 1;
                panel.add(cancelButton, gbc);

                // Bắt sự kiện cho nút Add
                loginButton.addActionListener(e -> {
                    String username = textUsername.getText().trim();
                    String password = new String(textPassword.getPassword()).trim();
                    if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Vui lòng nhập tên và mật khẩu!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (username.equals("fpt") && password.equals("polytechnic")) {
                        if (rememberMeCheckBox.isSelected()) {
                            JOptionPane.showMessageDialog(frame, "Đăng nhập thành công! Tài khoản của bạn được ghi nhớ.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Đăng nhập thành công!", "Info", JOptionPane.INFORMATION_MESSAGE);
                            textUsername.setText("");
                            textPassword.setText("");
                        }
                    }
                    else if (username.equals("fpt") && !password.equals("polytechnic")) {
                        if (rememberMeCheckBox.isSelected()) {
                            JOptionPane.showMessageDialog(frame, "Mật khẩu sai!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            JOptionPane.showMessageDialog(frame, "Mật khẩu sai!", "Error", JOptionPane.ERROR_MESSAGE);
                            textPassword.setText("");
                        }
                        }
                    else if (!username.equals("fpt") && password.equals("polytechnic")) {
                        if (rememberMeCheckBox.isSelected()) {
                            JOptionPane.showMessageDialog(frame, "Tên sai!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            JOptionPane.showMessageDialog(frame, "Tên sai!", "Error", JOptionPane.ERROR_MESSAGE);
                            textUsername.setText("");
                        }

                    }
                    else {
                        if (!rememberMeCheckBox.isSelected()) {
                            textUsername.setText("");
                            textPassword.setText("");
                        }
                        JOptionPane.showMessageDialog(frame, "Không đăng nhập thành công.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                cancelButton.addActionListener(e -> {
                    textUsername.setText("");
                    textPassword.setText("");
                    rememberMeCheckBox.setSelected(true);
                });

                // Đặt panel vào frame
                frame.add(panel, BorderLayout.CENTER);

                // Hiển thị cửa sổ
                frame.setVisible(true);
            });
        }
    }

}
