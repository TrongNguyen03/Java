package org.example;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

class Product {
    private int productId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imagePath;
    private boolean isAvailable;


    // Constructor đầy đủ
    public Product(int productId, String name, String description, BigDecimal price, String imagePath, boolean isAvailable) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
        this.isAvailable = isAvailable;
    }

    // Constructor cho sản phẩm mới
    public Product(String name, String description, BigDecimal price, String imagePath, boolean isAvailable) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
        this.isAvailable = isAvailable;
    }


    // Getters
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public boolean isAvailable() { return isAvailable; }


    @Override
    public String toString() {
        return name + " - " + price;
    }

    //Ô sản phẩm
    public JButton toDisplayComponent(ActionListener clickAction) {
        JButton button = new JButton();
        // Sử dụng BorderLayout cho nút chính để chia thành 2 vùng: Ảnh (WEST) và Text (CENTER)
        button.setLayout(new BorderLayout(5, 5)); // 5px khoảng cách giữa các thành phần

        ImageIcon icon = null;
        int imageSize = 100; // Kích thước ảnh
        if (this.imagePath != null && !this.imagePath.isEmpty()) {
            try {
                if (this.imagePath.startsWith("http")) {
                    // Ảnh từ URL online
                    URL imageUrl = new URL(this.imagePath);
                    ImageIcon originalIcon = new ImageIcon(imageUrl);
                    Image img = originalIcon.getImage().getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(img);
                } else {
                    // Ảnh nội bộ từ file
                    File imageFile = new File(this.imagePath);
                    if (imageFile.exists()) {
                        ImageIcon originalIcon = new ImageIcon(this.imagePath);
                        Image img = originalIcon.getImage().getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(img);
                    } else {
                        System.err.println("Image file not found: " + this.imagePath);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + this.imagePath + " - " + e.getMessage());
            }
        }

        JLabel imageLabel = new JLabel(icon, SwingConstants.CENTER);
        if (icon == null) {
            imageLabel.setText("No Img");
            imageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            imageLabel.setVerticalTextPosition(SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(imageSize, imageSize));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Thêm border để dễ nhìn vị trí
        } else {
            imageLabel.setPreferredSize(new Dimension(imageSize, imageSize)); // Đảm bảo kích thước cố định
        }



        // Panel chứa thông tin Văn bản
        JPanel textPanel = new JPanel();
        // Sử dụng BoxLayout để xếp các nhãn văn bản theo chiều dọc
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false); // Làm panel trong suốt để màu nền của nút hiển thị

        // Tạo các nhãn cho từng thông tin
        JLabel idLabel = new JLabel("ID: " + this.productId);
        idLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        JLabel nameLabel = new JLabel("<html><b>" + this.name + "</b></html>");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Định dạng giá tiền có dấu phân cách hàng nghìn
        DecimalFormat currencyFormat = new DecimalFormat("#,###");
        JLabel priceLabel = new JLabel(currencyFormat.format(this.price) + " VND");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        // Căn lề trái cho các nhãn trong textPanel
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Thêm các nhãn vào textPanel
        textPanel.add(idLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2))); // Thêm khoảng cách dọc 2px
        textPanel.add(nameLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2))); // Thêm khoảng cách
        textPanel.add(priceLabel);


        // Panel bao bọc để căn giữa khối văn bản theo chiều dọc
        JPanel centerWrapperPanel = new JPanel();
        // Sử dụng BoxLayout để thêm "glue" đẩy textPanel vào giữa
        centerWrapperPanel.setLayout(new BoxLayout(centerWrapperPanel, BoxLayout.Y_AXIS));
        centerWrapperPanel.setOpaque(false); // Làm panel trong suốt

        // Thêm "glue" ở trên textPanel để đẩy nó xuống
        centerWrapperPanel.add(Box.createVerticalGlue());
        // Thêm textPanel vào panel bao bọc
        centerWrapperPanel.add(textPanel);
        // Thêm "glue" ở dưới textPanel để đẩy nó lên
        centerWrapperPanel.add(Box.createVerticalGlue());

        // Đặt nhãn ảnh vào vị trí WEST (bên trái) của nút
        button.add(imageLabel, BorderLayout.WEST);
        // Đặt panel bao bọc (chứa textPanel và glue) vào vị trí CENTER (bên phải ảnh) của nút
        button.add(centerWrapperPanel, BorderLayout.CENTER);


        // Tăng chiều rộng để chứa cả ảnh và text, chiều cao khoảng bằng chiều cao ảnh + padding
        button.setPreferredSize(new Dimension(250, imageSize + 10)); // Điều chỉnh kích thước

        button.putClientProperty("productId", this.productId);
        if (clickAction != null) {
            button.addActionListener(clickAction);
        }

        return button;
    }
}


