package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNameContaining(String name);
    Author findByName(String name);
    // Дополнительные методы, если необходимо
}
