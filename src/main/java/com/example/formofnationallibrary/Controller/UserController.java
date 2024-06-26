package com.example.formofnationallibrary.Controller;

import com.example.formofnationallibrary.Service.UserService;
import com.example.formofnationallibrary.Entities.Role;
import com.example.formofnationallibrary.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@SessionAttributes("loggedInUser")
public class UserController {

    @Autowired
    private UserService userService;

    @ModelAttribute("loggedInUser")
    public User loggedInUser() {
        return null; // Initialize the session attribute
    }

    @GetMapping("/home")
    public ModelAndView home(Model model) {
        // Проверяем, есть ли залогиненный пользователь в сессии
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
        } else {
            model.addAttribute("loggedIn", false);
        }
        return new ModelAndView("Home");
    }

    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("user", new User());
        mav.addObject("roles", userService.getAllRoles());
        return mav;
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm() {
        return new ModelAndView("login");
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute User user, @RequestParam String confirmPassword) {
        ModelAndView mav = new ModelAndView("register");

        if (!user.getPassword().equals(confirmPassword)) {
            mav.addObject("message", "Пароли не совпадают!");
            mav.addObject("roles", userService.getAllRoles());
            return mav;
        }

        if (userService.existsByLogin(user.getLogin())) {
            mav.addObject("message", "Логин уже существует!");
            mav.addObject("roles", userService.getAllRoles());
            return mav;
        }
        if (userService.existsByEmail(user.getEmail())) {
            mav.addObject("message", "Почта уже занята!");
            mav.addObject("roles", userService.getAllRoles());
            return mav;
        }

        userService.registerUser(user);
        mav.addObject("message", "Регистрация успешна!");
        mav.addObject("roles", userService.getAllRoles());
        mav.setViewName("redirect:/accounts");
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String username, @RequestParam String password, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        if (userService.authenticate(username, password)) {
            User user = userService.findByLogin(username);
            model.addAttribute("loggedInUser", user);
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("login");
            modelAndView.addObject("error", "Неправильный логин или пароль");
        }
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout(SessionStatus status) {
        status.setComplete();
        return "redirect:/home";
    }

    @GetMapping("/roles")
    @ResponseBody
    public List<Role> getRoles() {
        return userService.getAllRoles();
    }
}




