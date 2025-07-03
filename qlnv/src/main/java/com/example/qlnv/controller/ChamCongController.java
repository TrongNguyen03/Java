package com.example.qlnv.controller;

import com.example.qlnv.entity.ChamCong;
import com.example.qlnv.service.ChamCongService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin // Giúp tránh lỗi 403 khi gọi từ giao diện web
@RestController
@RequestMapping("/api/chamcong")
@RequiredArgsConstructor
public class ChamCongController {

    private final ChamCongService chamCongService;

    @GetMapping
    public List<ChamCong> getAll() {
        return chamCongService.getAll();
    }

    @GetMapping("/nhanvien/{maNv}")
    public List<ChamCong> getByNhanVien(@PathVariable String maNv) {
        return chamCongService.getByNhanVien(maNv);
    }

    @PostMapping("/{maNv}")
    public ResponseEntity<String> addChamCong(@PathVariable String maNv, @RequestBody ChamCong chamCong) {
        chamCongService.addOrUpdate(chamCong, maNv);
        return ResponseEntity.ok("Lưu chấm công thành công");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChamCong(@PathVariable Long id) {
        chamCongService.deleteById(id);
        return ResponseEntity.ok("Xóa thành công");
    }

    @GetMapping("/export-excel")
    public ResponseEntity<Resource> exportChamCong(@RequestParam int thang, @RequestParam int nam) {
        return chamCongService.exportChamCongToExcel(thang, nam);
    }

    // Gọi để tổng hợp lại chấm công tháng từ chấm công ngày
    @PostMapping("/tong-hop")
    public ResponseEntity<String> tongHopChamCong(@RequestParam int thang, @RequestParam int nam) {
        chamCongService.tongHopChamCongThang(thang, nam);
        return ResponseEntity.ok("Tổng hợp chấm công tháng thành công");
    }
}
