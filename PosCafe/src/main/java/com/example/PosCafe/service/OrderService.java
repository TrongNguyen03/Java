package com.example.PosCafe.service;

import com.example.PosCafe.model.Order;
import com.example.PosCafe.model.Payment;
import com.example.PosCafe.repository.OrderRepository;
import com.example.PosCafe.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired private OrderRepository orderRepo;
    @Autowired private PaymentRepository payRepo;

    public Order saveOrder(Order o) {
        return orderRepo.save(o);
    }

    public void savePayment(Payment p) {
        payRepo.save(p);
    }

    public List<Order> getToday() {
        LocalDate d = LocalDate.now();
        Timestamp start = Timestamp.valueOf(d.atStartOfDay());
        Timestamp end   = Timestamp.valueOf(LocalDateTime.of(d, LocalTime.MAX));
        return orderRepo.findByOrderDatetimeBetween(start, end);
    }

}
