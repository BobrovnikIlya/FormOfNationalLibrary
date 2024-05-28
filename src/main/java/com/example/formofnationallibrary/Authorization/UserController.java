package com.example.formofnationallibrary.Authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("user", new User());
        mav.addObject("roles", userService.getAllRoles());
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute User user, @RequestParam String confirmPassword) {
        ModelAndView mav = new ModelAndView("register");

        // Validate passwords
        if (!user.getPassword().equals(confirmPassword)) {
            mav.addObject("message", "Пароли не совпадают!");
            mav.addObject("roles", userService.getAllRoles());
            return mav;
        }

        // Check if login already exists
        if (userService.existsByLogin(user.getLogin())) {
            mav.addObject("message", "Логин уже существует!");
            mav.addObject("roles", userService.getAllRoles());
            return mav;
        }

        userService.registerUser(user);
        mav.addObject("message", "Регистрация успешна!");
        mav.addObject("roles", userService.getAllRoles());
        return mav;
    }

    @GetMapping("/roles")
    @ResponseBody
    public List<Role> getRoles() {
        return userService.getAllRoles();
    }
}

