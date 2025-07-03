package com.example.qlnv.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChamCong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_nv", referencedColumnName = "ma_nv", nullable = false)
    private NhanVien nhanVien;

    private int thang;
    private int nam;
    private int soNgayCong;
    private int soNgayNghi;

    private Double luongThucNhan;

    private String ghiChu;

    @Transient
    public double getLuongTinhToan() {
        // Trường hợp không lưu lương trong DB hàm này để hiển thị
        return Math.round(nhanVien.getLuong() * soNgayCong / 26.0 * 100.0) / 100.0;
    }
}
