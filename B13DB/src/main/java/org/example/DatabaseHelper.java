package org.example; // Đảm bảo đúng package của bạn

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal; // Sử dụng BigDecimal cho tiền tệ
import java.sql.Timestamp; // Để làm việc với thời gian


public class DatabaseHelper {

    public static Connection getConnection() throws SQLException {
        return ConnectionPool.getDataSource().getConnection();
    }

    // Phương thức đóng kết nối và các tài nguyên liên quan
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Phương thức đóng kết nối và PreparedStatement
    public static void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // Lấy tất cả sản phẩm (còn hàng)
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT product_id, name, description, price, image_path, is_available FROM PRODUCTS WHERE is_available = TRUE";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                BigDecimal price = rs.getBigDecimal("price");
                String imagePath = rs.getString("image_path");
                boolean isAvailable = rs.getBoolean("is_available");
                products.add(new Product(id, name, description, price, imagePath, isAvailable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách sản phẩm: " + e.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        } finally {
            closeResources(conn, stmt, rs);
        }
        return products;
    }

    // Tìm sản phẩm theo ID
    public Product findProductById(int productId) {
        String sql = "SELECT product_id, name, description, price, image_path, is_available FROM PRODUCTS WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                BigDecimal price = rs.getBigDecimal("price");
                String imagePath = rs.getString("image_path");
                boolean isAvailable = rs.getBoolean("is_available");
                return new Product(id, name, description, price, imagePath, isAvailable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm sản phẩm: " + e.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null; // Không tìm thấy sản phẩm
    }

    // Tìm sản phẩm theo tên hoặc mô tả (sử dụng LIKE)
    public List<Product> findProductsByNameOrDescription(String searchTerm) {
        List<Product> products = new ArrayList<>();
        // Tìm kiếm trong cả tên và mô tả, không phân biệt chữ hoa chữ thường
        String sql = "SELECT product_id, name, description, price, image_path, is_available FROM PRODUCTS WHERE (LOWER(name) LIKE ? OR LOWER(description) LIKE ?) AND is_available = TRUE";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            // Thêm '%' vào chuỗi tìm kiếm các chuỗi con
            String likeTerm = "%" + searchTerm.toLowerCase() + "%";
            pstmt.setString(1, likeTerm);
            pstmt.setString(2, likeTerm);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                BigDecimal price = rs.getBigDecimal("price");
                String imagePath = rs.getString("image_path");
                boolean isAvailable = rs.getBoolean("is_available");
                products.add(new Product(id, name, description, price, imagePath, isAvailable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm sản phẩm: " + e.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return products;
    }

    // Thêm sản phẩm mới
    public boolean insertProduct(Product product) {
        String sql = "INSERT INTO PRODUCTS (name, description, price, image_path, is_available) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setString(4, product.getImagePath());
            pstmt.setBoolean(5, product.isAvailable());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm sản phẩm: " + e.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    // Cập nhật thông tin sản phẩm
    public boolean updateProduct(Product product) {
        String sql = "UPDATE PRODUCTS SET name = ?, description = ?, price = ?, image_path = ?, is_available = ? WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setString(4, product.getImagePath());
            pstmt.setBoolean(5, product.isAvailable());
            pstmt.setInt(6, product.getProductId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật sản phẩm: " + e.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }


    // Cập nhật trạng thái is_available thành FALSE (Ngừng kinh doanh)
    public boolean deactivateProduct(int productId) {
        String sql = "UPDATE PRODUCTS SET is_available = FALSE WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật trạng thái sản phẩm: " + e.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }


    // Lấy danh sách đơn hôm nay
    public List<Order> getTodayOrders() {
        List<Order> orders = new ArrayList<>();
        // Sửa đổi truy vấn SQL để JOIN với bảng PAYMENTS
        String sql = "SELECT o.order_id, o.order_datetime, o.total_amount, o.status, p.payment_method " +
                "FROM ORDERS o LEFT JOIN PAYMENTS p ON o.order_id = p.order_id " +
                "WHERE DATE(o.order_datetime) = CURRENT_DATE"; // Sử dụng alias 'o'

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("order_id");
                Timestamp datetime = rs.getTimestamp("order_datetime");
                BigDecimal total = rs.getBigDecimal("total_amount");
                String status = rs.getString("status");
                String paymentMethod = rs.getString("payment_method");


                orders.add(new Order(id, datetime, total, status, paymentMethod));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách đơn hôm nay: " + e.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return orders;
    }

    //Phương thức Tài Khoản
    public boolean checkLogin(String username,String password){
        String sql = "SELECT * FROM USERS WHERE BINARY username = ? AND password = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            rs=pstmt.executeQuery();
            return rs.next();
        }catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Lỗi kiểu kiểm tra đăng nhập: "+e.getMessage(),"lỗi DB",JOptionPane.ERROR_MESSAGE);

        }finally {
            closeResources(conn,pstmt,rs);
        }
        return false;
    }




    // Phương thức này sẽ lưu Order, các OrderItem và Payment trong một transaction
    public boolean saveOrderAndPayment(Order order, List<OrderItemEntry> orderItems, Payment payment) {
        Connection conn = null;
        PreparedStatement pstmtOrder = null;
        PreparedStatement pstmtOrderItem = null;
        PreparedStatement pstmtPayment = null;
        ResultSet rs = null; // Để lấy generated key

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // Lưu Order vào bảng ORDERS
            String insertOrderSQL = "INSERT INTO ORDERS (order_datetime, total_amount, status, notes) VALUES (?, ?, ?, ?)";

            pstmtOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            pstmtOrder.setTimestamp(1, new Timestamp(System.currentTimeMillis())); // Thời gian hiện tại
            pstmtOrder.setBigDecimal(2, order.getTotalAmount());
            pstmtOrder.setString(3, order.getStatus());
            pstmtOrder.setString(4, order.getNotes());

            int affectedRows = pstmtOrder.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            // Lấy order_id vừa được tạo
            int orderId = -1;
            rs = pstmtOrder.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1); // Cột đầu tiên là khóa tự tạo
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }

            // Lưu các OrderItem vào bảng ORDER_ITEMS
            String insertOrderItemSQL = "INSERT INTO ORDER_ITEMS (order_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
            pstmtOrderItem = conn.prepareStatement(insertOrderItemSQL);

            for (OrderItemEntry item : orderItems) {
                pstmtOrderItem.setInt(1, orderId);
                pstmtOrderItem.setInt(2, item.getProduct().getProductId());
                pstmtOrderItem.setInt(3, item.getQuantity());
                pstmtOrderItem.setBigDecimal(4, item.getUnitPrice());
                pstmtOrderItem.setBigDecimal(5, item.getSubtotal());
                pstmtOrderItem.addBatch(); // Thêm vào batch để chạy hiệu quả hơn
            }
            pstmtOrderItem.executeBatch(); // Thực thi batch insert

            // Lưu Payment vào bảng PAYMENTS
            String insertPaymentSQL = "INSERT INTO PAYMENTS (order_id, payment_datetime, amount_paid, payment_method, transaction_id) VALUES (?, ?, ?, ?, ?)";
            pstmtPayment = conn.prepareStatement(insertPaymentSQL);
            pstmtPayment.setInt(1, orderId);
            pstmtPayment.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Thời gian thanh toán
            pstmtPayment.setBigDecimal(3, payment.getAmountPaid());
            pstmtPayment.setString(4, payment.getPaymentMethod());
            pstmtPayment.setString(5, payment.getTransactionId()); // transaction_id có thể null

            pstmtPayment.executeUpdate();

            conn.commit(); // Hoàn thành Transaction
            return true;

        } catch (SQLException e) {
            // Xảy ra lỗi, thực hiện rollback
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lưu đơn hàng/thanh toán: " + e.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Đóng tài nguyên trong finally
            closeResources(null, pstmtOrder, rs); // rs từ pstmtOrder
            closeResources(null, pstmtOrderItem, null);
            closeResources(conn, pstmtPayment, null); // Đóng conn ở đây
        }
        return false;
    }


}