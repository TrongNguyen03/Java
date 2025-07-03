package com.example.qlnv.security;


import com.example.qlnv.entity.TaiKhoan;
import com.example.qlnv.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TaiKhoanRepository taiKhoanRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TaiKhoan tk = taiKhoanRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));

        return new User(
                tk.getUsername(),
                tk.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + tk.getRole().name()))
        );
    }
}
