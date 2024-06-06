package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Author;
import com.example.formofnationallibrary.Repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    public List<Author> findAuthorsByNameContaining(String name) {
        return authorRepository.findByNameContaining(name);
    }
    public Author findAuthorByName(String name) {
        return authorRepository.findByName(name);
    }

    public void saveAuthor(Author author) {
        authorRepository.save(author);
    }
}
