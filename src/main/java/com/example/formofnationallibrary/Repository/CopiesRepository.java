package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Copies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CopiesRepository extends JpaRepository<Copies, Long> {
    List<Copies> findByBookId(Long bookId);
}

