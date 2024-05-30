package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

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
}
