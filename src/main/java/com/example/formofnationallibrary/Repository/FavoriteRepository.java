package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Book;
import com.example.formofnationallibrary.Entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByBookIdAndUserId(Long bookId, Long userId);
    @Query("SELECT f.bookId FROM Favorite f WHERE f.userId = :userId")
    List<Book> findFavoritesByUserId(@Param("userId") Long userId);
    // Можно добавить дополнительные методы, если необходимо
}

