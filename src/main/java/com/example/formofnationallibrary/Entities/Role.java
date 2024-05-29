package com.example.formofnationallibrary.Entities;

import jakarta.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_role;

    private String name;

    public Long getId() {
        return id_role;
    }

    public void setId(Long id) {
        this.id_role = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
// Getters and Setters
}

