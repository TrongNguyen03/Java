package com.example.qlnv.repository;


import com.example.qlnv.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {
    @Query("SELECT nv FROM NhanVien nv WHERE nv.maNv NOT IN (SELECT tk.nhanVien.maNv FROM TaiKhoan tk)")
    List<NhanVien> findNhanVienChuaCoTaiKhoan();
}

