package com.example.formofnationallibrary.Controller;

import com.example.formofnationallibrary.Entities.Book;
import com.example.formofnationallibrary.Entities.User;
import com.example.formofnationallibrary.Repository.AuthorRepository;
import com.example.formofnationallibrary.Repository.BookRepository;
import com.example.formofnationallibrary.Repository.LanguageRepository;
import com.example.formofnationallibrary.Repository.PublishRepository;
import com.example.formofnationallibrary.Service.BookService;
import com.example.formofnationallibrary.Service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public ModelAndView searchBooks(@RequestParam(defaultValue = " ") String query, @RequestParam(defaultValue = "") String field, Model model) {
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
        return "redirect:/search";
    }

    @GetMapping("/changeBook")
    public ModelAndView changeBook(@RequestParam Long bookId, Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null && loggedInUser.getRole().getName().equals("Admin")) {
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
            return new ModelAndView("redirect:/search");
        }
    }
    @PostMapping("/changeBook")
    public String changeBook(@ModelAttribute("book") Book updatedBook, @RequestParam Long bookId) {
        bookService.updateBook(updatedBook, bookId);
        return "redirect:/book/" + bookId;
    }
    @PostMapping("/addFavorite")
    public String addFavorite(@RequestParam Long bookId, Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            favoriteService.addFavorite(bookId, loggedInUser.getId());
            return "redirect:/search";
        } else {
            return "redirect:/login"; // Перенаправляем на страницу входа, если пользователь не авторизован
        }
    }
}


