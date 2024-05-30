package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Order;
import com.example.formofnationallibrary.Entities.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findByUserId(Long userId);
}


