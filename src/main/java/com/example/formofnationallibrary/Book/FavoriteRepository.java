package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByBookIdAndUserId(Long bookId, Long userId);
    // Можно добавить дополнительные методы, если необходимо
}

