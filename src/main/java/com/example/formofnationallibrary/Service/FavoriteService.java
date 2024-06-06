package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Book;
import com.example.formofnationallibrary.Entities.Favorite;
import com.example.formofnationallibrary.Repository.BookRepository;
import com.example.formofnationallibrary.Repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BookRepository bookRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, BookRepository bookRepository) {
        this.favoriteRepository = favoriteRepository;
        this.bookRepository = bookRepository;
    }
    public void addFavorite(Long bookId, Long userId) {

        if (!favoriteRepository.existsByBookIdAndUserId(bookId, userId)) {
            // Если не существует, то добавляем новую запись
            Favorite favorite = new Favorite();
            favorite.setBookId(bookId);
            favorite.setUserId(userId);

            favoriteRepository.save(favorite);
        } else {
            return;
        }
    }
    public void deleteFavoritesByBookId(Long bookId) {
        List<Favorite> favoritesToDelete = favoriteRepository.findByBookId(bookId);
        favoriteRepository.deleteAll(favoritesToDelete);
    }
    public List<Favorite> findByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public void removeById(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
