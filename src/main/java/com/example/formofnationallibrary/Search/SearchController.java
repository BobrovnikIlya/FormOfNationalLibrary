package com.example.formofnationallibrary.Search;

import com.example.formofnationallibrary.Book.*;
import com.example.formofnationallibrary.Entities.Book;
import com.example.formofnationallibrary.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("loggedInUser")
public class SearchController {
    @Autowired
    private BookService bookService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private PublishRepository publishRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping("/search")
    public ModelAndView searchBooks(@RequestParam String query, @RequestParam String field, Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
        } else {
            model.addAttribute("loggedIn", false);
        }

        List<Book> books;
        switch (field) {
            case "author":
                books = bookRepository.findByAuthorName(query);
                break;
            case "title":
                books = bookRepository.findByTitle(query);
                break;
            default:
                books = bookRepository.findAllByQuery(query);
        }

        ModelAndView mav = new ModelAndView("Catalog");
        mav.addObject("books", books);
        return mav;
    }
    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam Long bookId) {
        bookService.deleteBookById(bookId);
        return "redirect:/home"; // Перенаправляем обратно на страницу поиска после удаления
    }

    @GetMapping("/changeBook")
    public ModelAndView changeBook(@RequestParam Long bookId, Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            Book book = bookRepository.findById(bookId).orElse(null);
            ModelAndView mav = new ModelAndView("ChangeBook");
            mav.addObject("book", book);
            mav.addObject("authors", authorRepository.findAll());
            mav.addObject("languages", languageRepository.findAll());
            mav.addObject("publishes", publishRepository.findAll());
            mav.addObject("id", bookId);
            return mav;
        }
        else{
            return new ModelAndView("redirect:/home");
        }
    }
    @PostMapping("/changeBook")
    public String changeBook(@ModelAttribute("book") Book updatedBook, @RequestParam Long bookId) {
        bookService.updateBook(updatedBook, bookId);
        return "redirect:/home";
    }
    @PostMapping("/addFavorite")
    public String addFavorite(@RequestParam Long bookId, Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            favoriteService.addFavorite(bookId, loggedInUser.getId());
            return "redirect:/home";
        } else {
            return "redirect:/login"; // Перенаправляем на страницу входа, если пользователь не авторизован
        }
    }
}


