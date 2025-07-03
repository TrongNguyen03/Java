package com.example.qlnv.controller;

import com.example.qlnv.dto.AuthRequest;
import com.example.qlnv.dto.AuthResponse;
import com.example.qlnv.entity.TaiKhoan;
import com.example.qlnv.security.JWTUtil;
import com.example.qlnv.service.TaiKhoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JWTUtil jwtUtil;
    private final TaiKhoanService taiKhoanService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            TaiKhoan tk = taiKhoanService.findByUsername(request.getUsername());

            String token = jwtUtil.generateToken(tk.getUsername(), tk.getRole().name());
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tài khoản hoặc mật khẩu");
        }
    }

    @PostMapping("/create-account")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAccount(@RequestBody AuthRequest request) {
        taiKhoanService.createNhanVienAccount(request);
        return ResponseEntity.ok("Tạo tài khoản thành công");
    }
}

