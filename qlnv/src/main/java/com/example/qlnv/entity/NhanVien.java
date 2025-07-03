package com.example.qlnv.entity;


import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "NhanVien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhanVien {
    @Id
    @Column(name = "ma_nv")
    private String maNv;

    @Column(name = "ten")
    private String ten;

    @Column(name = "tuoi")
    private int tuoi;

    @Column(name = "email")
    private String email;

    @Column(name = "luong")
    private double luong;
}
