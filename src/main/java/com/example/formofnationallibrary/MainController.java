package com.example.formofnationallibrary;

import com.example.formofnationallibrary.Entities.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@RestController
@SessionAttributes("loggedInUser")
public class MainController {

    @GetMapping("/person")
    public ModelAndView person(Model model){
        ModelAndView modelAndView = new ModelAndView("Person");
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
        } else {
            model.addAttribute("loggedIn", false);
        }
        return new ModelAndView("Person");
    }
}
