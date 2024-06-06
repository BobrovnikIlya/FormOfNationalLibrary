package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Publish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublishRepository extends JpaRepository<Publish, Long> {
    List<Publish> findByNameContaining(String term);
    Publish findByName(String name);
    // Дополнительные методы, если необходимо
}
