package com.example.formofnationallibrary.Search;

import com.example.formofnationallibrary.Entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/book/{id}")
    public ModelAndView getBook(@PathVariable Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            ModelAndView mav = new ModelAndView("Book");
            mav.addObject("book", bookOptional.get());
            return mav;
        } else {
            return new ModelAndView("error/404");
        }
    }
}
