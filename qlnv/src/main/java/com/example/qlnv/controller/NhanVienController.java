package com.example.qlnv.controller;

import com.example.qlnv.entity.NhanVien;
import com.example.qlnv.repository.NhanVienRepository;
import com.example.qlnv.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/nhanvien")
@RequiredArgsConstructor
public class NhanVienController {

    private final NhanVienService service;
    private final NhanVienService nhanVienService;
    private final NhanVienRepository nhanVienRepository;

    @GetMapping
    public List<NhanVien> getAll() {
        return service.getAll();
    }

    @GetMapping("/{maNv}")
    public ResponseEntity<NhanVien> getById(@PathVariable String maNv) {
        NhanVien nv = service.getById(maNv);
        return (nv != null) ? ResponseEntity.ok(nv) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public void addOrUpdate(@RequestBody NhanVien nv) {
        service.addOrUpdate(nv);
    }

    @DeleteMapping("/{maNv}")
    public ResponseEntity<?> deleteNhanVien(@PathVariable String maNv) {
        try {
            nhanVienService.deleteById(maNv);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Không thể xóa vì nhân viên đang có dữ liệu liên quan (chấm công hoặc tài khoản)");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @GetMapping("/chua-co-tai-khoan")
    public List<NhanVien> getNhanVienChuaCoTaiKhoan() {
        return nhanVienRepository.findNhanVienChuaCoTaiKhoan();
    }

    @PostMapping("/import-excel")
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file) {
        service.importFromExcel(file);
        return ResponseEntity.ok("Import Excel thành công");
    }

    @GetMapping("/export-excel")
    public ResponseEntity<Resource> exportExcel() {
        byte[] excelData = service.exportToExcel(); // Trả về byte[]
        ByteArrayResource resource = new ByteArrayResource(excelData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=NhanVienExport.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(excelData.length)
                .body(resource);
    }
}
