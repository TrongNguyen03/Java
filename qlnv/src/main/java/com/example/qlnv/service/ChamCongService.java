package com.example.qlnv.service;

import com.example.qlnv.entity.ChamCong;
import com.example.qlnv.entity.ChamCongNgay;
import com.example.qlnv.entity.NhanVien;
import com.example.qlnv.repository.ChamCongNgayRepository;
import com.example.qlnv.repository.ChamCongRepository;
import com.example.qlnv.repository.NhanVienRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChamCongService {
    private final ChamCongRepository chamCongRepo;
    private final ChamCongNgayRepository chamCongNgayRepo;
    private final NhanVienRepository nhanVienRepo;

    // Tính lương theo công thức: lương = lương cứng * (số ngày công / 26)
    private double tinhLuongThucNhan(NhanVien nv, int soNgayCong) {
        return Math.round(nv.getLuong() * soNgayCong / 26.0 * 100.0) / 100.0;
    }

    public List<ChamCong> getAll() {
        return chamCongRepo.findAll();
    }

    public List<ChamCong> getByNhanVien(String maNv) {
        return chamCongRepo.findByNhanVien_MaNv(maNv);
    }

    public void addOrUpdate(ChamCong chamCong, String maNv) {
        NhanVien nv = nhanVienRepo.findById(maNv)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        chamCong.setNhanVien(nv);
        chamCong.setLuongThucNhan(tinhLuongThucNhan(nv, chamCong.getSoNgayCong()));
        chamCongRepo.save(chamCong);
    }

    public void deleteById(Long id) {
        chamCongRepo.deleteById(id);
    }

    public ResponseEntity<Resource> exportChamCongToExcel(int thang, int nam) {
        List<ChamCong> list = chamCongRepo.findByThangAndNam(thang, nam);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("ChamCong");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Mã NV");
            header.createCell(1).setCellValue("Tên nhân viên");
            header.createCell(2).setCellValue("Tháng");
            header.createCell(3).setCellValue("Năm");
            header.createCell(4).setCellValue("Số ngày công");
            header.createCell(5).setCellValue("Số ngày nghỉ");
            header.createCell(6).setCellValue("Lương thực nhận (VND)");
            header.createCell(7).setCellValue("Ghi chú");

            int rowNum = 1;
            for (ChamCong cc : list) {
                Row row = sheet.createRow(rowNum++);
                NhanVien nv = cc.getNhanVien();
                row.createCell(0).setCellValue(nv != null ? nv.getMaNv() : "");
                row.createCell(1).setCellValue(nv != null ? nv.getTen() : "");
                row.createCell(2).setCellValue(cc.getThang());
                row.createCell(3).setCellValue(cc.getNam());
                row.createCell(4).setCellValue(cc.getSoNgayCong());
                row.createCell(5).setCellValue(cc.getSoNgayNghi());

                double luong = cc.getLuongThucNhan() != null ? cc.getLuongThucNhan()
                        : (nv != null ? tinhLuongThucNhan(nv, cc.getSoNgayCong()) : 0);
                row.createCell(6).setCellValue(luong);
                row.createCell(7).setCellValue(cc.getGhiChu() != null ? cc.getGhiChu() : "");
            }

            for (int i = 0; i <= 7; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=chamcong_" + thang + "_" + nam + ".xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xuất Excel: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void tongHopChamCongThang(int thang, int nam) {
        List<NhanVien> danhSachNv = nhanVienRepo.findAll();

        for (NhanVien nv : danhSachNv) {
            LocalDate start = LocalDate.of(nam, thang, 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

            List<ChamCongNgay> congNgayList = chamCongNgayRepo
                    .findByNhanVienMaNvAndNgayBetween(nv.getMaNv(), start, end);

            int soNgayCong = (int) congNgayList.stream().filter(cc -> cc.getGioCheckIn() != null).count();
            int soNgayNghi = 26 - soNgayCong;

            ChamCong chamCong = chamCongRepo
                    .findByNhanVien_MaNvAndThangAndNam(nv.getMaNv(), thang, nam)
                    .orElse(new ChamCong());

            chamCong.setNhanVien(nv);
            chamCong.setThang(thang);
            chamCong.setNam(nam);
            chamCong.setSoNgayCong(soNgayCong);
            chamCong.setSoNgayNghi(soNgayNghi);
            chamCong.setLuongThucNhan(tinhLuongThucNhan(nv, soNgayCong));

            chamCongRepo.save(chamCong);
        }
    }
}
