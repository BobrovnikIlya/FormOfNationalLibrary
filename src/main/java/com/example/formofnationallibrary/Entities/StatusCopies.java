package com.example.formofnationallibrary.Entities;

import jakarta.persistence.*;

@Entity
public class StatusCopies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStatusCopies;
    private String status;

    // getters and setters
    public Long getIdStatusCopies() {
        return idStatusCopies;
    }

    public void setIdStatusCopies(Long idStatusCopies) {
        this.idStatusCopies = idStatusCopies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
