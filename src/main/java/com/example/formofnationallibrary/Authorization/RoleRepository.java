package com.example.formofnationallibrary.Authorization;

import com.example.formofnationallibrary.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
