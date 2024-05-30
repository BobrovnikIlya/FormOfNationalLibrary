package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QueueRepository extends JpaRepository<Queue, Long> {
    List<Queue> findByBookIdOrderByQueueNumberDesc(Long bookId);
    List<Queue> findByUserId(Long userId);
    List<Queue> findByBookIdAndQueueNumberGreaterThan(Long bookId, int queueNumber);
    Optional<Queue> findByUserIdAndBookId(Long userId, Long bookId);
    void deleteByUserId(Long userId);
}