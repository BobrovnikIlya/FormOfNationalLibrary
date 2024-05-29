package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    // Дополнительные методы, если необходимо
}