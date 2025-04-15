package org.example;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.sql.*;

public class MainQLNVv2 {
    static JFrame frame;
    static DefaultTableModel tableModel;

    static class NhanVien {
        protected String maNhanVien;
        protected String hoTen;
        protected int tuoi;
        protected String email;
        protected double luongCoBan;

        public NhanVien(String maNhanVien, String hoTen, int tuoi, String email, double luongCoBan) {
            this.maNhanVien = maNhanVien;
            this.hoTen = hoTen;
            this.tuoi = tuoi;
            this.email = email;
            this.luongCoBan = luongCoBan;
        }

        public double getThuNhap() {
            return luongCoBan;
        }

        public String getTen() {
            return hoTen;
        }

        public int getTuoi() {
            return tuoi;
        }

        public String getEmail() {
            return email;
        }

        public double getLuong() {
            return luongCoBan;
        }

        @Override
        public String toString() {
            DecimalFormat df = new DecimalFormat("#,###");
            return "Ma NV: " + maNhanVien +
                    ", Ho Ten: " + hoTen +
                    ", Tuổi: " + tuoi +
                    ", Email: " + email +
                    ", Thu Nhập: " + df.format(getThuNhap());
        }

        public String[] toTableRow() {
            DecimalFormat df = new DecimalFormat("#,###");
            return new String[]{
                    maNhanVien, hoTen, String.valueOf(tuoi), email, df.format(luongCoBan)
            };
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Quản Lý Nhân Viên");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 630);
            frame.setLayout(null);

            // Tiêu đề và đồng hồ hiển thị
            JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
            lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
            lblTitle.setForeground(Color.RED);
            lblTitle.setBounds(350, 10, 300, 40);
            frame.add(lblTitle);

            JLabel lblTime = new JLabel();
            lblTime.setFont(new Font("Arial", Font.BOLD, 14));
            lblTime.setForeground(Color.RED);
            lblTime.setBounds(900, 10, 100, 30);
            frame.add(lblTime);
            new Timer(1000, e -> lblTime.setText(new SimpleDateFormat("hh:mm a").format(new Date()))).start();

            // Các trường nhập liệu (Form)
            int startY = 60;
            int labelWidth = 100, fieldWidth = 250, height = 30, gapY = 40;

            JLabel lblMaNV = new JLabel("Mã NV:");
            lblMaNV.setBounds(20, startY, labelWidth, height);
            frame.add(lblMaNV);
            JTextField tfMaNV = new JTextField();
            tfMaNV.setBounds(130, startY, fieldWidth, height);
            frame.add(tfMaNV);

            JLabel lblHoTen = new JLabel("Họ Tên:");
            lblHoTen.setBounds(20, startY + gapY, labelWidth, height);
            frame.add(lblHoTen);
            JTextField tfHoTen = new JTextField();
            tfHoTen.setBounds(130, startY + gapY, fieldWidth, height);
            frame.add(tfHoTen);

            JLabel lblTuoi = new JLabel("Tuổi:");
            lblTuoi.setBounds(20, startY + 2 * gapY, labelWidth, height);
            frame.add(lblTuoi);
            JTextField tfTuoi = new JTextField();
            tfTuoi.setBounds(130, startY + 2 * gapY, fieldWidth, height);
            frame.add(tfTuoi);

            JLabel lblEmail = new JLabel("Email:");
            lblEmail.setBounds(20, startY + 3 * gapY, labelWidth, height);
            frame.add(lblEmail);
            JTextField tfEmail = new JTextField();
            tfEmail.setBounds(130, startY + 3 * gapY, fieldWidth, height);
            frame.add(tfEmail);

            JLabel lblLuong = new JLabel("Lương:");
            lblLuong.setBounds(20, startY + 4 * gapY, labelWidth, height);
            frame.add(lblLuong);
            JTextField tfLuong = new JTextField();
            tfLuong.setBounds(130, startY + 4 * gapY, fieldWidth, height);
            frame.add(tfLuong);

            // Các nút chức năng (bố trí theo cột bên phải)
            int btnX = 400, btnY = 60, btnWidth = 180, btnHeight = 30, btnGap = 40;

            JButton btnThem = new JButton("Thêm nhân viên");
            btnThem.setBounds(btnX, btnY, btnWidth, btnHeight);
            frame.add(btnThem);

            JButton btnXuatDS = new JButton("Xuất danh sách");
            btnXuatDS.setBounds(btnX, btnY + btnGap, btnWidth, btnHeight);
            frame.add(btnXuatDS);

            JButton btnXoa = new JButton("Xóa theo mã");
            btnXoa.setBounds(btnX, btnY + 2 * btnGap, btnWidth, btnHeight);
            frame.add(btnXoa);

            JButton btnCapNhat = new JButton("Cập nhật");
            btnCapNhat.setBounds(btnX, btnY + 3 * btnGap, btnWidth, btnHeight);
            frame.add(btnCapNhat);

            JButton btnTimKiem = new JButton("Tìm theo mã");
            btnTimKiem.setBounds(btnX, btnY + 4 * btnGap, btnWidth, btnHeight);
            frame.add(btnTimKiem);

            JButton btnImport = new JButton("Import nhiều file");
            btnImport.setBounds(btnX + 200, btnY, btnWidth, btnHeight);
            frame.add(btnImport);

            JButton btnExport = new JButton("Export nhiều file");
            btnExport.setBounds(btnX + 200, btnY + btnGap, btnWidth, btnHeight);
            frame.add(btnExport);

            JButton btnRollThuong = new JButton("Roll thưởng");
            btnRollThuong.setBounds(btnX + 200, btnY + 2 * btnGap, btnWidth, btnHeight);
            frame.add(btnRollThuong);

            JButton btnGenDS = new JButton("Gen danh sách");
            btnGenDS.setBounds(btnX + 200, btnY + 3 * btnGap, btnWidth, btnHeight);
            frame.add(btnGenDS);

            JButton btnClear = new JButton("Clear danh sách");
            btnClear.setBounds(btnX + 200, btnY + 4 * btnGap, btnWidth, btnHeight);
            frame.add(btnClear);

            // Bảng hiển thị danh sách nhân viên
            tableModel = new DefaultTableModel(new Object[]{"Mã NV", "Họ Tên", "Tuổi", "Email", "Lương (VND)"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable table = new JTable(tableModel);
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            table.setRowSorter(sorter);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(20, startY + 5 * gapY, 950, 300);
            frame.add(scrollPane);

            // Load dữ liệu
            updateTable();

            //Xử lý sự kiện nút chức năng

            // Thêm nhân viên
            btnThem.addActionListener(e -> {
                String maNV = tfMaNV.getText().trim();
                String hoTen = tfHoTen.getText().trim();
                String tuoiStr = tfTuoi.getText().trim();
                String email = tfEmail.getText().trim();
                String luongStr = tfLuong.getText().trim();
                if (maNV.isEmpty() || hoTen.isEmpty() || tuoiStr.isEmpty() || email.isEmpty() || luongStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Vui lòng nhập đủ thông tin!");
                    return;
                }
                if (!maNV.matches("\\d{3}")) {
                    JOptionPane.showMessageDialog(frame, "Mã nhân viên phải là 3 chữ số!");
                    return;
                }
                if (!hoTen.matches("[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯưẠ-ỹ\\s]+")) {
                    JOptionPane.showMessageDialog(frame, "Họ tên chỉ được chứa chữ cái và khoảng trắng!");
                    return;
                }
                if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                    JOptionPane.showMessageDialog(frame, "Email phải đúng định dạng!");
                    return;
                }
                try {
                    int tuoi = Integer.parseInt(tuoiStr);
                    if (tuoi < 0) {
                        JOptionPane.showMessageDialog(frame, "Tuổi không thể là số âm!");
                        return;
                    }
                    double luong = Double.parseDouble(luongStr);
                    if (DatabaseHelper.employeeExists(maNV)) {
                        JOptionPane.showMessageDialog(frame, "Mã nhân viên đã tồn tại!");
                        return;
                    }
                    DatabaseHelper.insertEmployee(maNV, hoTen, tuoi, email, luong);
                    tfMaNV.setText("");
                    tfHoTen.setText("");
                    tfTuoi.setText("");
                    tfEmail.setText("");
                    tfLuong.setText("");
                    updateTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Tuổi và lương phải là số hợp lệ!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Lỗi khi lưu vào CSDL: " + ex.getMessage());
                }
            });

            // Xuất danh sách ra file CSV
            btnXuatDS.addActionListener(e -> DatabaseHelper.xuatDanhSachRaCSV());

            // Xóa nhân viên theo mã
            btnXoa.addActionListener(e -> {
                String maNV = JOptionPane.showInputDialog(frame, "Nhập mã nhân viên cần xóa:");
                if (maNV != null && !maNV.trim().isEmpty()) {
                    try {
                        DatabaseHelper.deleteEmployee(maNV);
                        JOptionPane.showMessageDialog(frame, "Đã xóa nhân viên có mã " + maNV);
                        updateTable();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Lỗi khi xóa nhân viên: " + ex.getMessage());
                    }
                }
            });

            // Clear danh sách nhân viên khỏi DB
            btnClear.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Bạn có chắc chắn muốn xóa toàn bộ danh sách nhân viên khỏi CSDL?",
                        "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        Statement stmt = DatabaseHelper.getConnection().createStatement();
                        int rowsDeleted = stmt.executeUpdate("DELETE FROM NhanVien");
                        if (rowsDeleted > 0) {
                            JOptionPane.showMessageDialog(frame, "Đã xóa toàn bộ danh sách nhân viên!");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Không có dữ liệu để xóa!");
                        }
                        updateTable();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Lỗi khi xóa dữ liệu: " + ex.getMessage());
                    }
                }
            });

            // Cập nhật thông tin nhân viên theo mã
            btnCapNhat.addActionListener(e -> {
                String maNV = JOptionPane.showInputDialog(frame, "Nhập mã nhân viên cần cập nhật:");
                if (maNV != null && !maNV.trim().isEmpty()) {
                    try {
                        NhanVien nv = DatabaseHelper.findEmployeeById(maNV);
                        if (nv != null) {
                            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
                            JTextField tfhoten = new JTextField(nv.getTen());
                            JTextField tftuoi = new JTextField(String.valueOf(nv.getTuoi()));
                            JTextField tfemail = new JTextField(nv.getEmail());
                            JFormattedTextField tfluong = new JFormattedTextField(NumberFormat.getNumberInstance());
                            tfluong.setValue(nv.getLuong());
                            panel.add(new JLabel("Họ Tên mới:"));
                            panel.add(tfhoten);
                            panel.add(new JLabel("Tuổi mới:"));
                            panel.add(tftuoi);
                            panel.add(new JLabel("Email mới:"));
                            panel.add(tfemail);
                            panel.add(new JLabel("Lương mới:"));
                            panel.add(tfluong);
                            int result = JOptionPane.showConfirmDialog(frame, panel, "Cập nhật thông tin", JOptionPane.OK_CANCEL_OPTION);
                            if (result == JOptionPane.OK_OPTION) {
                                String hoTenMoi = tfhoten.getText().trim();
                                String tuoiMoiStr = tftuoi.getText().trim();
                                String emailMoi = tfemail.getText().trim();
                                int tuoiMoi = Integer.parseInt(tuoiMoiStr);
                                double luongMoi = ((Number) tfluong.getValue()).doubleValue();
                                if (!hoTenMoi.matches("[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯưẠ-ỹ\\s]+")) {
                                    JOptionPane.showMessageDialog(frame, "Họ tên chỉ được chứa chữ cái và khoảng trắng!");
                                    return;
                                }
                                if (!emailMoi.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                                    JOptionPane.showMessageDialog(frame, "Email phải đúng định dạng!");
                                    return;
                                }
                                boolean updated = DatabaseHelper.updateEmployee(maNV, hoTenMoi, tuoiMoi, emailMoi, luongMoi);
                                if (updated) {
                                    JOptionPane.showMessageDialog(frame, "Cập nhật thành công!");
                                    updateTable();
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Cập nhật thất bại!");
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Không tìm thấy nhân viên có mã " + maNV);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Lỗi khi truy cập CSDL!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Tuổi và lương phải là số hợp lệ!");
                    }
                }
            });

            // Tìm kiếm nhân viên theo mã
            btnTimKiem.addActionListener(e -> {
                String maNV = JOptionPane.showInputDialog(frame, "Nhập mã nhân viên cần tìm:");
                if (maNV != null && !maNV.trim().isEmpty()) {
                    try {
                        NhanVien nv = DatabaseHelper.findEmployeeById(maNV.trim());
                        if (nv != null) {
                            DecimalFormat df = new DecimalFormat("#,###");
                            String info = "Mã NV: " + maNV +
                                    "\nHọ Tên: " + nv.getTen() +
                                    "\nTuổi: " + nv.getTuoi() +
                                    "\nEmail: " + nv.getEmail() +
                                    "\nLương: " + df.format(nv.getLuong());
                            JOptionPane.showMessageDialog(frame, info);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Không tìm thấy nhân viên có mã " + maNV);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Lỗi truy vấn: " + ex.getMessage());
                    }
                }
            });

            // Import nhiều file CSV
            btnImport.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                fileChooser.setMultiSelectionEnabled(true);
                int option = fileChooser.showOpenDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File[] files = fileChooser.getSelectedFiles();
                    ExecutorService executor = Executors.newFixedThreadPool(4);
                    for (File file : files) {
                        if (!file.getName().toLowerCase().endsWith(".csv")) {
                            JOptionPane.showMessageDialog(frame, "Chỉ chấp nhận file CSV: " + file.getName());
                            continue;
                        }
                        executor.submit(() -> {
                            DatabaseHelper.importFromCSV(file.getAbsolutePath());
                            List<NhanVien> danhSach = laydanhsachtuDB();
                            SwingUtilities.invokeLater(() -> {
                                tableModel.setRowCount(0);
                                for (NhanVien nv : danhSach) {
                                    tableModel.addRow(nv.toTableRow());
                                }
                            });
                        });
                    }
                    executor.shutdown();
                }
            });

            // Export nhiều file CSV
            btnExport.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files (*.csv)", "csv");
                fileChooser.setFileFilter(filter);
                int option = fileChooser.showSaveDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File[] files = fileChooser.getSelectedFiles();
                    ExecutorService executor = Executors.newFixedThreadPool(4);
                    for (File file : files) {
                        executor.submit(() -> {
                            String filePath = file.getAbsolutePath();
                            if (!filePath.toLowerCase().endsWith(".csv")) {
                                filePath += ".csv";
                            }
                            DatabaseHelper.exportToCSV(filePath);
                        });
                    }
                    executor.shutdown();
                }
            });

            // Roll thưởng: chọn ngẫu nhiên số lượng nhân viên
            btnRollThuong.addActionListener(e -> {
                String soNguoimm = JOptionPane.showInputDialog(frame, "Nhập số người nhận thưởng:");
                if (soNguoimm == null || soNguoimm.trim().isEmpty()) {
                    return;
                }
                try {
                    int soNguoi = Integer.parseInt(soNguoimm.trim());
                    List<NhanVien> danhSachNhanVien = laydanhsachtuDB();
                    if (danhSachNhanVien.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Không có nhân viên nào trong CSDL!");
                        return;
                    }
                    if (soNguoi <= 0 || soNguoi > danhSachNhanVien.size()) {
                        JOptionPane.showMessageDialog(frame, "Số người không hợp lệ!");
                        return;
                    }
                    List<NhanVien> ketQua = new ArrayList<>();
                    Set<Integer> indexDaChon = new HashSet<>();
                    Random random = new Random();
                    while (ketQua.size() < soNguoi) {
                        int index = random.nextInt(danhSachNhanVien.size());
                        if (!indexDaChon.contains(index)) {
                            indexDaChon.add(index);
                            ketQua.add(danhSachNhanVien.get(index));
                        }
                    }
                    StringBuilder sb = new StringBuilder("Nhân viên được tặng thưởng:\n");
                    for (NhanVien nv : ketQua) {
                        sb.append("- ").append(nv.toString()).append("\n");
                    }
                    JOptionPane.showMessageDialog(frame, sb.toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Số người nhận thưởng phải là số hợp lệ!");
                }
            });

            // Gen danh sách nhân viên ngẫu nhiên (export CSV)
            btnGenDS.addActionListener(e -> {
                String soLuongStr = JOptionPane.showInputDialog(frame, "Nhập số lượng nhân viên cần tạo:");
                try {
                    int soLuong = Integer.parseInt(soLuongStr);
                    if (soLuong > 1000 || soLuong <= 0) {
                        JOptionPane.showMessageDialog(null, "Vượt quá số lượng cho phép hoặc dưới cho phép!");
                    } else {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                        int option = fileChooser.showSaveDialog(frame);
                        if (option == JFileChooser.APPROVE_OPTION) {
                            File file = fileChooser.getSelectedFile();
                            if (!file.getName().toLowerCase().endsWith(".csv")) {
                                file = new File(file.getAbsolutePath() + ".csv");
                            }
                            generateRandomEmployeeList(file, soLuong);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Số lượng phải là số hợp lệ!");
                }
            });

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // Hàm cập nhật bảng hiển thị (sử dụng DatabaseHelper)
    public static void updateTable() {
        List<NhanVien> danhSach = laydanhsachtuDB();
        tableModel.setRowCount(0);
        for (NhanVien nv : danhSach) {
            tableModel.addRow(nv.toTableRow());
        }
    }

    // Hàm lấy danh sách nhân viên từ DB
    private static List<NhanVien> laydanhsachtuDB(){
        return DatabaseHelper.loadEmployees();
    }

    // Hàm gen danh sách nhân viên ngẫu nhiên và xuất ra file CSV
    public static void generateRandomEmployeeList(File file, int soLuong) {
        String[] hoTenMau = {"Nguyen Van A", "Tran Thi B", "Le Van C", "Pham Thi D", "Ha Kim A", "Ha A Chu", "Tran Van Tai"};
        Random random = new Random();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Mã NV,Họ Tên,Tuổi,Email,Lương (VND)");
            writer.newLine();
            DecimalFormat df = new DecimalFormat("#.###");
            for (int i = 0; i < soLuong; i++) {
                String maNV = String.format("%03d", i);
                String hoTen = hoTenMau[random.nextInt(hoTenMau.length)];
                int tuoi = random.nextInt(50) + 18;
                String email = "user" + maNV + "@gmail.com";
                double luong = (random.nextInt(30) + 5) * 1000000;
                String line = maNV + "," + hoTen + "," + tuoi + "," + email + "," + df.format(luong);
                writer.write(line);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "Đã tạo danh sách nhân viên tại file: " + file.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi ghi file CSV!");
        }
    }
}
