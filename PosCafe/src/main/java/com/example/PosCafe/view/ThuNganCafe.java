package com.example.PosCafe.view;

import com.example.PosCafe.model.Order;
import com.example.PosCafe.model.OrderItem;
import com.example.PosCafe.model.Payment;
import com.example.PosCafe.model.Product;
import com.example.PosCafe.service.OrderService;
import com.example.PosCafe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ThuNganCafe extends JFrame {
    private final ProductService ps;
    private final OrderService os;

    private JPanel pnl;
    private DefaultListModel<String> lm;
    private List<OrderItem> items = new ArrayList<>();
    private JLabel lblT;

    @Autowired
    public ThuNganCafe(ProductService ps, OrderService os) {
        this.ps = ps;
        this.os = os;

        setTitle("Thu Ngan Cafe");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        pnl = new JPanel(new FlowLayout());
        add(new JScrollPane(pnl), BorderLayout.CENTER);

        lm = new DefaultListModel<>();
        JList<String> lst = new JList<>(lm);
        add(new JScrollPane(lst), BorderLayout.EAST);

        JButton btn = new JButton("Thanh Toan");
        btn.addActionListener(e -> pay());

        lblT = new JLabel("Tong: 0 VND");
        JPanel bot = new JPanel();
        bot.add(btn);
        bot.add(lblT);
        add(bot, BorderLayout.SOUTH);
    }

    @PostConstruct
    private void postConstruct() {
        loadProducts();
    }

    private void loadProducts() {
        pnl.removeAll();
        ps.getAllAvailable().forEach(p -> {
            JButton b = new JButton(p.getName());
            b.addActionListener(e -> addItem(p));
            pnl.add(b);
        });
        pnl.revalidate();
        pnl.repaint();
    }

    private void addItem(Product p) {
        OrderItem it = new OrderItem();
        it.setProduct(p);
        it.setQuantity(1);
        it.setUnitPrice(p.getPrice());
        it.setSubtotal(p.getPrice());
        items.add(it);
        lm.addElement(p.getName() + " - " + p.getPrice());
        updateTotal();
    }

    private void updateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.getSubtotal());
        }
        lblT.setText("Tong: " + total + " VND");
    }

    private void pay() {
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chua co san pham");
            return;
        }

        BigDecimal total = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderDatetime(new Timestamp(System.currentTimeMillis()));
        order.setTotalAmount(total);
        order.setStatus("Completed");
        order.setNotes("POS Swing");
        items.forEach(i -> i.setOrder(order));

        Order savedOrder = os.saveOrder(order);

        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmountPaid(total);
        payment.setPaymentMethod("Tiền mặt");
        payment.setPaymentDatetime(new Timestamp(System.currentTimeMillis()));
        os.savePayment(payment);

        JOptionPane.showMessageDialog(this, "Thanh toan thanh cong!");
        items.clear();
        lm.clear();
        updateTotal();
    }
}