package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findByNameContaining(String term);
    Language findByName(String name);
    // Дополнительные методы, если необходимо
}