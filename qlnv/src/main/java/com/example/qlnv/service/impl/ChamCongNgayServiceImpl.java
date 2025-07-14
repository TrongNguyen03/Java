package com.example.qlnv.service.impl;

import com.example.qlnv.entity.ChamCongNgay;
import com.example.qlnv.entity.NhanVien;
import com.example.qlnv.repository.ChamCongNgayRepository;
import com.example.qlnv.repository.NhanVienRepository;
import com.example.qlnv.service.ChamCongNgayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChamCongNgayServiceImpl implements ChamCongNgayService {

    private final ChamCongNgayRepository chamCongNgayRepo;
    private final NhanVienRepository nhanVienRepo;

    @Override
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
        cc.setGhiChu(ghiChu);
        chamCongNgayRepo.save(cc);
    }

    @Override
    public void checkOut(String maNv, String ghiChu) {
        NhanVien nv = nhanVienRepo.findById(maNv)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        LocalDate today = LocalDate.now();
        ChamCongNgay cc = chamCongNgayRepo.findByNhanVienAndNgay(nv, today)
                .orElseThrow(() -> new IllegalStateException("Chưa check-in hôm nay"));

        if (cc.getGioCheckOut() != null) {
            throw new IllegalStateException("Đã check-out hôm nay");
        }

        LocalTime gioRa = LocalTime.now();
        cc.setGioCheckOut(gioRa);

        // Tính số giờ làm việc
        long gioLam = java.time.Duration.between(cc.getGioCheckIn(), gioRa).toHours();
        double soCong;

        if (gioLam >= 8) {
            soCong = 1.0;
        } else if (gioLam >= 4) {
            soCong = 0.5;
        } else {
            soCong = 0.0;
        }

        cc.setSoCong(soCong);

        // Ghi chú bổ sung
        if (ghiChu != null && !ghiChu.isBlank()) {
            String currentNote = cc.getGhiChu() == null ? "" : cc.getGhiChu() + " | ";
            cc.setGhiChu(currentNote + ghiChu);
        }

        chamCongNgayRepo.save(cc);
    }

}
