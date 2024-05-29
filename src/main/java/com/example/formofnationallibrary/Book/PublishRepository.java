package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.Publish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishRepository extends JpaRepository<Publish, Long> {
    // Дополнительные методы, если необходимо
}
