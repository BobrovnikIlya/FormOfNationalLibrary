package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUserId(Long userId);
    List<Order> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}