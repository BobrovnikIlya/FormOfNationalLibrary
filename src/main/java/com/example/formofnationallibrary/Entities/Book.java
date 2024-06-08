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
    @Column(name = "views")
    private int views;
    @Column(name = "number_orders")
    private int number_orders;
    @Column(name = "number_favorite")
    private int number_favorite;
    @ManyToOne
    @JoinColumn(name = "id_publish", nullable = false)
    private Publish publish;
    @ManyToOne
    @JoinColumn(name = "id_city", nullable = false)
    private Cities cities;
    @Column(name = "description")
    private String description;
    @Column(name = "category")
    private String category;
    @ManyToOne
    @JoinColumn(name = "id_language", nullable = false)
    private Language language;

    public int getNumber_orders() {
        return number_orders;
    }

    public void setNumber_orders(int number_orders) {
        this.number_orders = number_orders;
    }

    public int getNumber_favorite() {
        return number_favorite;
    }

    public void setNumber_favorite(int number_favorite) {
        this.number_favorite = number_favorite;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Cities getCities() {
        return cities;
    }

    public void setCities(Cities cities) {
        this.cities = cities;
    }
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
