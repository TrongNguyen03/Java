package com.example.PosCafe;


import com.example.PosCafe.view.LoginForm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class MainApp {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MainApp.class);
		app.setHeadless(false);            // BẮT BUỘC phải có
		ConfigurableApplicationContext ctx = app.run(args);
		SwingUtilities.invokeLater(() -> ctx.getBean(LoginForm.class).setVisible(true));
	}
}


