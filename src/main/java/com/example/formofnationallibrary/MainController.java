package com.example.formofnationallibrary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {
    @GetMapping("/home")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView("Home");
        return modelAndView;
    }
    @GetMapping("/person")
    public ModelAndView person(){
        ModelAndView modelAndView = new ModelAndView("Person");
        return modelAndView;
    }
}
