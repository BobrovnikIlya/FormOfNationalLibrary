package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Book;
import com.example.formofnationallibrary.Entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByBookIdAndUserId(Long bookId, Long userId);
    void deleteByUserId(Long userId);
    List<Favorite> findByBookId(Long bookId);
    List<Favorite> findByUserId(Long userId);
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.bookId = :bookId")
    void deleteByBookId(@Param("bookId") Long bookId);
    // Можно добавить дополнительные методы, если необходимо
}

