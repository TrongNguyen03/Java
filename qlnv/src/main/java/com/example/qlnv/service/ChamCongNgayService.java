package com.example.qlnv.service;

import com.example.qlnv.entity.ChamCongNgay;
import com.example.qlnv.entity.NhanVien;
import com.example.qlnv.repository.ChamCongNgayRepository;
import com.example.qlnv.repository.NhanVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChamCongNgayService {

    private final ChamCongNgayRepository chamCongNgayRepo;
    private final NhanVienRepository nhanVienRepo;

    public void checkIn(String maNv, String ghiChu) {
        NhanVien nv = nhanVienRepo.findById(maNv)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        LocalDate today = LocalDate.now();
        Optional<ChamCongNgay> optional = chamCongNgayRepo.findByNhanVienAndNgay(nv, today);

        if (optional.isPresent()) {
            throw new IllegalStateException("Đã check-in hôm nay");
        }

        ChamCongNgay cc = new ChamCongNgay();
        cc.setNhanVien(nv);
        cc.setNgay(today);
        cc.setGioCheckIn(LocalTime.now());
        cc.setGhiChu(ghiChu);  // 💡 Lưu ghi chú nếu có
        chamCongNgayRepo.save(cc);
    }

    public void checkOut(String maNv, String ghiChu) {
        NhanVien nv = nhanVienRepo.findById(maNv)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        LocalDate today = LocalDate.now();
        ChamCongNgay cc = chamCongNgayRepo.findByNhanVienAndNgay(nv, today)
                .orElseThrow(() -> new IllegalStateException("Chưa check-in hôm nay"));

        if (cc.getGioCheckOut() != null) {
            throw new IllegalStateException("Đã check-out hôm nay");
        }

        cc.setGioCheckOut(LocalTime.now());

        // 💡 Nếu có ghi chú lúc check-out thì gộp vào hoặc cập nhật
        if (ghiChu != null && !ghiChu.isBlank()) {
            String currentNote = cc.getGhiChu() == null ? "" : cc.getGhiChu() + " | ";
            cc.setGhiChu(currentNote + ghiChu);
        }

        chamCongNgayRepo.save(cc);
    }

}
