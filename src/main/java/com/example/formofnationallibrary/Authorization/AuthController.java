package com.example.formofnationallibrary.Authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    @GetMapping("/authorization")
    public ModelAndView authorization(){
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }
    @GetMapping("/register")
    public ModelAndView register(){
        ModelAndView modelAndView = new ModelAndView("register");
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String username, @RequestParam String password) {
        ModelAndView modelAndView = new ModelAndView();
        if (userService.authenticate(username, password)) {
            // Успешная аутентификация
            modelAndView.setViewName("redirect:/home");
        } else {
            // Ошибка аутентификации
            modelAndView.setViewName("login");
            modelAndView.addObject("error", "Неправильный логин или пароль");
        }
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(@RequestParam String username, @RequestParam String password,
                                 @RequestParam String cpassword, @RequestParam String email) {
        ModelAndView modelAndView = new ModelAndView();

        if (!password.equals(cpassword)) {
            modelAndView.setViewName("register");
            modelAndView.addObject("message", "Пароли не совпадают");
            return modelAndView;
        }

        if (userService.findByLogin(username) != null) {
            modelAndView.setViewName("register");
            modelAndView.addObject("message", "Пользователь с таким именем уже существует");
            return modelAndView;
        }

        User user = new User();
        user.setLogin(username);
        user.setPassword(password);
        user.setEmail(email);
        //user.setRole("role"); // по умолчанию назначаем роль user
        userService.saveUser(user);

        modelAndView.setViewName("login");
        modelAndView.addObject("message", "Регистрация успешна. Пожалуйста, войдите.");
        return modelAndView;
    }
}
