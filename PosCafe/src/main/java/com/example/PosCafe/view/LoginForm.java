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
        setTitle("Đăng nhập POS Cafe"); setSize(400,250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
    }
    private void init() {
        JTextField u=new JTextField(20);
        JPasswordField p=new JPasswordField(20);
        JButton b=new JButton("Đăng nhập");
        b.addActionListener(e->{
            NhanVien n=svc.login(u.getText(),new String(p.getPassword()));
            if(n!=null){cafe.setVisible(true);dispose();}
            else JOptionPane.showMessageDialog(this,"Sai thông tin","Lỗi",JOptionPane.ERROR_MESSAGE);
        });
        JPanel m=new JPanel(new GridLayout(3,2,10,10));
        m.setBorder(BorderFactory.createEmptyBorder(20,30,20,30));
        m.add(new JLabel("User:"));m.add(u);
        m.add(new JLabel("Pass:"));m.add(p);
        m.add(new JLabel());m.add(b);
        add(m);
    }

}
