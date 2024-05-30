package com.example.formofnationallibrary.Controller;

import com.example.formofnationallibrary.Entities.Book;
import com.example.formofnationallibrary.Entities.User;
import com.example.formofnationallibrary.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@SessionAttributes("loggedInUser")
public class AccauntsController {
    private final UserService userService;

    public AccauntsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/accounts")
    public ModelAndView showAccounts(Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null && loggedInUser.getRole().getId() == 1) {
            ModelAndView modelAndView = new ModelAndView("Users");
            modelAndView.addObject("users", userService.getAllUsers());
            return modelAndView;
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteUserAndRelatedRecords(userId);
        return "redirect:/accounts";
    }
}


