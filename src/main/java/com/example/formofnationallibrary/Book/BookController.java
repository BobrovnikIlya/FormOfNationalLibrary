package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.Book;
import com.example.formofnationallibrary.Entities.Copies;
import com.example.formofnationallibrary.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

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
        model.addAttribute("loggedIn", true);

        ModelAndView mav = new ModelAndView("AddBook");
        mav.addObject("book", new Book());
        mav.addObject("authors", authorService.getAllAuthors());
        mav.addObject("languages", languageService.getAllLanguages());
        mav.addObject("publishes", publishService.getAllPublishes());
        return mav;
    }
    @PostMapping("/addBook")
    public ModelAndView addBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return new ModelAndView("redirect:/home");
    }
}

