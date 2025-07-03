package com.example.qlnv.repository;

import com.example.qlnv.entity.ChamCongNgay;
import com.example.qlnv.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChamCongNgayRepository extends JpaRepository<ChamCongNgay, Long> {
    Optional<ChamCongNgay> findByNhanVienAndNgay(NhanVien nhanVien, LocalDate ngay);
    boolean existsByNhanVien_MaNv(String maNv);
    List<ChamCongNgay> findByNhanVienMaNvAndNgayBetween(String maNv, LocalDate start, LocalDate end);
}
