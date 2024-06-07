package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Copies;
import com.example.formofnationallibrary.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUserId(Long userId);
    List<Order> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    @Modifying
    @Query("DELETE FROM Order o WHERE o.copies.idCopies IN :copiesIds")
    void deleteByCopiesIds(@Param("copiesIds") List<Long> copiesIds);
}