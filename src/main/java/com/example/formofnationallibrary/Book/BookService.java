package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }
}