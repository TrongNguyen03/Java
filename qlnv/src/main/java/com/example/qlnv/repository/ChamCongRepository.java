package com.example.qlnv.repository;

import com.example.qlnv.entity.ChamCong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChamCongRepository extends JpaRepository<ChamCong, Long> {
    List<ChamCong> findByNhanVien_MaNv(String maNv);
    List<ChamCong> findByThangAndNam(int thang, int nam);
    Optional<ChamCong> findByNhanVien_MaNvAndThangAndNam(String maNv, int thang, int nam);
    boolean existsByNhanVien_MaNv(String maNv);

}

