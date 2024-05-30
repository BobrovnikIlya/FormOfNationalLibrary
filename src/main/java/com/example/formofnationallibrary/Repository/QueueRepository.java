package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueueRepository extends JpaRepository<Queue, Long> {
    List<Queue> findByBookIdOrderByQueueNumberDesc(Long bookId);
    List<Queue> findQueueByUserId(Long userId);
}