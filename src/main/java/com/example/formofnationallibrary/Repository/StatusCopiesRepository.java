package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.StatusCopies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusCopiesRepository extends JpaRepository<StatusCopies, Long> {
    StatusCopies findByStatus(String status);
    // Дополнительные методы, если необходимо
}
