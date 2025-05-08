package com.example.PosCafe.view;

import com.example.PosCafe.model.NhanVien;
import com.example.PosCafe.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class LoginForm extends JFrame {
    @Autowired private NhanVienService svc;
    @Autowired private ThuNganCafe cafe;

    public LoginForm() {
        setTitle("Đăng nhập POS Cafe");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        // Font chung
        Font fontLabel = new Font("SansSerif", Font.BOLD, 14);
        Font fontField = new Font("SansSerif", Font.PLAIN, 14);

        // Các thành phần
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(new Color(26, 172, 77));

        JLabel lblUser = new JLabel("Tên đăng nhập:");
        JLabel lblPass = new JLabel("Mật khẩu:");

        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();

        lblUser.setFont(fontLabel);
        lblPass.setFont(fontLabel);
        txtUser.setFont(fontField);
        txtPass.setFont(fontField);

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setBackground(new Color(79, 221, 127));
        btnLogin.setForeground(Color.WHITE);


        btnLogin.addActionListener(e -> {
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());

            NhanVien nv = svc.login(username, password);
            if (nv != null) {
                cafe.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!",
                        "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Bố cục chính
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelForm.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panelForm.add(lblUser, gbc);
        gbc.gridx = 1;
        panelForm.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(lblPass, gbc);
        gbc.gridx = 1;
        panelForm.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        panelForm.add(btnLogin, gbc);

        // Thêm vào JFrame
        add(panelForm);
        setResizable(false);
        setVisible(true);
    }
}
