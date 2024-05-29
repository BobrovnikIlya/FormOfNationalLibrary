package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("loggedInUser")
public class BookController {


    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private PublishService publishService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CopiesRepository copiesRepository;

    @GetMapping("/book/{id}")
    public ModelAndView getBook(@PathVariable Long id, Model model) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            User loggedInUser = (User) model.getAttribute("loggedInUser");
            if (loggedInUser != null) {
                model.addAttribute("loggedIn", true);
            } else {
                model.addAttribute("loggedIn", false);
            }

            ModelAndView mav = new ModelAndView("book");
            Book book = bookOptional.get();
            mav.addObject("book", book);

            // Получение всех копий для данной книги
            List<Copies> copiesList = copiesRepository.findByBookId(book.getId());
            mav.addObject("copiesList", copiesList);

            return mav;
        } else {
            return new ModelAndView("error/404");
        }
    }

    @GetMapping("/addBook")
    public ModelAndView showAddBookForm(Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);

            ModelAndView mav = new ModelAndView("AddBook");
            mav.addObject("book", new Book());

            List<Author> sortedAuthors = authorService.getAllAuthors().stream()
                    .sorted(Comparator.comparing(Author::getName))
                    .collect(Collectors.toList());
            mav.addObject("authors", sortedAuthors);

            List<Language> sortedLanguages = languageService.getAllLanguages().stream()
                    .sorted(Comparator.comparing(Language::getName))
                    .collect(Collectors.toList());
            mav.addObject("languages", sortedLanguages);

            List<Publish> sortedPublishes = publishService.getAllPublishes().stream()
                    .sorted(Comparator.comparing(Publish::getName))
                    .collect(Collectors.toList());
            mav.addObject("publishes", sortedPublishes);

            return mav;
        } else {
            // Если пользователь не администратор или не авторизован, перенаправляем его куда-то еще
            return new ModelAndView("redirect:/home");
        }
    }
    @PostMapping("/addBook")
    public ModelAndView addBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/addAuthor")
    public ModelAndView showAddAuthorForm(Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
        model.addAttribute("loggedIn", true);
        ModelAndView mav = new ModelAndView("AddAuthor");
        mav.addObject("author", new Author());
        return mav;
        }else{
            return new ModelAndView("redirect:/home");
        }
    }
    @PostMapping("/addAuthor")
    public ModelAndView addAuthor(@ModelAttribute Author author) {
        authorService.saveAuthor(author);
        return new ModelAndView("redirect:/addBook");
    }

    @GetMapping("/addPublish")
    public ModelAndView showAddPublishForm(Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
            ModelAndView mav = new ModelAndView("AddPublish");
            mav.addObject("publish", new Publish());
            return mav;
        }else{
            return new ModelAndView("redirect:/home");
        }
    }
    @PostMapping("/addPublish")
    public ModelAndView addPublish(@ModelAttribute Publish publish) {
        publishService.savePublish(publish);
        return new ModelAndView("redirect:/addBook");
    }
    @GetMapping("/addLanguage")
    public ModelAndView showAddLanguageForm(Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
            ModelAndView mav = new ModelAndView("AddLanguage");
            mav.addObject("language", new Language());
            return mav;
        }else{
            return new ModelAndView("redirect:/home");
        }
    }
    @PostMapping("/addLanguage")
    public ModelAndView addLa(@ModelAttribute Language language) {
        languageService.saveLanguage(language);
        return new ModelAndView("redirect:/addBook");
    }


}

