package com.example.qlnv.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String maNv;
}