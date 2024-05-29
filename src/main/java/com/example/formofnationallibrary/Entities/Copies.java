package com.example.formofnationallibrary.Entities;

import jakarta.persistence.*;

@Entity
public class Copies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCopies;
    private String cipher;

    @ManyToOne
    @JoinColumn(name = "id_book")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "id_location")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "id_statuscopies")
    private StatusCopies statusCopies;

    // getters and setters
    public Long getIdCopies() {
        return idCopies;
    }

    public void setIdCopies(Long idCopies) {
        this.idCopies = idCopies;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public StatusCopies getStatusCopies() {
        return statusCopies;
    }

    public void setStatusCopies(StatusCopies statusCopies) {
        this.statusCopies = statusCopies;
    }
}
