package com.example.qlnv.controller;

import com.example.qlnv.dto.GhiChuRequest;
import com.example.qlnv.repository.TaiKhoanRepository;
import com.example.qlnv.service.ChamCongNgayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chamcong-ngay")
@RequiredArgsConstructor
public class ChamCongNgayController {

    private final ChamCongNgayService chamCongNgayService;
    private final TaiKhoanRepository taiKhoanRepo;

    @PostMapping("/checkin")
    @PreAuthorize("hasRole('NHANVIEN')")
    public ResponseEntity<?> checkIn(@RequestBody GhiChuRequest req, Authentication authentication) {
        String username = authentication.getName();
        String maNv = taiKhoanRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"))
                .getNhanVien().getMaNv();
        try {
            chamCongNgayService.checkIn(maNv, req.getGhiChu());
            return ResponseEntity.ok("Check-in thành công");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Đã check-in hôm nay");
        }
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('NHANVIEN')")
    public ResponseEntity<?> checkOut(@RequestBody GhiChuRequest req, Authentication authentication) {
        String username = authentication.getName();
        String maNv = taiKhoanRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"))
                .getNhanVien().getMaNv();
        try {
            chamCongNgayService.checkOut(maNv, req.getGhiChu());
            return ResponseEntity.ok("Check-out thành công");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Đã check-out hôm nay hoặc chưa check-in");
        }
    }
}
