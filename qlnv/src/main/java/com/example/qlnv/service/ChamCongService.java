package com.example.qlnv.service;

import com.example.qlnv.entity.ChamCong;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChamCongService {
    List<ChamCong> getAll();
    List<ChamCong> getByNhanVien(String maNv);
    void addOrUpdate(ChamCong chamCong, String maNv);
    void deleteById(Long id);
    ResponseEntity<Resource> exportChamCongToExcel(int thang, int nam);
    void tongHopChamCongThang(int thang, int nam);
}
