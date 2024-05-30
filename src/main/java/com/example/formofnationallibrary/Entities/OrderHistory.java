package com.example.formofnationallibrary.Entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_history")
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_copies", nullable = false)
    private Copies copies;

    @Column(name = "issue_date", nullable = false)
    private Date issueDate;

    @Column(name = "return_date", nullable = false)
    private Date returnDate;

    // Конструкторы, геттеры и сеттеры
}

