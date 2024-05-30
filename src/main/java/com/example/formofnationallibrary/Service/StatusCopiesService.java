package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.StatusCopies;
import com.example.formofnationallibrary.Repository.StatusCopiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StatusCopiesService {
    @Autowired
    private StatusCopiesRepository statusCopiesRepository;
    public List<StatusCopies> getAllStatusCopies() {
        return statusCopiesRepository.findAll();
    }
    public StatusCopies findByStatus(String status) {
        return statusCopiesRepository.findByStatus(status);
    }
}