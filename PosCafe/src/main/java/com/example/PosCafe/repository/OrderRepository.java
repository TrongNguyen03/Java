package com.example.PosCafe.repository;

import com.example.PosCafe.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List findByOrderDatetimeBetween(Timestamp start, Timestamp end);
}