// Lớp helper để lưu trữ mục đơn hàng tạm thời trong bộ nhớ
class OrderItemEntry {
    private Product product;
    private int quantity;
    private BigDecimal unitPrice; // Giá tại thời điểm thêm vào đơn hàng
    private BigDecimal subtotal;

    public OrderItemEntry(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice(); // Lấy giá hiện tại của sản phẩm
        this.subtotal = this.unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Getters
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getSubtotal() { return subtotal; }

    // Setters (để cập nhật số lượng)
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = this.unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public Object[] toTableRow() {
        return new Object[]{
                product.getName(),
                quantity,
                unitPrice,
                subtotal
        };
    }
}

// Lớp Model cho Order
class Order {
    private BigDecimal totalAmount;
    private String status;
    private String notes;
    private int orderId;
    private Timestamp orderDatetime;
    private String paymentMethod;

    // Constructor cho Order mới
    public Order(BigDecimal totalAmount, String status, String notes) {
        this.totalAmount = totalAmount;
        this.status = status;
        this.notes = notes;

    }

    // Constructor cho đơn đã lưu
    public Order(int orderId, Timestamp orderDatetime, BigDecimal totalAmount, String status,String paymentMethod) {
        this.orderId = orderId;
        this.orderDatetime = orderDatetime;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }

    // Getters
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public int getOrderId() { return orderId; }
    public Timestamp getOrderDatetime() { return orderDatetime; }
    public String getPaymentMethod() { return paymentMethod; }

}

class Payment {
    private int orderId;
    private BigDecimal amountPaid;
    private String paymentMethod;
    private String transactionId;

    public Payment(int orderId, BigDecimal amountPaid, String paymentMethod, String transactionId) {
        this.orderId = orderId;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
    }

    // Getters
    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }
}



//Lớp chính của ứng dụng GUI
public class ThuNganCafe extends JFrame { // Đổi tên lớp

    private JTextField tfProductId, tfProductName, tfPrice, tfImagePath, tfDescription; // Trường nhập liệu sản phẩm
    private JPanel productDisplayPanel; // Panel hiển thị các sản phẩm có sẵn
    private DefaultTableModel orderTableModel; // Model cho bảng chi tiết đơn hàng
    private JTable orderTable; // Bảng chi tiết đơn hàng
    private JLabel totalLabel; // Label hiển thị tổng tiền đơn hàng

    private List<OrderItemEntry> currentOrderItems; // Danh sách các mục trong đơn hàng hiện tại (trong bộ nhớ)
    private BigDecimal currentTotal;

    private DatabaseHelper dbHelper;
    private JTextField tfSearch;

    public ThuNganCafe() { // Đổi tên constructor
        super("Hệ Thống POS Quán Cafe"); // Đổi tiêu đề
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700); // Tăng kích thước để có không gian hiển thị sản phẩm và đơn hàng
        setLocationRelativeTo(null);

        dbHelper = new DatabaseHelper();
        currentOrderItems = new ArrayList<>();
        currentTotal = BigDecimal.ZERO;

