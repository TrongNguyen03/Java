package com.example.TestApp.service;

import com.example.TestApp.model.User;
import com.example.TestApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public User login(String username, String password) {
        return userRepo.findByUsernameAndPassword(username, password).orElse(null);
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public User getById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}
