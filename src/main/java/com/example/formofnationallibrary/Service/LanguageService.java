package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Language;
import com.example.formofnationallibrary.Repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    @Autowired
    private LanguageRepository languageRepository;
    public List<Language> findLanguagesByNameContaining(String term) {
        return languageRepository.findByNameContaining(term);
    }
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    public Language findLanguageByName(String name) {
        return languageRepository.findByName(name);
    }

    public void saveLanguage(Language language) {
        languageRepository.save(language);
    }
}
