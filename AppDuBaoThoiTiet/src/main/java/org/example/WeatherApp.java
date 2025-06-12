package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class WeatherApp {
    public static void main(String[] args) {
        // Áp dụng Nimbus Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }

        JFrame frame = new JFrame("☁️ Weather Forecast");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null);

        // Sử dụng BorderLayout cho frame chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(181, 216, 244));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        frame.setContentPane(mainPanel);

        // Header title
        JLabel titleLabel = new JLabel("Weather App", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(35, 53, 67));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel trung tâm
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Panel nhập thành phố
        JPanel cityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cityPanel.setOpaque(false);
        JLabel cityLabel = new JLabel("Nhập tên thành phố:");
        cityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField cityField = new JTextField(15);
        cityField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JButton searchButton = new JButton("Tra cứu");
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Đảm bảo rằng các thành phần được thêm đúng thứ tự
        cityPanel.add(cityLabel);
        cityPanel.add(cityField);
        cityPanel.add(searchButton);

        centerPanel.add(cityPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Panel hiển thị thông tin thời tiết
        JPanel weatherPanel = new JPanel(new BorderLayout(10, 10));
        weatherPanel.setOpaque(false);

        // Icon thời tiết
        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        weatherPanel.add(iconLabel, BorderLayout.WEST);

        // Kết quả thông báo thời tiết
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(new Color(142, 173, 200));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));
        weatherPanel.add(resultArea, BorderLayout.CENTER);

        centerPanel.add(weatherPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Xử lý sự kiện nút bấm
        searchButton.addActionListener(e -> {
            String city = cityField.getText().trim();
            if (!city.isEmpty()) {
                WeatherInfo info = weatherService.getWeather(city);
                if (info != null) {
                    resultArea.setText(
                            "Mô tả: " + info.description + "\n" +
                                    "Nhiệt độ: " + info.temp + "°C\n" +
                                    "Độ ẩm: " + info.humidity + "%"
                    );
                    try {
                        Image img = ImageIO.read(new URL(info.getIconUrl()));
                        Image scaledImage = img.getScaledInstance(120, 100, Image.SCALE_SMOOTH);
                        iconLabel.setIcon(new ImageIcon(scaledImage));
                    } catch (IOException ex) {
                        iconLabel.setIcon(null);
                    }
                } else {
                    resultArea.setText("Không tìm thấy thành phố hoặc có lỗi xảy ra.");
                    iconLabel.setIcon(null);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Vui lòng nhập tên thành phố!", "Chú ý", JOptionPane.WARNING_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}
