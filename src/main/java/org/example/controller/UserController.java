package org.example.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getShowAllUsers(
            @RequestParam(value = "editId", required = false) Long editId,
            Model model) {

        model.addAttribute("users", userService.getAllUsers());

        if (editId != null) {
            try {
                User user = userService.getUserById(editId);
                model.addAttribute("userForm", user);
            } catch (EntityNotFoundException e) {
                model.addAttribute("error", "User not found with id: " + editId);
                model.addAttribute("userForm", new User());
            }
        } else {
            model.addAttribute("userForm", new User());
        }

        return "users";
    }

    @PostMapping("/add")
    public String saveUser(@Valid @ModelAttribute("userForm") User user,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("error", "Validation error: please check your input");
            return "users";
        }

        try {
            userService.saveOrUpdateUser(user);
            return "redirect:/users";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("error", "Database error: " + e.getMessage());
            return "users";
        } catch (IllegalArgumentException e) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("error", e.getMessage());
            return "users";
        } catch (Exception e) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("error", "Unexpected error: " + e.getMessage());
            return "users";
        }
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id, Model model) {
        try {
            userService.deleteUser(id);
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("userForm", new User());
            model.addAttribute("error", "Error deleting user: " + e.getMessage());
            return "users";
        }
    }
}