package org.example;

import java.sql.*;

public class mainJDBC {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/jdbc_test";
        String username = "Trong";
        String password = "31122003";

        try {
            // 1. Kết nối tới DB
            Connection conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Đã kết nối MySQL!");


            String insertSQL = "INSERT INTO user (name, email) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, "Nguyễn Văn D");
            pstmt.setString(2, "D@gmail.com");
            pstmt.executeUpdate();
            System.out.println("Đã thêm user mới.");


            String selectSQL = "SELECT * FROM user";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);

            System.out.println("Danh sách user:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println(id + " - " + name + " - " + email);
            }


            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
