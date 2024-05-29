package com.example.formofnationallibrary.Entities;

import com.example.formofnationallibrary.Entities.Book;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_language")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "language")
    private List<Book> books;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    // Getters and Setters
}

