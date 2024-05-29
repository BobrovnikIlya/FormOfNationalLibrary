package com.example.formofnationallibrary.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_author", nullable = false)
    private Author author;

    @Column(name = "year")
    private int year;

    @Column(name = "name")
    private String name;

    @Column(name = "pages")
    private int pages;

    @ManyToOne
    @JoinColumn(name = "id_publish", nullable = false)
    private Publish publish;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_language", nullable = false)
    private Language language;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Publish getPublish() {
        return publish;
    }

    public void setPublish(Publish publish) {
        this.publish = publish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    // Getters and Setters
}
