package com.example.qlnv.service;

import com.example.qlnv.entity.NhanVien;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NhanVienService {
    List<NhanVien> getAll();
    boolean existsById(String maNV);
    NhanVien getById(String maNV);
    void addOrUpdate(NhanVien nv);
    void deleteById(String maNV);
    void importFromExcel(MultipartFile file);
    byte[] exportToExcel();
}
