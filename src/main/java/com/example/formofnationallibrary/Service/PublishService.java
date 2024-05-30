package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Publish;
import com.example.formofnationallibrary.Repository.PublishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublishService {
    @Autowired
    private PublishRepository publishRepository;

    public void savePublish(Publish publish) {
        publishRepository.save(publish);
    }

    public List<Publish> getAllPublishes() {
        return publishRepository.findAll();
    }
}