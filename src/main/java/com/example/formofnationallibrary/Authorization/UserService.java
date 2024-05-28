package com.example.formofnationallibrary.Authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // Encrypt the password
        user.setPassword(encryptPassword(user.getPassword()));
        // Save user
        return userRepository.save(user);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public boolean authenticate(String login, String password) {
        User user = findByLogin(login);
        if (user != null) {
            return checkPassword(user, password);
        }
        return false;
    }
}


