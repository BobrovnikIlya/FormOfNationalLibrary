package com.example.formofnationallibrary.Entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cities")
public class Cities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_city")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "cities")
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
}
