import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UDChuyenDoiPNG extends JFrame {
    private final JButton selectFileButton;
    private final JButton chooseOutputButton;
    private final JButton convertButton;
    private final JLabel statusLabel;
    private File selectedFile; // File gốc
    private File outputFile;  // File đầu ra do người dùng chọn

    public UDChuyenDoiPNG() {
        // Tiêu đề ứng dụng
        setTitle("Chuyển đổi ảnh sang PNG");
        setSize(450, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());


        // Tạo các thành phần giao diện
        JLabel formTitle = new JLabel(" CHUYỂN FILE PNG ", JLabel.CENTER);
        formTitle.setFont(new Font("Arial", Font.BOLD, 18));
        formTitle.setForeground(Color.RED);

        JLabel instructionLabel = new JLabel("Chọn file ảnh để chuyển đổi và vị trí lưu:");
        selectFileButton = new JButton("Chọn file ảnh");
        chooseOutputButton = new JButton("Chọn nơi lưu");
        convertButton = new JButton("Chuyển đổi");
        convertButton.setEnabled(false); // Chỉ bật khi file gốc và file lưu được chọn
        statusLabel = new JLabel("Trạng thái: Chưa chọn file hoặc nơi lưu");

        // Thêm các thành phần vào giao diện
        add(formTitle);
        add(instructionLabel);
        add(selectFileButton);
        add(chooseOutputButton);
        add(convertButton);
        add(statusLabel);

        // Sự kiện chọn file gốc
        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn ảnh cần chuyển đổi");
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    statusLabel.setText("Đã chọn file: " + selectedFile.getName());
                    checkReadyToConvert();
                }
            }
        });

        // Sự kiện chọn nơi lưu file chuyển đổi
        chooseOutputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn nơi lưu file PNG");
                fileChooser.setSelectedFile(new File("output.png")); // Tên mặc định cho file PNG
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    outputFile = fileChooser.getSelectedFile();
                    if (!outputFile.getName().endsWith(".png")) {
                        outputFile = new File(outputFile.getAbsolutePath() + ".png");
                    }
                    statusLabel.setText("Đã chọn nơi lưu: " + outputFile.getName());
                    checkReadyToConvert();
                }
            }
        });

        // Sự kiện chuyển đổi file ảnh
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null && outputFile != null) {
                    convertToPNG(selectedFile, outputFile);
                } else {
                    statusLabel.setText("Hãy chọn file và nơi lưu trước!");
                }
            }
        });
    }

    /**
     * Phương thức kiểm tra điều kiện để bật nút chuyển đổi
     */
    private void checkReadyToConvert() {
        if (selectedFile != null && outputFile != null) {
            convertButton.setEnabled(true);
            statusLabel.setText("Sẵn sàng chuyển đổi!");
        }
    }

    /**
     * Phương thức chuyển đổi ảnh sang PNG
     */
    private void convertToPNG(File inputFile, File outputFile) {
        try {
            // Đọc file ảnh gốc
            BufferedImage originalImage = ImageIO.read(inputFile);
            if (originalImage == null) {
                throw new IOException("File không hợp lệ hoặc không phải file ảnh.");
            }

            // Ghi ảnh ra file PNG
            boolean result = ImageIO.write(originalImage, "png", outputFile);

            // Hiển thị trạng thái
            if (result) {
                statusLabel.setText("Chuyển đổi thành công! File PNG được lưu tại: " + outputFile.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Chuyển đổi thành công!\nFile được lưu tại: " + outputFile.getAbsolutePath(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                statusLabel.setText("Chuyển đổi thất bại.");
                JOptionPane.showMessageDialog(this, "Chuyển đổi thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            statusLabel.setText("Đã xảy ra lỗi khi đọc/ghi file.");
            JOptionPane.showMessageDialog(this, "Lỗi xảy ra: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Khởi chạy ứng dụng
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UDChuyenDoiPNG app = new UDChuyenDoiPNG();
            app.setVisible(true);
        });
    }
}

