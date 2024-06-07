package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Copies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CopiesRepository extends JpaRepository<Copies, Long> {
    List<Copies> findByBookId(Long bookId);
    @Modifying
    @Query("DELETE FROM Copies c WHERE c.book.id = :bookId")
    void deleteByBookId(@Param("bookId") Long bookId);

    @Query("SELECT c.idCopies FROM Copies c WHERE c.book.id = :bookId")
    List<Long> findIdsByBookId(@Param("bookId") Long bookId);
}

