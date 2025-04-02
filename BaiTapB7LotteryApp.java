import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class BaiTapB7LotteryApp {
    private static final int MAX_NUMBER = 9;
    private static final int DELAY = 90;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Xổ Số Kiến Thiết");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setLayout(new BorderLayout(10, 10));

        // Thêm viền trống cho toàn bộ frame để cách các cạnh 10px
        ((JComponent) frame.getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tạo tiêu đề lớn
        JLabel titleLabel = new JLabel("XỔ SỐ KIẾN THIẾT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.RED);
        frame.add(titleLabel, BorderLayout.NORTH);

        // Panel chứa các ô dữ liệu (Trăm, Chục, Đơn)
        JPanel numberPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JLabel lblHundreds = createNumberLabel("Trăm: 0");
        JLabel lblTens = createNumberLabel("Chục: 0");
        JLabel lblUnits = createNumberLabel("Đơn: 0");
        numberPanel.add(lblHundreds);
        numberPanel.add(lblTens);
        numberPanel.add(lblUnits);
        frame.add(numberPanel, BorderLayout.CENTER);

        // Panel chứa các nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnStartHundreds = createButton("Start Trăm", lblHundreds, "Trăm");
        JButton btnStartTens = createButton("Start Chục", lblTens, "Chục");
        JButton btnStartUnits = createButton("Start Đơn", lblUnits, "Đơn");
        buttonPanel.add(btnStartHundreds);
        buttonPanel.add(btnStartTens);
        buttonPanel.add(btnStartUnits);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JLabel createNumberLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        label.setPreferredSize(new Dimension(100, 50)); // Kích thước vừa đủ cho ô dữ liệu
        return label;
    }

    private static JButton createButton(String text, JLabel label, String type) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> runLottery(label, type)).start();
            }
        });
        return button;
    }

    private static void runLottery(JLabel label, String type) {
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int number = random.nextInt(MAX_NUMBER +1);
            label.setText(type + ": " + number);
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
