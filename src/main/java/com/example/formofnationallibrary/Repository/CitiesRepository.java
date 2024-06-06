package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitiesRepository extends JpaRepository<Cities, Long> {
    List<Cities> findByNameContaining(String term);
    Cities findByName(String name);
    // Другие методы, если необходимо
}