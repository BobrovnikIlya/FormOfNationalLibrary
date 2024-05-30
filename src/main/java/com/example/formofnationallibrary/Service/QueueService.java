package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Queue;
import com.example.formofnationallibrary.Repository.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Queue> getUserQueue(Long userId) {
        return queueRepository.findQueueByUserId(userId);
    }
}
