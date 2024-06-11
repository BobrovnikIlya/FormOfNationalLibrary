package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QueueRepository extends JpaRepository<Queue, Long> {
    List<Queue> findByBookIdOrderByQueueNumberDesc(Long bookId);
    List<Queue> findByUserId(Long userId);
    List<Queue> findByBookIdAndQueueNumberGreaterThan(Long bookId, int queueNumber);
    Optional<Queue> findByUserIdAndBookId(Long userId, Long bookId);
    void deleteByUserId(Long userId);
    @Modifying
    @Query("DELETE FROM Queue q WHERE q.book.id = :bookId")
    void deleteByBookId(@Param("bookId") Long bookId);

    List<Queue> findQueueByBookIdOrderByQueueNumberAsc(Long bookId);
}