        initComponents(); // Khởi tạo các thành phần GUI
        // Gọi loadProductsDisplay với null để hiển thị tất cả sản phẩm ban đầu
        loadProductsDisplay(null);
        updateOrderTable(); // Cập nhật bảng đơn hàng ban đầu
        updateTotalLabel(); // Cập nhật tổng tiền ban đầu
    }

    private void initComponents() {

        //Background
        setLayout(new BorderLayout());

        // Panel Quản lý Sản phẩm
        JPanel productManagementPanel = new JPanel();
        productManagementPanel.setLayout(null);
        productManagementPanel.setPreferredSize(new Dimension(200, 200)); // Kích thước gợi ý

        // Tiêu đề và đồng hồ (giữ nguyên vị trí)
        JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM & THANH TOÁN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(79, 221, 127));
        lblTitle.setBounds(300, 5, 400, 30);
        productManagementPanel.add(lblTitle);

        JLabel lblTime = new JLabel();
        lblTime.setFont(new Font("Arial", Font.BOLD, 14));
        lblTime.setForeground(Color.RED);
        lblTime.setBounds(880, 5, 100, 30);
        productManagementPanel.add(lblTime);
        new Timer(1000, e -> lblTime.setText(new SimpleDateFormat("hh:mm a").format(new Date()))).start();

        // Các trường nhập liệu Sản phẩm
        int startY = 40; // Bắt đầu dưới tiêu đề
        int labelWidth = 120, fieldWidth = 200, height = 25, gapY = 30; // Điều chỉnh kích thước và khoảng cách

        JLabel lblProductId = new JLabel("Product ID:");
        lblProductId.setBounds(20, startY, labelWidth, height);
        productManagementPanel.add(lblProductId);
        tfProductId = new JTextField();
        tfProductId.setBounds(150, startY, fieldWidth, height);
        tfProductId.setEditable(false);
        productManagementPanel.add(tfProductId);

        JLabel lblProductName = new JLabel("Tên sản phẩm:");
        lblProductName.setBounds(20, startY + gapY, labelWidth, height);
        productManagementPanel.add(lblProductName);
        tfProductName = new JTextField();
        tfProductName.setBounds(150, startY + gapY, fieldWidth, height);
        productManagementPanel.add(tfProductName);

        JLabel lblPrice = new JLabel("Giá (VND):");
        lblPrice.setBounds(20, startY + 2 * gapY, labelWidth, height);
        productManagementPanel.add(lblPrice);
        tfPrice = new JTextField();
        tfPrice.setBounds(150, startY + 2 * gapY, fieldWidth, height);
        productManagementPanel.add(tfPrice);

        JLabel lblImagePath = new JLabel("Đường dẫn ảnh:");
        lblImagePath.setBounds(20, startY + 3 * gapY, labelWidth, height);
        productManagementPanel.add(lblImagePath);
        tfImagePath = new JTextField();
        tfImagePath.setBounds(150, startY + 3 * gapY, fieldWidth, height);
        productManagementPanel.add(tfImagePath);

        JLabel lblDescription = new JLabel("Mô tả:");
        lblDescription.setBounds(20, startY + 4 * gapY, labelWidth, height);
        productManagementPanel.add(lblDescription);
        tfDescription = new JTextField(); // Có thể dùng JTextArea trong JScrollPane cho mô tả dài
        tfDescription.setBounds(150, startY + 4 * gapY, fieldWidth, height);
        productManagementPanel.add(tfDescription);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setBounds(400, startY+100, labelWidth, height);
        productManagementPanel.add(lblSearch);
        tfSearch = new JTextField();
        tfSearch.setBounds(400, startY + 4 * gapY,fieldWidth-50, height);
        productManagementPanel.add(tfSearch);

        // Các nút chức năng Sản phẩm
        int btnX = 400, btnY = 40, btnWidth = 150, btnHeight = 25, btnGap = 35; // Điều chỉnh vị trí và khoảng cách

        JButton btnAddProduct = new JButton("Thêm sản phẩm");
        btnAddProduct.setBounds(btnX, btnY, btnWidth, btnHeight);
        productManagementPanel.add(btnAddProduct);

        JButton btnUpdateProduct = new JButton("Cập nhật");
        btnUpdateProduct.setBounds(btnX, btnY + btnGap, btnWidth, btnHeight);
        productManagementPanel.add(btnUpdateProduct);

        JButton btnDeleteProduct = new JButton("Bỏ Sp theo ID");
        btnDeleteProduct.setBounds(btnX, btnY + 2 * btnGap, btnWidth, btnHeight);
        productManagementPanel.add(btnDeleteProduct);

        JButton btnClearFields = new JButton("Clear Form");
        btnClearFields.setBounds(btnX + 200, btnY, btnWidth, btnHeight);
        productManagementPanel.add(btnClearFields);

        JButton btnOrderToday = new JButton("Order Today");
        btnOrderToday.setBounds(btnX + 200, btnY +btnGap, btnWidth, btnHeight);
        productManagementPanel.add(btnOrderToday);


        add(productManagementPanel, BorderLayout.NORTH); // Đặt panel quản lý sản phẩm lên trên


        // Panel Hiển thị Sản phẩm và Đơn hàng/Thanh toán
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Panel hiển thị Sản phẩm (Left Side)
        productDisplayPanel = new JPanel();
        productDisplayPanel.setLayout(new GridLayout(0, 1, 10, 10));

        JScrollPane productScrollPane = new JScrollPane(productDisplayPanel);
        productScrollPane.setPreferredSize(new Dimension(150, 150));// Kích thước gợi ý cho panel sản phẩm

        // Panel Đơn hàng và Thanh toán (Right Side)
        JPanel orderPaymentPanel = new JPanel();
        orderPaymentPanel.setLayout(new BorderLayout());

        // Bảng chi tiết đơn hàng
        String[] orderColumnNames = {"Sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
        orderTableModel = new DefaultTableModel(orderColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderTableModel);

        TableRowSorter<DefaultTableModel> orderSorter = new TableRowSorter<>(orderTableModel);
        orderTable.setRowSorter(orderSorter);
        JScrollPane orderScrollPane = new JScrollPane(orderTable);

        // Panel Tổng tiền và Thanh toán
        JPanel paymentArea = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Tổng cộng: 0.00 VND");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 24));
        totalLabel.setForeground(Color.BLUE);

        JButton btnPay = new JButton("THANH TOÁN");
        btnPay.setFont(new Font("Arial", Font.BOLD, 18));
        btnPay.setBackground(Color.GREEN);
        btnPay.setForeground(Color.WHITE);


        paymentArea.add(totalLabel);
        paymentArea.add(Box.createHorizontalStrut(10));
        paymentArea.add(btnPay);

        orderPaymentPanel.add(orderScrollPane, BorderLayout.CENTER);
        orderPaymentPanel.add(paymentArea, BorderLayout.SOUTH);

        mainSplitPane.setLeftComponent(productScrollPane);
        mainSplitPane.setRightComponent(orderPaymentPanel);
        mainSplitPane.setResizeWeight(0.4);

        add(mainSplitPane, BorderLayout.CENTER); // Đặt JSplitPane vào giữa JFrame


        // DocumentListener cho thanh tìm kiếm để lọc sản phẩm khi gõ
        tfSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterProducts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterProducts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // changedUpdate dùng cho các thay đổi thuộc tính (ít dùng với JTextField)
                // filterProducts(); // Có thể gọi hoặc không, tùy nhu cầu
            }

            private void filterProducts() {
                String searchTerm = tfSearch.getText();
                // Gọi lại hàm tải và hiển thị với chuỗi tìm kiếm
                loadProductsDisplay(searchTerm);
            }
        });

        // Thêm Sản phẩm
        btnAddProduct.addActionListener(e -> {
            // Lấy dữ liệu từ form
            String name = tfProductName.getText().trim();
            String priceStr = tfPrice.getText().trim();
            String imagePath = tfImagePath.getText().trim();
            String description = tfDescription.getText().trim();

            // Kiểm tra dữ liệu đầu vào cơ bản
            if (name.isEmpty() || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên và Giá sản phẩm không được rỗng!");
                return;
            }

            try {
                BigDecimal price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    JOptionPane.showMessageDialog(this, "Giá không thể là số âm!");
                    return;
                }

                // Tạo đối tượng Product (không cần ID vì DB tự tạo)
                Product newProduct = new Product(name, description, price, imagePath, true); // Mặc định là còn hàng

                // Lưu vào database
                boolean success = dbHelper.insertProduct(newProduct);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                    clearProductFields();
                    loadProductsDisplay(tfSearch.getText());
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá sản phẩm phải là số hợp lệ!");
            }
        });

        // Cập nhật Sản phẩm
        btnUpdateProduct.addActionListener(e -> {
            String productIdStr = JOptionPane.showInputDialog(this, "Vui lòng nhập ID sản phẩm cần sửa");

            if (productIdStr == null || productIdStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID sản phẩm không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int productId;
            try {
                productId = Integer.parseInt(productIdStr.trim());
                if (productId <= 0) {
                    JOptionPane.showMessageDialog(this, "ID sản phẩm phải là số dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID sản phẩm phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product product = dbHelper.findProductById(productId);
            if (product != null) {

                JTextField tfName = new JTextField(product.getName(), 20);
                JTextField tfPrice = new JTextField(product.getPrice().toPlainString(), 20);
                JTextField tfDescription = new JTextField(product.getDescription(), 20);
                JTextField tfImage = new JTextField(product.getImagePath(), 20);

                JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
                panel.add(new JLabel("Tên sản phẩm:"));
                panel.add(tfName);
                panel.add(new JLabel("Giá:"));
                panel.add(tfPrice);
                panel.add(new JLabel("Mô tả:"));
                panel.add(tfDescription);
                panel.add(new JLabel("Đường dẫn hình ảnh:"));
                panel.add(tfImage);

                int result = JOptionPane.showConfirmDialog(this, panel, "Sửa thông tin sản phẩm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String name = tfName.getText().trim();
                        String priceStr = tfPrice.getText().trim();
                        String description = tfDescription.getText().trim();
                        String imagePath = tfImage.getText().trim();

                        if (name.isEmpty() || priceStr.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Tên và Giá không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        BigDecimal price = new BigDecimal(priceStr);
                        if (price.compareTo(BigDecimal.ZERO) < 0) {
                            JOptionPane.showMessageDialog(this, "Giá phải là số dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        Product updatedProduct = new Product(productId, name, description, price, imagePath, true);
                        boolean success = dbHelper.updateProduct(updatedProduct);

                        if (success) {
                            JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
                            loadProductsDisplay(tfSearch.getText()); // Tải lại giữ nguyên bộ lọc nếu có
                        } else {
                            JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Giá phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với ID đã nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ngừng kinh doanh
        btnDeleteProduct.addActionListener(e -> {
            String productIdStr = JOptionPane.showInputDialog(this, "Vui lòng nhập ID để ngừng kinh doanh!");
            if (productIdStr != null && !productIdStr.trim().isEmpty()) {
                try {
                    int productId = Integer.parseInt(productIdStr);
                    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn ngừng kinh doanh sản phẩm ID " + productId + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = dbHelper.deactivateProduct(productId);

                        if (success) {
                            JOptionPane.showMessageDialog(this, "Đã ngừng kinh doanh sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                            clearProductFields();
                            loadProductsDisplay(tfSearch.getText()); // Tải lại giữ nguyên bộ lọc nếu có
                        } else {
                            JOptionPane.showMessageDialog(this, "Ngừng kinh doanh sản phẩm thất bại! Có thể không tìm thấy ID.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Product ID phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không để chống ID", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Clear Form Sản phẩm
        btnClearFields.addActionListener(e -> clearProductFields());

        // Thanh toán
        btnPay.addActionListener(e -> processPayment());

        //Order Today
        btnOrderToday.addActionListener(e-> showTodayOrders());

        // Optional: Click vào dòng trong bảng đơn hàng để xóa mục?
        orderTable.addMouseListener(new MouseAdapter() {
            Timer clickTimer; // Timer để phân biệt click đơn và double

            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = orderTable.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    if (evt.getClickCount() == 2) { // Double click để xóa
                        if (clickTimer != null && clickTimer.isRunning()) {
                            clickTimer.stop(); // Hủy xử lý click đơn
                        }
                        int confirm = JOptionPane.showConfirmDialog(ThuNganCafe.this,
                                "Bạn có muốn xóa mục \"" + currentOrderItems.get(row).getProduct().getName() + "\" khỏi đơn hàng?",
                                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            currentOrderItems.remove(row);
                            updateOrderTable();
                            updateTotalLabel();
                        }
                    } else if (evt.getClickCount() == 1) { // Single click, đợi xem có double click không
                        if (clickTimer != null && clickTimer.isRunning()) {
                            clickTimer.stop();
                        }
                        clickTimer = new Timer(300, e -> {
                            OrderItemEntry item = currentOrderItems.get(row);
                            String input = JOptionPane.showInputDialog(ThuNganCafe.this,
                                    "Nhập số lượng mới cho \"" + item.getProduct().getName() + "\":",
                                    item.getQuantity());

                            if (input != null) {
                                try {
                                    int newQuantity = Integer.parseInt(input.trim());
                                    if (newQuantity > 0) {
                                        item.setQuantity(newQuantity);
                                        updateOrderTable();
                                        updateTotalLabel();
                                    } else {
                                        JOptionPane.showMessageDialog(ThuNganCafe.this,
                                                "Số lượng phải lớn hơn 0!",
                                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(ThuNganCafe.this,
                                            "Vui lòng nhập số hợp lệ!",
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                        clickTimer.setRepeats(false); // Chỉ chạy 1 lần
                        clickTimer.start();
                    }
                }
            }
        });

    }

    // Hàm tải và hiển thị sản phẩm từ database
    private void loadProductsDisplay(String searchTerm) {
        productDisplayPanel.removeAll(); // Xóa các sản phẩm cũ trước khi tải lại
        List<Product> products;

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            // Nếu chuỗi tìm kiếm rỗng, lấy tất cả sản phẩm còn hàng
            products = dbHelper.getAllProducts();
        } else {
            // Nếu có chuỗi tìm kiếm, gọi phương thức tìm kiếm mới theo tên/mô tả
            products = dbHelper.findProductsByNameOrDescription(searchTerm);
        }

        // Thêm listener chung cho các nút sản phẩm
        ActionListener productClickListener = e -> {
            JButton sourceButton = (JButton) e.getSource();
            int productId = (int) sourceButton.getClientProperty("productId");
            addProductToOrder(productId);
        };

        for (Product product : products) {
            JButton productButton = product.toDisplayComponent(productClickListener);
            productDisplayPanel.add(productButton);

        }

        productDisplayPanel.revalidate(); // Cập nhật layout
        productDisplayPanel.repaint();    // Vẽ lại
    }

    // Hàm thêm sản phẩm vào đơn hàng tạm thời
    private void addProductToOrder(int productId) {
        Product product = dbHelper.findProductById(productId); // Lấy thông tin sản phẩm từ DB (có thể cache để nhanh hơn)
        if (product == null || !product.isAvailable()) {
            JOptionPane.showMessageDialog(this, "Sản phẩm không tồn tại hoặc tạm hết hàng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean found = false;
        // Kiểm tra xem sản phẩm đã có trong đơn hàng chưa
        for (OrderItemEntry item : currentOrderItems) {
            if (item.getProduct().getProductId() == productId) {
                item.setQuantity(item.getQuantity() + 1); // Tăng số lượng
                found = true;
                break;
            }
        }

        // Nếu chưa có, thêm mới vào danh sách
        if (!found) {
            currentOrderItems.add(new OrderItemEntry(product, 1));
        }

        updateOrderTable();
        updateTotalLabel();
    }


    // Hàm cập nhật bảng chi tiết đơn hàng từ danh sách tạm thời
    private void updateOrderTable() {
        orderTableModel.setRowCount(0); // Xóa tất cả các dòng hiện có

        currentTotal = BigDecimal.ZERO; // Tính lại tổng tiền
        for (OrderItemEntry item : currentOrderItems) {
            orderTableModel.addRow(item.toTableRow()); // Thêm dòng mới
            currentTotal = currentTotal.add(item.getSubtotal()); // Cộng dồn vào tổng tiền
        }
    }

    // Hàm cập nhật Label hiển thị tổng tiền
    private void updateTotalLabel() {
        DecimalFormat currencyFormat = new DecimalFormat("#,###");
        totalLabel.setText(String.format("Tổng cộng: %s VND", currencyFormat.format(currentTotal)));
    }

    // Hàm xử lý thanh toán
    private void processPayment() {
        if (currentOrderItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Đơn hàng trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Hỏi phương thức thanh toán
        String[] paymentMethods = {"Tiền mặt", "Chuyển khoản", "Thẻ"};
        String selectedMethod = (String) JOptionPane.showInputDialog(
                this,
                "Chọn phương thức thanh toán:",
                "Thanh toán",
                JOptionPane.QUESTION_MESSAGE,
                null,
                paymentMethods,
                paymentMethods[0]
        );

        if (selectedMethod == null) {
            // Người dùng hủy thanh toán
            return;
        }

        // Hỏi số tiền khách trả
        BigDecimal amountPaid = BigDecimal.ZERO;
        if ("Tiền mặt".equals(selectedMethod)) {
            String amountPaidStr = JOptionPane.showInputDialog(this, "Nhập số tiền khách trả:");
            if (amountPaidStr == null || amountPaidStr.trim().isEmpty()) {
                return;
            }
            try {
                amountPaid = new BigDecimal(amountPaidStr);
                if (amountPaid.compareTo(currentTotal) < 0) {
                    JOptionPane.showMessageDialog(this, "Số tiền khách trả không đủ!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số tiền nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            // Các phương thức khác giả định thanh toán đủ
            amountPaid = currentTotal;
        }


        // Lưu Đơn hàng và Thanh toán vào Database
        try {
            Order newOrder = new Order(currentTotal, "Completed", "Thanh toán bằng " + selectedMethod); // Status: Completed, Notes: phương thức TT

            Payment payment = new Payment(-1, amountPaid, selectedMethod, null); // orderId sẽ được set trong saveOrderAndPayment

            boolean success = dbHelper.saveOrderAndPayment(newOrder, currentOrderItems, payment);

            if (success) {
                BigDecimal change = amountPaid.subtract(currentTotal);
                DecimalFormat currencyFormat = new DecimalFormat("#,###");

                String paymentSummary = "Thanh toán thành công!\n" +
                        "Tổng cộng: " + currencyFormat.format(currentTotal) + " VND\n" +
                        "Khách trả: " + currencyFormat.format(amountPaid) + " VND\n";

                if ("Tiền mặt".equals(selectedMethod)) {
                    paymentSummary += "Tiền thừa: " + currencyFormat.format(change) + " VND";
                } else {
                    paymentSummary += "Phương thức: " + selectedMethod;
                }


                JOptionPane.showMessageDialog(this, paymentSummary, "Thanh toán hoàn tất", JOptionPane.INFORMATION_MESSAGE);

                clearOrder();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Hàm xóa nội dung form nhập liệu sản phẩm
    private void clearProductFields() {
        tfProductId.setText("");
        tfProductName.setText("");
        tfPrice.setText("");
        tfImagePath.setText("");
        tfDescription.setText("");
        tfProductId.setEditable(false); // Đảm bảo ID vẫn không cho sửa
    }

    // Hàm hóa đơn ngày
    private void showTodayOrders() {
        List<Order> orders = dbHelper.getTodayOrders();

        // Thêm cột "Hình thức thanh toán"
        String[] columns = {"Mã đơn", "Thời gian", "Tổng tiền (VND)", "Trạng thái", "Hình thức thanh toán"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        DecimalFormat currencyFormat = new DecimalFormat("#,###");
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Order order : orders) {
            String formattedDatetime = (order.getOrderDatetime() == null) ? "N/A" : datetimeFormat.format(order.getOrderDatetime());
            String displayPaymentMethod = (order.getPaymentMethod() == null) ? "N/A" : order.getPaymentMethod();

            model.addRow(new Object[]{
                    order.getOrderId(),
                    formattedDatetime,
                    currencyFormat.format(order.getTotalAmount()),
                    order.getStatus(),
                    displayPaymentMethod
            });
            totalRevenue = totalRevenue.add(order.getTotalAmount());
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JLabel totalRevenueLabel = new JLabel("Tổng doanh thu hôm nay: " + currencyFormat.format(totalRevenue) + " VND");
        totalRevenueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalRevenueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(totalRevenueLabel, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(this, "Chi tiết đơn hàng hôm nay", true);
        dialog.getContentPane().add(panel);
        dialog.setSize(700, 400); // Có thể tăng kích thước dialog để chứa cột mới
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    // Hàm reset đơn hàng hiện tại
    private void clearOrder() {
        currentOrderItems.clear();
        currentTotal = BigDecimal.ZERO;
        updateOrderTable();
        updateTotalLabel();
    }

}