package com.example.PosCafe.view;

import com.example.PosCafe.model.Order;
import com.example.PosCafe.model.OrderItem;
import com.example.PosCafe.model.Payment;
import com.example.PosCafe.model.Product;
import com.example.PosCafe.service.OrderService;
import com.example.PosCafe.service.ProductService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class ThuNganCafe extends JFrame {
    private final ProductService productService;
    private final OrderService orderService;

    private JPanel productDisplayPanel;
    private DefaultTableModel orderTableModel;
    private JTable orderTable;
    private JLabel totalLabel;
    private JTextField tfSearch;
    private JTextField tfProductName, tfPrice, tfImagePath, tfDescription;

    private List<OrderItem> currentOrderItems = new ArrayList<>();
    private BigDecimal currentTotal = BigDecimal.ZERO;

    @Autowired
    public ThuNganCafe(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;

        setTitle("Hệ Thống POS Quán Cafe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());


        JPanel topPanel = new JPanel(null);
        topPanel.setPreferredSize(new Dimension(1000, 180));

        JLabel title = new JLabel("QUẢN LÝ SẢN PHẨM & THANH TOÁN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(0, 180, 100));
        title.setBounds(400, 5, 400, 30);
        topPanel.add(title);

        JLabel lblTime = new JLabel();
        lblTime.setFont(new Font("Arial", Font.BOLD, 14));
        lblTime.setForeground(Color.RED);
        lblTime.setBounds(1000, 5, 100, 30);
        topPanel.add(lblTime);
        new javax.swing.Timer(1000, e -> lblTime.setText(new SimpleDateFormat("hh:mm a").format(new Date()))).start();

        int  h = 25, w = 200, xL = 20, xF = 150;


        JLabel lblName = new JLabel("Tên sản phẩm:");
        lblName.setBounds(xL, 40, 120, h);
        tfProductName = new JTextField();
        tfProductName.setBounds(xF, 40, w, h);
        topPanel.add(lblName);
        topPanel.add(tfProductName);

        JLabel lblGia = new JLabel("Giá sản phẩm:");
        lblGia.setBounds(xL, 70, 120, h);
        tfPrice = new JTextField();
        tfPrice.setBounds(xF, 70, w, h);
        topPanel.add(lblGia);
        topPanel.add(tfPrice);

        JLabel lblAnh = new JLabel("Đường dẫn ảnh:");
        lblAnh.setBounds(xL, 100, 120, h);
        tfImagePath = new JTextField();
        tfImagePath.setBounds(xF, 100, w, h);
        topPanel.add(lblAnh);
        topPanel.add(tfImagePath);

        JLabel lblMota = new JLabel("Mô tả:");
        lblMota.setBounds(xL, 130, 120, h);
        tfDescription = new JTextField();
        tfDescription.setBounds(xF, 130, w, h);
        topPanel.add(lblMota);
        topPanel.add(tfDescription);


        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setBounds(600, 40, 100, 25);
        topPanel.add(lblSearch);
        tfSearch = new JTextField();
        tfSearch.setBounds(670, 40, 200, 25);
        tfSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { loadProducts(tfSearch.getText()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { loadProducts(tfSearch.getText()); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { }
        });
        topPanel.add(tfSearch);

        JButton btnAdd = new JButton("Thêm sản phẩm");
        btnAdd.addActionListener(e -> {
            String name = tfProductName.getText().trim();
            String priceStr = tfPrice.getText().trim();
            String imagePath = tfImagePath.getText().trim();
            String description = tfDescription.getText().trim();

            if (name.isEmpty() || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên và giá sản phẩm không được để trống!");
                return;
            }

            try {
                BigDecimal price = new BigDecimal(priceStr);
                Product p = new Product();
                p.setName(name);
                p.setPrice(price);
                p.setImagePath(imagePath);
                p.setDescription(description);
                p.setAvailable(true);

                productService.save(p);
                JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm thành công!");
                clearForm();
                loadProducts(tfSearch.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá sản phẩm không hợp lệ!");
            }
        });


        JButton btnUpdate = new JButton("Cập nhật");
        btnUpdate.addActionListener(e -> {
            String inputId = JOptionPane.showInputDialog(this, "Nhập ID sản phẩm cần cập nhật:");
            if (inputId == null || inputId.trim().isEmpty()) {
                return;
            }


            try {
                int id = Integer.parseInt(inputId.trim());
                Product existing = productService.getById(id);
                if (existing == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với ID này!");
                    return;
                }

                // Hiển thị hộp thoại sửa thông tin
                JTextField tfName = new JTextField(existing.getName());
                JTextField tfPrice = new JTextField(existing.getPrice().toPlainString());
                JTextField tfImage = new JTextField(existing.getImagePath());
                JTextField tfDesc = new JTextField(existing.getDescription());

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Tên sản phẩm:")); panel.add(tfName);
                panel.add(new JLabel("Giá:")); panel.add(tfPrice);
                panel.add(new JLabel("Đường dẫn ảnh:")); panel.add(tfImage);
                panel.add(new JLabel("Mô tả:")); panel.add(tfDesc);

                int result = JOptionPane.showConfirmDialog(this, panel, "Cập nhật sản phẩm", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    existing.setName(tfName.getText().trim());
                    existing.setPrice(new BigDecimal(tfPrice.getText().trim()));
                    existing.setImagePath(tfImage.getText().trim());
                    existing.setDescription(tfDesc.getText().trim());
                    //Available sau cập nhật
                    existing.setAvailable(true);

                    boolean ok = productService.update(existing);
                    if (ok) {
                        JOptionPane.showMessageDialog(this, "Đã cập nhật sản phẩm!");
                        loadProducts(tfSearch.getText());
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
            }
        });



        JButton btnDelete = new JButton("Bỏ Sp theo ID");
        btnDelete.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Nhập ID sản phẩm muốn ngừng kinh doanh:");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    boolean ok = productService.deactivate(id);
                    if (ok) {
                        JOptionPane.showMessageDialog(this, "Đã ngừng kinh doanh sản phẩm.");
                        loadProducts(tfSearch.getText());
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm hoặc không thể xóa!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID phải là số hợp lệ!");
                }
            }
        });

        JButton btnClear = new JButton("Clear Form");
        btnClear.addActionListener(e -> clearForm());

        JButton btnReport = new JButton("Order Today");
        btnReport.setBounds(670, 75, 150, 25);
        topPanel.add(btnReport);
        btnReport.addActionListener(e-> showTodayOrders() );


        int bx = 400, by = 40, bw = 150, bh = 25, bgap = 35;
        for (JButton b : new JButton[]{btnAdd, btnUpdate, btnDelete, btnClear}) {
            b.setBounds(bx, by, bw, bh);
            topPanel.add(b);
            by += bgap;
        }

        add(topPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        //panel hiển thị sản phẩm
        productDisplayPanel = new JPanel();
        productDisplayPanel.setLayout(new BoxLayout(productDisplayPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollLeft = new JScrollPane(productDisplayPanel);

        //Bảng chi tiết đơn hàng
        String[] cols = {"Sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
        orderTableModel = new DefaultTableModel(cols, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderTableModel);
        // Thêm TableRowSorter để sắp xếp bảng
        TableRowSorter<DefaultTableModel> orderSorter = new TableRowSorter<>(orderTableModel);
        orderTable.setRowSorter(orderSorter);
        // Tạo JScrollPane và thêm vào giao diện
        JScrollPane orderScrollPane = new JScrollPane(orderTable);

        // chuột trái sửa chuột phải xóa
        orderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = orderTable.rowAtPoint(e.getPoint());
                orderTable.setRowSelectionInterval(row, row);
                String productName = orderTable.getValueAt(row, 0).toString();


                if (SwingUtilities.isRightMouseButton(e)) {
                    int confirm = JOptionPane.showConfirmDialog(orderTable, "Xóa sản phẩm khỏi đơn hàng?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        currentOrderItems.removeIf(item -> item.getProduct().getName().equals(productName));
                        refreshOrderTable();
                    }
                } else if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {
                    String input = JOptionPane.showInputDialog(orderTable, "Nhập số lượng mới:");

                    if (input == null || input.trim().isEmpty()) {
                        return;
                    }
                    try {
                        int newQty = Integer.parseInt(input);
                        if (newQty <= 0) throw new NumberFormatException();
                        for (OrderItem item : currentOrderItems) {
                            if (item.getProduct().getName().equals(productName)) {
                                item.setQuantity(newQty);
                                item.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(newQty)));
                                break;
                            }
                        }
                        refreshOrderTable();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(orderTable, "Số lượng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        JScrollPane scrollRight = new JScrollPane(orderTable);


        JPanel bottomRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Tổng cộng: 0 VND");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 22));
        totalLabel.setForeground(Color.BLUE);

        JButton btnPay = new JButton("THANH TOÁN");
        btnPay.setFont(new Font("Arial", Font.BOLD, 18));
        btnPay.setBackground(Color.GREEN);
        btnPay.setForeground(Color.WHITE);
        btnPay.addActionListener(e -> processPayment());

        bottomRight.add(totalLabel);
        bottomRight.add(btnPay);

        //combine bên phải
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(scrollRight, BorderLayout.CENTER);
        rightPanel.add(bottomRight, BorderLayout.SOUTH);

        //combine 2 phần vào SPLITPANE
        splitPane.setLeftComponent(scrollLeft);
        splitPane.setRightComponent(rightPanel);
        splitPane.setResizeWeight(0.4);

        add(splitPane, BorderLayout.CENTER);

    }




    @PostConstruct
    private void initAfterStartup() {
        loadProducts(null);
    }

    private void loadProducts(String filter) {
        productDisplayPanel.removeAll();
        List<Product> products = (filter == null || filter.isEmpty())
                ? productService.getAllAvailable()
                : productService.search(filter);

        for (Product p : products) {
            productDisplayPanel.add(createProductButton(p));
        }
        productDisplayPanel.revalidate();
        productDisplayPanel.repaint();
    }


    private JPanel createProductButton(Product p) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setPreferredSize(new Dimension(250, 130));
        btn.setBackground(Color.WHITE);
        btn.setOpaque(true);


        JLabel img = new JLabel("No Img", SwingConstants.CENTER);
        img.setPreferredSize(new Dimension(100, 100));
        img.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        if (p.getImagePath() != null && !p.getImagePath().isEmpty()) {
            try {
                ImageIcon icon;
                if (p.getImagePath().startsWith("http")) {
                    java.net.URL url = new java.net.URL(p.getImagePath());
                    icon = new ImageIcon(url);
                } else {
                    java.io.File file = new java.io.File(p.getImagePath());
                    if (file.exists()) {
                        icon = new ImageIcon(p.getImagePath());
                    } else {
                        throw new Exception("Ảnh không tồn tại trong máy: " + p.getImagePath());
                    }
                }
                Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                img.setIcon(new ImageIcon(scaled));
                img.setText("");
            } catch (Exception e) {
                System.err.println("Không thể load ảnh: " + p.getImagePath() + " - " + e.getMessage());
            }
        }
        btn.add(img, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        info.add(new JLabel("ID: " + p.getProductId()));
        info.add(new JLabel(p.getName()));
        info.add(new JLabel(formatCurrency(p.getPrice())));
        info.add(new JLabel(p.getDescription()));

        btn.add(info, BorderLayout.CENTER);
        btn.putClientProperty("product", p);
        btn.addActionListener(e -> addProductToOrder(p));

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());
        wrapper.setMaximumSize(new Dimension(270, 140));
        wrapper.setOpaque(false);
        wrapper.add(btn, BorderLayout.CENTER);

        return wrapper;
    }

    private void addProductToOrder(Product p) {
        Optional<OrderItem> existing = currentOrderItems.stream()
                .filter(i -> i.getProduct().getProductId().equals(p.getProductId())).findFirst();

        if (existing.isPresent()) {
            OrderItem item = existing.get();
            item.setQuantity(item.getQuantity() + 1);
            item.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        } else {
            OrderItem item = new OrderItem();
            item.setProduct(p);
            item.setQuantity(1);
            item.setUnitPrice(p.getPrice());
            item.setSubtotal(p.getPrice());
            currentOrderItems.add(item);
        }

        refreshOrderTable();
    }

    private void refreshOrderTable() {
        orderTableModel.setRowCount(0);
        currentTotal = BigDecimal.ZERO;
        for (OrderItem item : currentOrderItems) {
            orderTableModel.addRow(new Object[]{
                    item.getProduct().getName(),
                    item.getQuantity(),
                    formatCurrency(item.getUnitPrice()),
                    formatCurrency(item.getSubtotal())
            });
            currentTotal = currentTotal.add(item.getSubtotal());
        }
        totalLabel.setText("Tổng cộng: " + formatCurrency(currentTotal));
    }

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
            return;
        }

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
            amountPaid = currentTotal; // Giả định đủ khi không dùng tiền mặt
        }

        try {
            // Tạo đơn hàng
            Order newOrder = new Order();
            newOrder.setOrderDatetime(new Timestamp(System.currentTimeMillis()));
            newOrder.setTotalAmount(currentTotal);
            newOrder.setStatus("Completed");
            newOrder.setNotes("Thanh toán bằng " + selectedMethod);

            // Tạo thanh toán
            Payment payment = new Payment();
            payment.setAmountPaid(amountPaid);
            payment.setPaymentMethod(selectedMethod);
            payment.setPaymentDatetime(new Timestamp(System.currentTimeMillis()));

            // Lưu vào DB
            boolean success = orderService.saveOrderAndPayment(newOrder, currentOrderItems, payment);

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

                clearOrder(); // Reset
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }



    private String formatCurrency(BigDecimal amount) {
        return new DecimalFormat("#,###").format(amount) + " VND";
    }

    private void clearForm() {
        tfProductName.setText("");
        tfPrice.setText("");
        tfImagePath.setText("");
        tfDescription.setText("");
    }

    private void clearOrder() {
        currentOrderItems.clear(); // Xóa danh sách item trong đơn hàng hiện tại
        currentTotal = BigDecimal.ZERO; // Đặt lại tổng tiền

        orderTableModel.setRowCount(0); // Xóa toàn bộ dòng trong bảng hiển thị
        totalLabel.setText("Tổng cộng: " + formatCurrency(currentTotal)); // Cập nhật nhãn tổng cộng
    }

    private JPanel contentPanel;

    private void showTodayOrders(){
        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnToday = new JButton("Xem đơn hôm nay");
        JButton btnMonth = new JButton("Xem đơn tháng này");
        JButton btnYear = new JButton("Xem đơn năm nay");

        topPanel.add(btnToday);
        topPanel.add(btnMonth);
        topPanel.add(btnYear);
        panel.add(topPanel, BorderLayout.NORTH);

        // Khởi tạo contentPanel
        contentPanel = new JPanel(new BorderLayout());
        panel.add(contentPanel, BorderLayout.CENTER); // Thêm contentPanel vào panel chính

        Consumer<String> updateTableByType = (type) -> {
            List<Order> orders;
            String prefix;
            switch (type){
                case "month":
                    orders = orderService.getMonthOrders();
                    prefix = "Tổng doanh thu tháng này: ";
                    break;
                case "year":
                    orders = orderService.getYearOrders();
                    prefix = "Tổng doanh thu năm nay: ";
                    break;
                default:
                    orders = orderService.getTodayOrders();
                    prefix = "Tổng danh thu hôm nay: ";
            }
            updateOrderTable(orders, prefix); // Chỉ truyền orders và prefix
        };

        updateTableByType.accept("today"); // Mặc định hôm nay

        btnToday.addActionListener(e -> updateTableByType.accept("today"));
        btnMonth.addActionListener(e -> updateTableByType.accept("month"));
        btnYear.addActionListener(e -> updateTableByType.accept("year"));

        JDialog dialog = new JDialog(this, "Chi tiết đơn hàng", true);
        dialog.getContentPane().add(panel);
        dialog.setSize(800, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Sửa đổi phương thức để cập nhật contentPanel
    private void updateOrderTable(List<Order> orders, String prefix) {
        // Xóa tất cả các thành phần cũ khỏi contentPanel
        if (contentPanel != null) {
            contentPanel.removeAll();
        } else {
            System.err.println("contentPanel is null in updateOrderTable!");
            return; // Thoát nếu panel chưa sẵn sàng
        }


        String[] columnNames = {"Mã đơn", "Thời gian", "Tổng tiền", "Ghi chú"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        DecimalFormat currencyFormat = new DecimalFormat("#,###");

        // Tạo đối tượng SimpleDateFormat với định dạng mong muốn
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        BigDecimal total = BigDecimal.ZERO;

        for (Order o : orders) {
            // Định dạng Timestamp trước khi thêm vào model
            String formattedTime = timeFormat.format(o.getOrderDatetime());

            model.addRow(new Object[]{
                    o.getOrderId(),
                    formattedTime, // Sử dụng chuỗi đã định dạng
                    currencyFormat.format(o.getTotalAmount()) + " VND",
                    o.getNotes()
            });
            total = total.add(o.getTotalAmount());
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JLabel lblTotal = new JLabel(prefix + currencyFormat.format(total) + " VND");
        lblTotal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Thêm các thành phần mới vào contentPanel
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(lblTotal, BorderLayout.SOUTH);

        // Cập nhật UI
        contentPanel.revalidate();
        contentPanel.repaint();
    }





}
