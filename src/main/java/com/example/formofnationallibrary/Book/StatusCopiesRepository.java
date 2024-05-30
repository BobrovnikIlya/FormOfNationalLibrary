package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.StatusCopies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface StatusCopiesRepository extends JpaRepository<StatusCopies, Long> {
    // Дополнительные методы, если необходимо
}
