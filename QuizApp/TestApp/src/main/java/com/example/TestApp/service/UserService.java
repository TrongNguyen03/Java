package com.example.TestApp.service;

import com.example.TestApp.model.User;

import java.util.List;


public interface UserService {

    User login(String username, String password);

    User save(User user);

    List<User> getAll();

    User getById(Long id);

    void delete(Long id);
}
