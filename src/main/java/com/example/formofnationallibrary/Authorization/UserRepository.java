package com.example.formofnationallibrary.Authorization;

import com.example.formofnationallibrary.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    User findByLogin(String login);
}
