package com.example.PosCafe.repository;

import com.example.PosCafe.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List findByIsAvailableTrue();
    List findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndIsAvailableTrue(String name, String desc);
}