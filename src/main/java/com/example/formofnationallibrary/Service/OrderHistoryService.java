package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.OrderHistory;
import com.example.formofnationallibrary.Repository.OrderHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderHistoryService {

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;
    public void save(OrderHistory orderHistory) {
        orderHistoryRepository.save(orderHistory);
    }
    public List<OrderHistory> findByUserId(Long userId) {
        return orderHistoryRepository.findByUserId(userId);
    }
}
