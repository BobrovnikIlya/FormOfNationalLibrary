package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.Publish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublishService {
    @Autowired
    private PublishRepository publishRepository;

    public List<Publish> getAllPublishes() {
        return publishRepository.findAll();
    }
}