package com.example.qlnv.service;

import com.example.qlnv.entity.NhanVien;
import com.example.qlnv.repository.NhanVienRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.qlnv.repository.ChamCongRepository;
import com.example.qlnv.repository.ChamCongNgayRepository;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NhanVienService {
    private final NhanVienRepository repository;
    private final ChamCongRepository chamCongRepository;
    private final ChamCongNgayRepository chamCongNgayRepository;


    public List<NhanVien> getAll() {
        return repository.findAll();
    }

    public boolean existsById(String maNV) {
        return repository.existsById(maNV);
    }

    public NhanVien getById(String maNV) {
        return repository.findById(maNV).orElse(null);
    }

    public void addOrUpdate(NhanVien nv) {
        repository.save(nv);
    }

    public void deleteById(String maNV) {
        boolean existsInChamCongThang = chamCongRepository.existsByNhanVien_MaNv(maNV);
        boolean existsInChamCongNgay = chamCongNgayRepository.existsByNhanVien_MaNv(maNV);

        if (existsInChamCongThang || existsInChamCongNgay) {
            throw new RuntimeException("❌ Không thể xóa nhân viên đã có dữ liệu chấm công.");
        }

        repository.deleteById(maNV);
    }


    public void importFromExcel(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String ma = row.getCell(0).getStringCellValue();
                String ten = row.getCell(1).getStringCellValue();
                int tuoi = (int) row.getCell(2).getNumericCellValue();
                String email = row.getCell(3).getStringCellValue();
                double luong = row.getCell(4).getNumericCellValue();

                NhanVien nv = new NhanVien(ma, ten, tuoi, email, luong);
                repository.save(nv);
            }

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi import Excel", e);
        }
    }


    public byte[] exportToExcel() {
        List<NhanVien> list = repository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("NhanVien");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Mã NV");
            header.createCell(1).setCellValue("Họ Tên");
            header.createCell(2).setCellValue("Tuổi");
            header.createCell(3).setCellValue("Email");
            header.createCell(4).setCellValue("Lương");

            int rowNum = 1;
            for (NhanVien nv : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(nv.getMaNv());
                row.createCell(1).setCellValue(nv.getTen());
                row.createCell(2).setCellValue(nv.getTuoi());
                row.createCell(3).setCellValue(nv.getEmail());
                row.createCell(4).setCellValue(nv.getLuong());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi export Excel", e);
        }
    }

}
