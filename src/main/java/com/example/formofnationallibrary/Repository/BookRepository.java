package com.example.formofnationallibrary.Repository;


import com.example.formofnationallibrary.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.author.name LIKE %:query%")
    List<Book> findByAuthorName(@Param("query") String query);

    @Query("SELECT b FROM Book b WHERE b.name LIKE %:query%")
    List<Book> findByTitle(@Param("query") String query);

    @Query("SELECT b FROM Book b WHERE b.name LIKE %:query% OR b.author.name LIKE %:query% OR b.description LIKE %:query% ORDER BY b.name")
    List<Book> findAllByQuery(@Param("query") String query);
}


