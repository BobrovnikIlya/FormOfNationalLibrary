package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Book;
import com.example.formofnationallibrary.Entities.Copies;
import com.example.formofnationallibrary.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void updateBook(Book updatedBook, Long bookId) {
        Book existingBook = bookRepository.findById(bookId).orElse(null);
        if (existingBook != null) {
            existingBook.setName(updatedBook.getName());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setYear(updatedBook.getYear());
            existingBook.setPages(updatedBook.getPages());
            existingBook.setLanguage(updatedBook.getLanguage());
            existingBook.setPublish(updatedBook.getPublish());
            existingBook.setDescription(updatedBook.getDescription());
            bookRepository.save(existingBook);
        } else {
            throw new IllegalArgumentException("Книга с идентификатором " + bookId + " не найдена.");
        }
    }
    public void deleteBookById(Long bookId) {
        bookRepository.deleteById(bookId);
    }
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }
        public void saveBook(Book book) {
        bookRepository.save(book);
    }
}
