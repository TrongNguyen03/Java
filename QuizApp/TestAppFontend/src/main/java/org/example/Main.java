package org.example;

import org.example.api.QuizApi;
import org.example.model.Question;
import org.example.view.LoginFrame;

import javax.swing.SwingUtilities;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Khởi động giao diện trên luồng sự kiện Swing
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));

    }


}
