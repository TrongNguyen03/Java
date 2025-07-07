package com.example.qlnv.service.impl;

import com.example.qlnv.dto.AuthRequest;
import com.example.qlnv.entity.NhanVien;
import com.example.qlnv.entity.TaiKhoan;
import com.example.qlnv.entity.enumeration.Role;
import com.example.qlnv.repository.NhanVienRepository;
import com.example.qlnv.repository.TaiKhoanRepository;
import com.example.qlnv.service.TaiKhoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaiKhoanServiceImpl implements TaiKhoanService {

    private final TaiKhoanRepository taiKhoanRepo;
    private final NhanVienRepository nhanVienRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createNhanVienAccount(AuthRequest request) {
        if (taiKhoanRepo.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }

        NhanVien nv = nhanVienRepo.findById(request.getMaNv())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        TaiKhoan tk = new TaiKhoan();
        tk.setUsername(request.getUsername());
        tk.setPassword(passwordEncoder.encode(request.getPassword()));
        tk.setRole(Role.NHANVIEN);
        tk.setNhanVien(nv);

        taiKhoanRepo.save(tk);
    }

    @Override
    public TaiKhoan findByUsername(String username) {
        return taiKhoanRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
    }
}
