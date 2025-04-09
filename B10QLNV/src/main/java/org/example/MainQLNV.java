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
public class MainQLNV {
    static JFrame frame;
    static DefaultTableModel tableModel;

    static class NhanVien {
            private String maNhanVien;
            private String hoTen;
            private int tuoi;
            private String email;
            private double luongCoBan;

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

            @Override
            public String toString() {
                DecimalFormat df = new DecimalFormat("#,###");
                return "Ma NV: " + maNhanVien +
                        ", Ho Ten: " + hoTen +
                        ", Tuoi: " + tuoi +
                        ", Email: " + email +
                        ", Thu Nhap: " + df.format(getThuNhap());
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
                JFrame frame = new JFrame("Quản Lý Nhân Viên");
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

                //Tính năng mới

                // Nút Import/Export nhiều file
                JButton btnImport = new JButton("Import nhiều file");
                btnImport.setBounds(btnX +200, btnY, btnWidth, btnHeight);
                frame.add(btnImport);

                JButton btnExport = new JButton("Export nhiều file");
                btnExport.setBounds(btnX +200,btnY + btnGap, btnWidth, btnHeight);
                frame.add(btnExport);

                // Nút Roll thưởng (chọn ngẫu nhiên nhân viên để tặng thưởng)
                JButton btnRollThuong = new JButton("Roll thưởng");
                btnRollThuong.setBounds(btnX +200, btnY + 2 * btnGap, btnWidth, btnHeight);
                frame.add(btnRollThuong);

                // Nút Gen danh sách nhân viên (sinh dữ liệu mẫu)
                JButton btnGenDS = new JButton("Gen danh sách");
                btnGenDS.setBounds(btnX+200, btnY + 3 * btnGap, btnWidth, btnHeight);
                frame.add(btnGenDS);

                // Nút clear danh sách nhân viên
                JButton btnClear = new JButton("Clear danh sách");
                btnClear.setBounds(btnX+200, btnY + 4 * btnGap, btnWidth, btnHeight);
                frame.add(btnClear);

                // Bảng hiển thị danh sách nhân viên
                tableModel = new DefaultTableModel(
                        new Object[]{"Mã NV", "Họ Tên", "Tuổi", "Email", "Lương (VND)"}, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // không cho chỉnh sửa
                    }
                };

                JTable table = new JTable(tableModel);


                // Cho phép sắp xếp các cột bằng TableRowSorter
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
                table.setRowSorter(sorter);
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setBounds(20, startY + 5 * gapY, 950, 300);
                frame.add(scrollPane);
                loadnv();




                //Xử lý các sự kiện nút chức năng
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

                        // Kiểm tra trùng mã trong DB
                        Connection conn = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/jdbc_test", "Trong", "31122003"
                        );
                        PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM NhanVien WHERE ma_nv = ?");
                        checkStmt.setString(1, maNV);
                        ResultSet rs = checkStmt.executeQuery();
                        rs.next();
                        if (rs.getInt(1) > 0) {
                            JOptionPane.showMessageDialog(frame, "Mã nhân viên đã tồn tại!");
                            conn.close();
                            return;
                        }

                        // Chèn vào DB
                        PreparedStatement stmt = conn.prepareStatement("INSERT INTO NhanVien (ma_nv, ten, tuoi, email, luong) VALUES (?, ?, ?, ?, ?)");
                        stmt.setString(1, maNV);
                        stmt.setString(2, hoTen);
                        stmt.setInt(3, tuoi);
                        stmt.setString(4, email);
                        stmt.setDouble(5, luong);
                        stmt.executeUpdate();

                        conn.close();

                        JOptionPane.showMessageDialog(frame, "Thêm nhân viên thành công!");
                        tfMaNV.setText("");
                        tfHoTen.setText("");
                        tfTuoi.setText("");
                        tfEmail.setText("");
                        tfLuong.setText("");

