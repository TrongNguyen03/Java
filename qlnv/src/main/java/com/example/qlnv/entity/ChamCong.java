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
    private double soNgayCong;
    private int soNgayNghi;

    private Double luongThucNhan;

    private String ghiChu;

}
