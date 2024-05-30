package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    // Дополнительные методы, если необходимо
}
