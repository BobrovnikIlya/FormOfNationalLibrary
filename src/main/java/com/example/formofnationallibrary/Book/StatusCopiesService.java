package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.Language;
import com.example.formofnationallibrary.Entities.StatusCopies;
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
}