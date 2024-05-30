package com.example.formofnationallibrary.Controller;

import com.example.formofnationallibrary.Entities.User;
import com.example.formofnationallibrary.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@SessionAttributes("loggedInUser")
public class PersonController {
    private final UserService userService;

    @Autowired
    public PersonController(UserService userService) {
        this.userService = userService;
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
}

