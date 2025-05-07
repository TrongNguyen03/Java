package com.example.PosCafe.repository;

import com.example.PosCafe.model.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    NhanVien findByUsernameAndPassword(String username, String password);
}