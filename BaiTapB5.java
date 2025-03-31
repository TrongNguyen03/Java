import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.time.LocalDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaiTapB5 {
    //Bài 1 Loginform
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

                // Giao diện chính
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
                    } else if (username.equals("fpt") && password.equals("polytechnic")) {
                        if (rememberMeCheckBox.isSelected()) {
                            JOptionPane.showMessageDialog(frame, "Đăng nhập thành công! Tài khoản của bạn được ghi nhớ.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Đăng nhập thành công!", "Info", JOptionPane.INFORMATION_MESSAGE);
                            textUsername.setText("");
                            textPassword.setText("");
                        }
                    } else if (username.equals("fpt") && !password.equals("polytechnic")) {
                        if (rememberMeCheckBox.isSelected()) {
                            JOptionPane.showMessageDialog(frame, "Mật khẩu sai!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Mật khẩu sai!", "Error", JOptionPane.ERROR_MESSAGE);
                            textPassword.setText("");
                        }
                    } else if (!username.equals("fpt") && password.equals("polytechnic")) {
                        if (rememberMeCheckBox.isSelected()) {
                            JOptionPane.showMessageDialog(frame, "Tên sai!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Tên sai!", "Error", JOptionPane.ERROR_MESSAGE);
                            textUsername.setText("");
                        }

                    } else {
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
    //Bài 2 Quản lý NV
    static class QuanlyNV {
        private String tenNV;
        private String ngaysinh;
        private double luong;

        public QuanlyNV(String tenNV, String ngaysinh, double luong) {
            this.tenNV = tenNV;
            this.ngaysinh = ngaysinh;
            this.luong = luong;
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                // Tạo cửa sổ chính
                JFrame frame = new JFrame("Quản lý nhân viên");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(600, 400);

                // Giao diện chính
                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                JLabel formTitle = new JLabel(" QUẢN LÝ NHÂN VIÊN", JLabel.CENTER);
                formTitle.setFont(new Font("Arial", Font.BOLD, 18));
                formTitle.setForeground(Color.RED);
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                panel.add(formTitle, gbc);

                // Thêm các thành phần
                gbc.gridwidth = 1;
                gbc.anchor = GridBagConstraints.LINE_END;

                JLabel labelUsername = new JLabel("Họ và tên:");
                gbc.gridx = 0;
                gbc.gridy = 1;
                panel.add(labelUsername, gbc);

                JTextField textUsername = new JTextField(30);
                gbc.gridx = 1;
                gbc.anchor = GridBagConstraints.LINE_START;
                panel.add(textUsername, gbc);

                JLabel labelday = new JLabel("Ngày sinh:");
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.anchor = GridBagConstraints.LINE_END;
                panel.add(labelday, gbc);

                JTextField textday = new JTextField(10); // JTextField để nhập ngày
                gbc.gridx = 1;
                gbc.anchor = GridBagConstraints.LINE_START;
                panel.add(textday, gbc);

                JLabel labelLuong = new JLabel("Lương:");
                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.anchor = GridBagConstraints.LINE_END;
                panel.add(labelLuong, gbc);

                JTextField textLuong = new JTextField(15); // JTextField để nhập lương
                gbc.gridx = 1;
                gbc.anchor = GridBagConstraints.LINE_START;
                panel.add(textLuong, gbc);

                JButton TestButton = new JButton("Kiểm tra");
                gbc.gridx = 0;
                gbc.gridy = 5;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.fill = GridBagConstraints.NONE;
                panel.add(TestButton, gbc);

                TestButton.addActionListener(e -> {
                    try {
                        String username = textUsername.getText().trim();
                        String ngaysinh = textday.getText().trim();
                        String luongInput = textLuong.getText().trim();
                        double luong = luongInput.isEmpty() ? -1 : Double.parseDouble(luongInput);

                        // chuyển màu nếu tên trống
                        if (username.isEmpty()) {
                            textUsername.setBackground(Color.YELLOW);
                        } else {
                            textUsername.setBackground(Color.WHITE);
                        }

                        // chuyển màu nếu ngày sinh sai định dạng hoặc trống
                        if (ngaysinh.isEmpty() || !ngaysinh.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$")) {
                            textday.setBackground(Color.YELLOW);
                            textday.setText("");
                        } else {
                            textday.setBackground(Color.WHITE);
                        }

                        // chuyển màu nếu lương không hợp lệ
                        if (luong <= 0) {
                            textLuong.setBackground(Color.YELLOW);
                            textLuong.setText("");
                        } else {
                            textLuong.setBackground(Color.WHITE);
                        }

                        // ra thông báo
                        if (username.isEmpty() || ngaysinh.isEmpty() || luong <= 0) {
                            JOptionPane.showMessageDialog(frame, "Vui lòng nhập tên, ngày sinh hợp lệ (dd/MM/yyyy), lương và phải là số dương!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else if (!ngaysinh.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$")) {
                            JOptionPane.showMessageDialog(frame, "Ngày sinh không đúng định dạng \nHoặc không phải ngày hợp lệ (dd/MM/yyyy)!", "Error", JOptionPane.ERROR_MESSAGE);
                            textday.setText("");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Dữ liệu hợp lệ!", "Info", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        textLuong.setBackground(Color.YELLOW);
                        JOptionPane.showMessageDialog(frame, "Lương phải là một số hợp lệ!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                // Đặt panel vào frame
                frame.add(panel, BorderLayout.CENTER);

                // Hiển thị cửa sổ
                frame.setVisible(true);
            });
        }
    }

    //Bài tập 3 thư viện XDate
            static class XDate{

                    // Chuyển đổi chuỗi thành Date với định dạng mặc định "dd-MM-yyyy"
                    public static Date parse(String text) throws RuntimeException {
                        return parse(text, "dd-MM-yyyy");
                    }

                    // Chuyển đổi chuỗi thành Date với định dạng tùy chỉnh
                    public static Date parse(String text, String pattern) throws RuntimeException {
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        sdf.setLenient(false); // Kiểm tra nghiêm ngặt định dạng ngày tháng
                        try {
                            return sdf.parse(text);
                        } catch (ParseException e) {
                            throw new RuntimeException("Lỗi: Định dạng ngày tháng không hợp lệ!", e);
                        }
                    }

                    // Test
                    public static void main(String[] args) {
                        try {
                            Date date1 = XDate.parse("30-03-2025");
                            System.out.println("Ngày (default format): " + date1);

                            Date date2 = XDate.parse("03/31/2025", "MM/dd/yyyy");
                            System.out.println("Ngày (custom format): " + date2);
                        } catch (RuntimeException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                }


            }
    
