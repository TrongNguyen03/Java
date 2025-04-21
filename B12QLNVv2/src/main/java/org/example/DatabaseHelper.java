package org.example;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    public static Connection getConnection() throws SQLException {
        return ConnectionPool.getDataSource().getConnection();
    }

    // Load danh sách nhân viên từ DB
    public static List<MainQLNVv2.NhanVien> loadEmployees() {
        List<MainQLNVv2.NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                String maNV = rs.getString("ma_nv");
                String ten = rs.getString("ten");
                int tuoi = rs.getInt("tuoi");
                String email = rs.getString("email");
                double luong = rs.getDouble("luong");
                list.add(new MainQLNVv2.NhanVien(maNV, ten, tuoi, email, luong));
            }
        } catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
        return list;
    }

    // Kiểm tra trùng nhân viên theo mã
    public static boolean employeeExists(String maNV) throws SQLException {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE ma_nv = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // Thêm nhân viên
    public static void insertEmployee(String maNV, String ten, int tuoi, String email, double luong) throws SQLException {

        String sql = "INSERT INTO NhanVien (ma_nv, ten, tuoi, email, luong) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            System.out.println("Thread: "+ Thread.currentThread().getName()+ " dùng connection:"+getConnection().hashCode());
            pstmt.setString(1, maNV);
            pstmt.setString(2, ten);
            pstmt.setInt(3, tuoi);
            pstmt.setString(4, email);
            pstmt.setDouble(5, luong);
            pstmt.executeUpdate();
        }
    }

    // Cập nhật nhân viên
    public static boolean updateEmployee(String maNV, String ten, int tuoi, String email, double luong) throws SQLException {

        String sql = "UPDATE NhanVien SET ten = ?, tuoi = ?, email = ?, luong = ? WHERE ma_nv = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            System.out.println("Thread: "+ Thread.currentThread().getName()+ " dùng connection:"+getConnection().hashCode());
            pstmt.setString(1, ten);
            pstmt.setInt(2, tuoi);
            pstmt.setString(3, email);
            pstmt.setDouble(4, luong);
            pstmt.setString(5, maNV);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        }
    }

    // Xóa nhân viên
    public static void deleteEmployee(String maNV) throws SQLException {

        String sql = "DELETE FROM NhanVien WHERE ma_nv = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            System.out.println("Thread: "+ Thread.currentThread().getName()+ " dùng connection:"+getConnection().hashCode());
            pstmt.setString(1, maNV);
            pstmt.executeUpdate();
        }
    }

    // Tìm kiếm nhân viên theo mã
    public static MainQLNVv2.NhanVien findEmployeeById(String maNV) throws SQLException {

        String sql = "SELECT * FROM NhanVien WHERE ma_nv = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            System.out.println("Thread: "+ Thread.currentThread().getName()+ " dùng connection:"+getConnection().hashCode());
            pstmt.setString(1, maNV);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                String ten = rs.getString("ten");
                int tuoi = rs.getInt("tuoi");
                String email = rs.getString("email");
                double luong = rs.getDouble("luong");
                return new MainQLNVv2.NhanVien(maNV, ten, tuoi, email, luong);
            }
        }
        return null;
    }

    // Import từ CSV
    public static void importFromCSV(String filePath) {

        String sql = "INSERT INTO NhanVien(ma_nv, ten, tuoi, email, luong) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE ten = VALUES(ten), tuoi = VALUES(tuoi), email = VALUES(email), luong = VALUES(luong)";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             Connection conn= getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if(line.trim().isEmpty() || line.toLowerCase().startsWith("ma_nv"))
                    continue;
                String[] parts = line.split(",", -1);
                if (parts.length != 5)
                    continue;
                try {
                    String maNV = parts[0].trim();
                    String ten = parts[1].trim();
                    int tuoi = Integer.parseInt(parts[2].trim());
                    String email = parts[3].trim();
                    double luong = Double.parseDouble(parts[4].trim());
                    pstmt.setString(1, maNV);
                    pstmt.setString(2, ten);
                    pstmt.setInt(3, tuoi);
                    pstmt.setString(4, email);
                    pstmt.setDouble(5, luong);
                    pstmt.addBatch();
                } catch (NumberFormatException e) {
                    // Bỏ qua dòng lỗi định dạng số
                }
            }
            pstmt.executeBatch();
            JOptionPane.showMessageDialog(null, "Import dữ liệu thành công!");
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi import file CSV: " + filePath);
        }
    }
    //Xuất ds
    public static void xuatDanhSachRaCSV() {
        String sql = "SELECT * FROM NhanVien";
        File file = new File("DanhSachNhanVien.csv");

        try (
             Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write("Mã NV,Họ Tên,Tuổi,Email,Lương (VND)");
            writer.newLine();
            DecimalFormat df = new DecimalFormat("#.###");

            while (rs.next()) {
                String ma = rs.getString("ma_nv");
                String ten = rs.getString("ten");
                int tuoi = rs.getInt("tuoi");
                String email = rs.getString("email");
                double luong = rs.getDouble("luong");

                writer.write(ma + "," + ten + "," + tuoi + "," + email + "," + df.format(luong));
                writer.newLine();
            }

            JOptionPane.showMessageDialog(null, "Đã xuất danh sách ra DanhSachNhanVien.csv");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất file: " + ex.getMessage());
        }
    }

    // Export ra CSV
    public static void exportToCSV(String filePath) {
        List<MainQLNVv2.NhanVien> list = loadEmployees();
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            pw.println("Mã NV,Họ Tên,Tuổi,Email,Lương (VND)");
            DecimalFormat df = new DecimalFormat("#.###");
            for(MainQLNVv2.NhanVien nv : list) {
                pw.printf("%s,%s,%d,%s,%s%n", nv.maNhanVien, nv.hoTen, nv.tuoi, nv.email, df.format(nv.luongCoBan));
            }
            JOptionPane.showMessageDialog(null, "Export thành công!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất file: " + filePath);
        }
    }
}
