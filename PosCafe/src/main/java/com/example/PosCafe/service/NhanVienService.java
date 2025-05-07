package com.example.PosCafe.service;


import com.example.PosCafe.model.NhanVien;
import com.example.PosCafe.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NhanVienService {
    @Autowired
    private NhanVienRepository repo;

    public NhanVien login(String username, String password) {
        return repo.findByUsernameAndPassword(username, password);
    }

}