                        loadnv();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Tuổi và lương phải là số hợp lệ!");
                    }catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Lỗi khi lưu vào cơ sở dữ liệu: " + ex.getMessage());
                    }
                });

                // Xuất danh sách ra file (xuất file DanhSachNhanVien.txt)
                btnXuatDS.addActionListener(e -> {
                    try {
                        Connection conn = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/jdbc_test", "Trong", "31122003"
                        );
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM NhanVien");

                        File file = new File("DanhSachNhanVien.csv");
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                        writer.write("Mã NV,Họ Tên,Tuổi,Email,Lương (VND)");
                        writer.newLine();

                        while (rs.next()) {
                            String ma = rs.getString("ma_nv");
                            String ten = rs.getString("ten");
                            int tuoi = rs.getInt("tuoi");
                            String email = rs.getString("email");
                            double luong = rs.getDouble("luong");

                            writer.write(ma + "," + ten + "," + tuoi + "," + email + "," + luong);
                            writer.newLine();
                        }

                        writer.close();
                        conn.close();
                        JOptionPane.showMessageDialog(frame, "Đã xuất danh sách ra DanhSachNhanVien.csv");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Lỗi khi xuất file: " + ex.getMessage());
                    }
                });


                // Xóa nhân viên theo mã
                btnXoa.addActionListener(e -> {

                    String maNV = JOptionPane.showInputDialog(frame, "Nhập mã nhân viên cần xóa:");
                    if (maNV != null && !maNV.trim().isEmpty()) {
                        try {
                            Connection conn = DriverManager.getConnection(
                                    "jdbc:mysql://localhost:3306/jdbc_test", "Trong", "31122003"
                            );
                            PreparedStatement stmt = conn.prepareStatement("DELETE FROM NhanVien WHERE ma_nv = ?");
                            stmt.setString(1, maNV);
                            int rowsAffected = stmt.executeUpdate();
                            conn.close();
                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(frame, "Đã xóa nhân viên có mã " + maNV);
                                loadnv(); //bảng cập nhật lại
                            } else {
                                JOptionPane.showMessageDialog(frame, "Không tìm thấy nhân viên có mã " + maNV);
                            }
                        }catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Lỗi khi xóa nhân viên: " + ex.getMessage());
                        }
                    }
                });

                // Clear danh sách
                btnClear.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(frame,
                            "Bạn có chắc chắn muốn xóa toàn bộ danh sách nhân viên khỏi cơ sở dữ liệu?",
                            "Xác nhận", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            Connection conn = DriverManager.getConnection(
                                    "jdbc:mysql://localhost:3306/jdbc_test", "Trong", "31122003"
                            );
                            Statement stmt = conn.createStatement();
                            int rowsDeleted = stmt.executeUpdate("DELETE FROM NhanVien");

                            conn.close();

                            if (rowsDeleted > 0) {
                                JOptionPane.showMessageDialog(frame, "Đã xóa toàn bộ danh sách nhân viên!");
                            } else {
                                JOptionPane.showMessageDialog(frame, "Không có dữ liệu để xóa!");
                            }

                            loadnv(); // Cập nhật bảng sau khi xóa

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Lỗi khi xóa dữ liệu: " + ex.getMessage());
                        }
                    }

                });
