package com.example.qlnv.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChamCongNgay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_nv", referencedColumnName = "ma_nv", nullable = false)
    private NhanVien nhanVien;

    private LocalDate ngay;

    private LocalTime gioCheckIn;

    private LocalTime gioCheckOut;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @PrePersist
    public void prePersist() {
        if (ngay == null) {
            ngay = LocalDate.now();
        }
    }
}

