package com.example.qlnv.service;

import com.example.qlnv.dto.AuthRequest;
import com.example.qlnv.entity.TaiKhoan;

public interface TaiKhoanService {
    void createNhanVienAccount(AuthRequest request);
    TaiKhoan findByUsername(String username);
}