//
//                // Cập nhật thông tin nhân viên theo mã
//                btnCapNhat.addActionListener(e -> {
//                    String maNV = JOptionPane.showInputDialog(frame, "Nhập mã nhân viên cần cập nhật:");
//                    if (maNV != null) {
//                        for (NhanVien nv : danhSachNhanVien) {
//                            if (nv.maNhanVien.equals(maNV)) {
//                                JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
//
//                                JTextField tfHoTenMoi = new JTextField(nv.hoTen);
//                                JTextField tfTuoiMoi = new JTextField(String.valueOf(nv.tuoi));
//                                JTextField tfEmailMoi = new JTextField(nv.email);
//
//                                NumberFormat numberFormat = NumberFormat.getNumberInstance();
//                                JFormattedTextField tfLuongMoi = new JFormattedTextField(numberFormat);
//                                tfLuongMoi.setValue(nv.luongCoBan);
//
//                                panel.add(new JLabel("Họ Tên mới:"));
//                                panel.add(tfHoTenMoi);
//                                panel.add(new JLabel("Tuổi mới:"));
//                                panel.add(tfTuoiMoi);
//                                panel.add(new JLabel("Email mới:"));
//                                panel.add(tfEmailMoi);
//                                panel.add(new JLabel("Lương mới:"));
//                                panel.add(tfLuongMoi);
//
//                                int result = JOptionPane.showConfirmDialog(frame, panel, "Cập nhật thông tin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//                                if (result == JOptionPane.OK_OPTION) {
//                                    try {
//                                        String hoTenMoi = tfHoTenMoi.getText().trim();
//                                        String tuoiMoiStr = tfTuoiMoi.getText().trim();
//                                        String emailMoi = tfEmailMoi.getText().trim();
//                                        String luongMoiStr = tfLuongMoi.getText().trim().replace(",", "");
//
//                                        if (!hoTenMoi.matches("[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯưẠ-ỹ\\s]+")) {
//                                            JOptionPane.showMessageDialog(frame, "Họ tên chỉ được chứa chữ cái và khoảng trắng!");
//                                            return;
//                                        }
//
//                                        if (!emailMoi.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
//                                            JOptionPane.showMessageDialog(frame, "Email phải đúng định dạng!");
//                                            return;
//                                        }
//
//                                        int tuoiMoi = Integer.parseInt(tuoiMoiStr);
//                                        double luongMoi = Double.parseDouble(luongMoiStr);
//
//                                        nv.hoTen = hoTenMoi;
//                                        nv.tuoi = tuoiMoi;
//                                        nv.email = emailMoi;
//                                        nv.luongCoBan = luongMoi;
//
//                                        tableModel.setRowCount(0);
//                                        for (NhanVien n : danhSachNhanVien) {
//                                            tableModel.addRow(n.toTableRow());
//                                        }
//                                        JOptionPane.showMessageDialog(frame, "Cập nhật thông tin thành công!");
//                                    } catch (NumberFormatException ex) {
//                                        JOptionPane.showMessageDialog(frame, "Tuổi và lương phải là số hợp lệ!");
//                                    }
//                                }
//                                return;
//                            }
//                        }
//                        JOptionPane.showMessageDialog(frame, "Không tìm thấy nhân viên có mã " + maNV);
//                    }
//                });

                // Tìm kiếm nhân viên theo mã
                btnTimKiem.addActionListener(e -> {
                    String maNV = JOptionPane.showInputDialog(frame, "Nhập mã nhân viên cần tìm:");
                    if (maNV != null && !maNV.trim().isEmpty()) {
                        try {
                            Connection conn = DriverManager.getConnection(
                                    "jdbc:mysql://localhost:3306/jdbc_test", "Trong", "31122003"
                            );
                            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM NhanVien WHERE ma_nv = ?");
                            stmt.setString(1, maNV.trim());
                            ResultSet rs = stmt.executeQuery();

                            if (rs.next()) {
                                String ten = rs.getString("ten");
                                int tuoi = rs.getInt("tuoi");
                                String email = rs.getString("email");
                                double luong = rs.getDouble("luong");

                                DecimalFormat df = new DecimalFormat("#,###");
                                String info = "Mã NV: " + maNV +
                                        "\nHọ Tên: " + ten +
                                        "\nTuổi: " + tuoi +
                                        "\nEmail: " + email +
                                        "\nLương: " + df.format(luong);
                                JOptionPane.showMessageDialog(frame, info);
                            } else {
                                JOptionPane.showMessageDialog(frame, "Không tìm thấy nhân viên có mã " + maNV);
                            }

                            conn.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Lỗi truy vấn: " + ex.getMessage());
                        }
                    }
                });


