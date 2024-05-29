package com.example.formofnationallibrary.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "status_copies")
public class StatusCopies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_status_copies;
    private String status;

    // getters and setters
    public Long getIdStatusCopies() {
        return id_status_copies;
    }

    public void setIdStatusCopies(Long idStatusCopies) {
        this.id_status_copies = idStatusCopies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
