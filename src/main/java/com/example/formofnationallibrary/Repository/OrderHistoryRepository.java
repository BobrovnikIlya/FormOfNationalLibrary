package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Copies;
import com.example.formofnationallibrary.Entities.Order;
import com.example.formofnationallibrary.Entities.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    @Modifying
    @Query("DELETE FROM OrderHistory oh WHERE oh.copies.idCopies IN :copiesIds")
    void deleteByCopiesIds(@Param("copiesIds") List<Long> copiesIds);
    void deleteAllByCopies(Copies copies);}


