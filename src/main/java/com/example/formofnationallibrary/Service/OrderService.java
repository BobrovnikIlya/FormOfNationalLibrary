package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Order;
import com.example.formofnationallibrary.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
    public void removeById(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findOrdersByUserId(userId);
    }
}

