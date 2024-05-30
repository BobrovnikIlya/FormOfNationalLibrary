package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Copies;
import com.example.formofnationallibrary.Repository.CopiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CopiesService {
    private final CopiesRepository copiesRepository;

    @Autowired
    public CopiesService(CopiesRepository copiesRepository) {
        this.copiesRepository = copiesRepository;
    }

    public void saveCopy(Copies copies) {
        copiesRepository.save(copies);
    }
}

