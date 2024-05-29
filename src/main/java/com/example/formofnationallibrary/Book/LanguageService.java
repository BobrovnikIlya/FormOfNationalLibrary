package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.Author;
import com.example.formofnationallibrary.Entities.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    @Autowired
    private LanguageRepository languageRepository;
    public void saveLanguage(Language language) {
        languageRepository.save(language);
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }
}
