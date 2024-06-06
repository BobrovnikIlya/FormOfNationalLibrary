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

    public List<Publish> getAllPublishes() {
        return publishRepository.findAll();
    }
    public List<Publish> findPublishesByNameContaining(String term) {
        return publishRepository.findByNameContaining(term);
    }

    public Publish findPublishByName(String name) {
        return publishRepository.findByName(name);
    }

    public void savePublish(Publish publisher) {
        publishRepository.save(publisher);
    }
}