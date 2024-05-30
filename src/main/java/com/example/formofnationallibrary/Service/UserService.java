package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Role;
import com.example.formofnationallibrary.Entities.User;
import com.example.formofnationallibrary.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FavoriteRepository favoritesRepository;
    private final OrderRepository ordersRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final QueueRepository queueRepository;

    public UserService(UserRepository userRepository,
                       FavoriteRepository favoritesRepository,
                       OrderRepository ordersRepository,
                       OrderHistoryRepository orderHistoryRepository,
                       QueueRepository queueRepository) {
        this.userRepository = userRepository;
        this.favoritesRepository = favoritesRepository;
        this.ordersRepository = ordersRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.queueRepository = queueRepository;
    }

    public User registerUser(User user) {
        // Encrypt the password
        user.setPassword(encryptPassword(user.getPassword()));
        // Save user
        return userRepository.save(user);
    }
    @Transactional
    public void deleteUserAndRelatedRecords(Long userId) {
        favoritesRepository.deleteByUserId(userId);
        ordersRepository.deleteByUserId(userId);
        orderHistoryRepository.deleteByUserId(userId);
        queueRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
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
    public List<User> getAllUsers() {
        return userRepository.findAll();
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


