package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.Book;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

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


    @Transactional
    public void deleteBookById(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }
}
