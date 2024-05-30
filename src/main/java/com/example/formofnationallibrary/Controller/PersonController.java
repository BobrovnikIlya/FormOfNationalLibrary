package com.example.formofnationallibrary.Controller;

import com.example.formofnationallibrary.Entities.Book;
import com.example.formofnationallibrary.Entities.Favorite;
import com.example.formofnationallibrary.Entities.User;
import com.example.formofnationallibrary.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@SessionAttributes("loggedInUser")
public class PersonController {
    private final UserService userService;
    private final FavoriteService favoriteService;
    private final BookService bookService;

    @Autowired
    public PersonController(UserService userService, FavoriteService favoriteService, BookService bookService) {
        this.userService = userService;
        this.favoriteService = favoriteService;
        this.bookService = bookService;
    }

    @GetMapping("/person")
    public ModelAndView person(Model model) {
        ModelAndView modelAndView = new ModelAndView("Person");
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
            Optional<User> userOptional = userService.findById(loggedInUser.getId());
            userOptional.ifPresent(user -> model.addAttribute("loggedInUserDetails", user));
        } else {
            model.addAttribute("loggedIn", false);
        }
        return modelAndView;
    }

    @GetMapping("/getUserDetails")
    @ResponseBody
    public Map<String, Object> getUserDetails(Model model) {
        Map<String, Object> response = new HashMap<>();
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            Optional<User> userOptional = userService.findById(loggedInUser.getId());
            userOptional.ifPresent(user -> response.put("user", user));
        }
        return response;
    }

    @GetMapping("/getFavorites")
    @ResponseBody
    public Map<String, Object> getFavorites(Model model) {
        Map<String, Object> response = new HashMap<>();
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            List<Favorite> favorites = favoriteService.findByUserId(loggedInUser.getId());
            List<Map<String, Object>> favoriteBooks = favorites.stream()
                    .map(favorite -> {
                        Map<String, Object> favoriteBook = new HashMap<>();
                        favoriteBook.put("id", favorite.getId());
                        Book book = bookService.findById(favorite.getBookId());
                        favoriteBook.put("bookName", book != null ? book.getName() : "Неизвестно");
                        return favoriteBook;
                    })
                    .collect(Collectors.toList());
            response.put("favorites", favoriteBooks);
        }
        return response;
    }


    @PostMapping("/removeFromFavorites")
    @ResponseBody
    public Map<String, Object> removeFromFavorites(@RequestBody Map<String, Long> request) {
        Map<String, Object> response = new HashMap<>();
        Long favoriteId = request.get("id");
        favoriteService.removeById(favoriteId);
        response.put("success", true);
        return response;
    }
}

