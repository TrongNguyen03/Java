package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.*;
import java.sql.*;
import java.util.Base64;
import java.util.prefs.Preferences;

public class LoginForm extends JFrame {
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JCheckBox cbRememberMe;
    private JButton btnLogin;
    private DatabaseHelper dbHelper;


    public LoginForm() {
        setTitle("Đăng nhập hệ thống POS Cafe");
        setSize(400, 300);
        setLocationRelativeTo(null); // Căn giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Không cho resize

        dbHelper = new DatabaseHelper();
        initComponents();
        loadLoginInfo();
    }

    private void initComponents() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));


        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));

        contentPane.add(lblTitle, BorderLayout.NORTH);


        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        tfUsername = new JTextField(20);

        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        tfPassword = new JPasswordField(20);

        cbRememberMe = new JCheckBox("Ghi nhớ đăng nhập");
        cbRememberMe.setBackground(Color.WHITE);
        cbRememberMe.setFont(new Font("Arial", Font.PLAIN, 12));

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBackground(new Color(79, 221, 127));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);

        // Thêm thành phần vào form
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUsername, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(tfUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(tfPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(cbRememberMe, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnLogin, gbc);

        contentPane.add(formPanel, BorderLayout.CENTER);

        setContentPane(contentPane);

        // Sự kiện đăng nhập
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void performLogin(){
        String username=tfUsername.getText().trim();
        String password=new String(tfPassword.getPassword()).trim();

        if (username.isEmpty()||password.isEmpty()){
            JOptionPane.showMessageDialog(this,"Vui lòng nhập tài khoản","thông báo",JOptionPane.WARNING_MESSAGE);
        }
        if (dbHelper.checkLogin(username,password)){
            if (cbRememberMe.isSelected()){
                saveLoginInfo(username,password);
            }else {
                deleteLoginInfo(username,password);
            }
            new ThuNganCafe().setVisible(true);
            dispose();
        }else {
            JOptionPane.showMessageDialog(this,"Tên hoặc mật khẩu không chính xác","lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }
    //mã hóa pw
    private String encrypt(String password){
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
    //giải pw
    private String decrypt(String encodedPassword){
        byte[] decodedBytes=Base64.getDecoder().decode(encodedPassword);
        return new String(decodedBytes);
    }

    private void saveLoginInfo(String username,String password){
        Preferences prefs=Preferences.userRoot().node(this.getClass().getName());
        prefs.put("username",username);
        prefs.put("password",encrypt(password));
    }

    private void deleteLoginInfo(String username,String password){
        Preferences prefs=Preferences.userRoot().node(this.getClass().getName());
        prefs.remove("username");
        prefs.remove("password");
    }

    private void loadLoginInfo(){
        Preferences prefs=Preferences.userRoot().node(this.getClass().getName());
        String savedUsername=prefs.get("username","");
        String savedPassword=prefs.get("password","");

        if (!savedUsername.isEmpty()){
            tfUsername.setText(savedUsername);
        }
        if(!savedPassword.isEmpty()){
            tfPassword.setText(decrypt(savedPassword));
            cbRememberMe.setSelected(true);
        }
    }

    public static void main(String[]arg){
        SwingUtilities.invokeLater(()->{
            new LoginForm().setVisible(true);
        });
    }
}
