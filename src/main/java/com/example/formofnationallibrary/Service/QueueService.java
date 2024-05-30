package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Queue;
import com.example.formofnationallibrary.Repository.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QueueService {

    @Autowired
    private QueueRepository queueRepository;

    public void save(Queue queue) {
        queueRepository.save(queue);
    }
    public List<Queue> findQueueByBookId(Long bookId) {
        return queueRepository.findByBookIdOrderByQueueNumberDesc(bookId);
    }
    public Optional<Queue> findById(Long id) {
        return queueRepository.findById(id);
    }

    public List<Queue> findByBookIdAndQueueNumberGreaterThan(Long bookId, int queueNumber) {
        return queueRepository.findByBookIdAndQueueNumberGreaterThan(bookId, queueNumber);
    }
    public List<Queue> findByUserId(Long userId) {
        return queueRepository.findByUserId(userId);
    }
    public void removeById(Long queueId) {
        queueRepository.deleteById(queueId);
    }
}
