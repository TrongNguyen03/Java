package org.example.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String role;

    // Getters & setters
}
