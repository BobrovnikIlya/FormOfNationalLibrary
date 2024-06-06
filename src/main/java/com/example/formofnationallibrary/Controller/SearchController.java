package com.example.formofnationallibrary.Controller;

import com.example.formofnationallibrary.Entities.*;
import com.example.formofnationallibrary.Repository.*;
import com.example.formofnationallibrary.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("loggedInUser")
public class SearchController {
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CitiesService citiesService;

    @Autowired
    private LanguageService languageService;
    @Autowired
    private PublishService publishService;
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
    public ModelAndView searchBooks(@RequestParam(defaultValue = "") String query, @RequestParam(defaultValue = "") String field, Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
        } else {
            model.addAttribute("loggedIn", false);
        }

        if (query.trim().isEmpty()) {
            ModelAndView mav = new ModelAndView("Catalog");
            mav.addObject("error", "Пожалуйста, введите текст для поиска.");
            mav.addObject("books", Collections.emptyList());
            return mav;
        }

        List<Book> books;
        switch (field) {
            case "author":
                books = bookRepository.findByAuthorName(query);
                break;
            case "title":
                books = bookRepository.findByTitle(query);
                break;
            case "category":
                books = bookRepository.findByCategoryName(query);
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
            mav.addObject("id", bookId); // Передаем ID книги в представление
            return mav;
        } else {
            return new ModelAndView("redirect:/search");
        }
    }
    @PostMapping("/changeBook")
    public String changeBook(@ModelAttribute("book") Book updatedBook, @RequestParam Long bookId) {

        Author existingAuthor = authorService.findAuthorByName(updatedBook.getAuthor().getName());
        if (existingAuthor == null) {
            // Если автора нет в базе данных, добавляем его
            existingAuthor = new Author();
            existingAuthor.setName(updatedBook.getAuthor().getName());
            authorService.saveAuthor(existingAuthor);
        }

        Language existingLanguage = languageService.findLanguageByName(updatedBook.getLanguage().getName());
        if (existingLanguage == null) {
            // Если языка нет в базе данных, добавляем его
            existingLanguage = new Language();
            existingLanguage.setName(updatedBook.getLanguage().getName());
            languageService.saveLanguage(existingLanguage);
        }

        Publish existingPublish = publishService.findPublishByName(updatedBook.getPublish().getName());
        if (existingPublish == null) {
            // Если языка нет в базе данных, добавляем его
            existingPublish = new Publish();
            existingPublish.setName(updatedBook.getPublish().getName());
            publishService.savePublish(existingPublish);
        }

        Cities existingCities = citiesService.findCitiesByName(updatedBook.getLanguage().getName());
        if (existingCities == null) {
            // Если языка нет в базе данных, добавляем его
            existingCities = new Cities();
            existingCities.setName(updatedBook.getCities().getName());
            citiesService.saveCities(existingCities);
        }

        updatedBook.setAuthor(existingAuthor);
        updatedBook.setLanguage(existingLanguage);
        updatedBook.setCities(existingCities);
        updatedBook.setPublish(existingPublish);

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


