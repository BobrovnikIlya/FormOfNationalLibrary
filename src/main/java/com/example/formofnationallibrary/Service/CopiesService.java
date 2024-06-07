package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Copies;
import com.example.formofnationallibrary.Repository.CopiesRepository;
import com.example.formofnationallibrary.Repository.OrderHistoryRepository;
import com.example.formofnationallibrary.Repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CopiesService {
    @Autowired
    private CopiesRepository copiesRepository;

    @Autowired
    public CopiesService(CopiesRepository copiesRepository) {
        this.copiesRepository = copiesRepository;
    }

    public void saveCopy(Copies copies) {
        copiesRepository.save(copies);
    }
}

