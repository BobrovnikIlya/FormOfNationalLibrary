package com.example.formofnationallibrary.Search;

import com.example.formofnationallibrary.Entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/search")
    public ModelAndView searchBooks(@RequestParam String query, @RequestParam String field) {
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
}