//                // Import nhiều file (CSV: mỗi dòng có định dạng: maNV,hoTen,tuoi,email,luong)
//                btnImport.addActionListener(e -> {
//                    JFileChooser fileChooser = new JFileChooser();
//                    fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
//                    fileChooser.setMultiSelectionEnabled(true);
//
//                    int option = fileChooser.showOpenDialog(frame);
//                    if (option == JFileChooser.APPROVE_OPTION) {
//                        File[] files = fileChooser.getSelectedFiles();
//                        ExecutorService executor = Executors.newFixedThreadPool(4);//chạy 4 luồng
//
//                        for (File file : files) {
//                            if (!file.getName().toLowerCase().endsWith(".csv")) {
//                                JOptionPane.showMessageDialog(frame, "Chỉ chấp nhận file CSV: " + file.getName());
//                                continue;
//                            }
//
//                            executor.submit(() -> {
//                                List<NhanVien> importedList = importFromCSV(file);
//                                synchronized (danhSachNhanVien) {
//                                    danhSachNhanVien.addAll(importedList);
//                                }
//                                SwingUtilities.invokeLater(() -> {
//                                    tableModel.setRowCount(0);
//                                    for (NhanVien nv : danhSachNhanVien) {
//                                        tableModel.addRow(nv.toTableRow());
//                                    }
//                                });
//                            });
//                        }
//                        executor.shutdown();
//                    }
//                });
//
//
//
//                // Export nhiều file (xuất danh sách nhân viên ra các file)
//                btnExport.addActionListener(e -> {
//                    JFileChooser fileChooser = new JFileChooser();
//                    fileChooser.setMultiSelectionEnabled(true);
//
//                    // Chỉ cho phép lưu file dưới dạng CSV
//                    FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files (*.csv)", "csv");
//                    fileChooser.setFileFilter(filter);
//
//                    int option = fileChooser.showSaveDialog(frame);
//                    if (option == JFileChooser.APPROVE_OPTION) {
//                        File[] files = fileChooser.getSelectedFiles();
//                        ExecutorService executor = Executors.newFixedThreadPool(4);//chạy 4 luồng
//
//                        for (File file : files) {
//                            executor.submit(() -> {
//                                File csvFile = file.getName().toLowerCase().endsWith(".csv") ? file : new File(file.getAbsolutePath() + ".csv");
//                                exportToCSV(csvFile, danhSachNhanVien);
//                            });
//                        }
//                        executor.shutdown();
//                    }
//                });
//
//
                // Roll thưởng: chọn ngẫu nhiên số lượng nhân viên
                btnRollThuong.addActionListener(e -> {
                    String soNguoimm = JOptionPane.showInputDialog(frame, "Nhập số người nhận thưởng:");
                    if (soNguoimm == null || soNguoimm.trim().isEmpty()) {
                        return;
                    }

                    try {
                        int soNguoi = Integer.parseInt(soNguoimm.trim());

                        Connection conn = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/jdbc_test", "Trong", "31122003"
                        );
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM NhanVien");

                        List<NhanVien> danhSachNhanVien = new ArrayList<>();

                        while (rs.next()) {
                            String maNV = rs.getString("ma_nv");
                            String ten = rs.getString("ten");
                            int tuoi = rs.getInt("tuoi");
                            String email = rs.getString("email");
                            double luong = rs.getDouble("luong");
                            danhSachNhanVien.add(new NhanVien(maNV, ten, tuoi, email, luong));
                        }

                        if (danhSachNhanVien.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "Không có nhân viên nào trong cơ sở dữ liệu!");
                            return;
                        }

                        if (soNguoi <= 0 || soNguoi > danhSachNhanVien.size()) {
                            JOptionPane.showMessageDialog(frame, "Số người không hợp lệ!");
                            return;
                        }

                        // Rút ngẫu nhiên
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

                        // Hiển thị
                        StringBuilder sb = new StringBuilder("Nhân viên được tặng thưởng:\n");
                        for (NhanVien nv : ketQua) {
                            sb.append("- ").append(nv.toString()).append("\n");
                        }

                        JOptionPane.showMessageDialog(frame, sb.toString());

                        conn.close();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Số người nhận thưởng phải là số hợp lệ!");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Lỗi truy vấn CSDL: " + ex.getMessage());
                    }
                });


                // Gen danh sách nhân viên ngẫu nhiên cho việc import
                btnGenDS.addActionListener(e -> {
                    String soLuongStr = JOptionPane.showInputDialog(frame, "Nhập số lượng nhân viên cần tạo:");
                    try {
                        int soLuong = Integer.parseInt(soLuongStr);
                        if (soLuong>1000 || soLuong<=0){
                            JOptionPane.showMessageDialog(null, "Vượt quá số lượng cho phép hoặc dưới cho phép!");
                        }
                        else {
                            JFileChooser fileChooser = new JFileChooser();

                            // Thiết lập kiểu file mặc định là CSV
                            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

                            int option = fileChooser.showSaveDialog(frame);
                            if (option == JFileChooser.APPROVE_OPTION) {
                                File file = fileChooser.getSelectedFile();

                                // Đảm bảo file có đuôi .csv
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
        //hàm khoi tao ds
        public static void loadnv(){

            try{
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/jdbc_test", "Trong", "31122003"
                );
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM NhanVien");

                // Xóa dữ liệu cũ trong table
                tableModel.setRowCount(0);

                while (rs.next()) {
                    String maNV=rs.getString("ma_nv");
                    String hoTen=rs.getString("ten");
                    int tuoi=rs.getInt("tuoi");
                    String email = rs.getString("email");
                    double luong=rs.getDouble("luong");
                    tableModel.addRow(new Object[]{maNV, hoTen, tuoi, email, String.format("%,.0f", luong)});
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Lỗi khi tải dữ liệu: " + e.getMessage());
            }
        }

        // Hàm import từ file, file định dạng CSV: maNV,hoTen,tuoi,email,luong
        public static List<NhanVien> importFromCSV(File file) {
            List<NhanVien> danhSach = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean firstLine = true;

                while ((line = reader.readLine()) != null) {
                    // Bỏ qua dòng tiêu đề
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }

                    String[] data = line.split(",");

                    if (data.length != 5) {
                        continue; // Bỏ qua dòng không hợp lệ
                    }

                    String maNV = data[0].trim();
                    String hoTen = data[1].trim();
                    int tuoi = Integer.parseInt(data[2].trim());
                    String email = data[3].trim();
                    double luong = Double.parseDouble(data[4].trim());

                    danhSach.add(new NhanVien(maNV, hoTen, tuoi, email, luong));
                }

                JOptionPane.showMessageDialog(null, "Nhập dữ liệu từ " + file.getName() + " thành công!");
            } catch (IOException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi nhập file CSV: " + file.getName());
            }
            return danhSach;
        }



        // Hàm export ra file
        private static void exportToCSV (File file, List<NhanVien> danhSachNhanVien) {

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Ghi tiêu đề
                writer.write("Mã NV,Họ Tên,Tuổi,Email,Lương (VND)");
                writer.newLine();

                // Ghi dữ liệu nhân viên
                for (NhanVien nv : danhSachNhanVien) {
                    writer.write(nv.maNhanVien + "," + nv.hoTen + "," + nv.tuoi + "," + nv.email + "," + nv.luongCoBan);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(null, "Xuất file CSV thành công: " + file.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi ghi file CSV: " + file.getAbsolutePath());
            }
        }


        // Hàm gen danh sách nhân viên ngẫu nhiên và xuất ra file CSV
        public static void generateRandomEmployeeList(File file, int soLuong) {
            String[] hoTenMau = {"Nguyen Van A", "Tran Thi B", "Le Van C", "Pham Thi D", "Ha Kim A", "Ha A Chu","Tran Van Tai"};
            Random random = new Random();
            Set<String> existingIDs = new HashSet<>();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Ghi dòng tiêu đề CSV
                writer.write("MaNV,HoTen,Tuoi,Email,Luong");
                writer.newLine();

                for (int i = 0; i < soLuong; i++) {
                    String maNV= String.format("%03d",i);
                    String hoTen = hoTenMau[random.nextInt(hoTenMau.length)];
                    int tuoi = random.nextInt(50) + 18;
                    String email = "user" + maNV + "@gmail.com";
                    double luong = (random.nextInt(30) + 5) * 1000000; // Lương từ 5-35 triệu
                    String line = maNV + "," + hoTen + "," + tuoi + "," + email + "," + luong;

                    writer.write(line);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(null, "Đã tạo danh sách nhân viên tại file: " + file.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi ghi file CSV!");
            }
        }

    }