package com.example.PosCafe.service;

import com.example.PosCafe.model.Product;
import com.example.PosCafe.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired private ProductRepository repo;

    public List<Product> getAllAvailable() {
        return repo.findByIsAvailableTrue();
    }

    public List<Product> search(String kw) {
        return repo.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndIsAvailableTrue(kw, kw);
    }

    public Product getById(int id) {
        return repo.findById(id).orElse(null);
    }

}
