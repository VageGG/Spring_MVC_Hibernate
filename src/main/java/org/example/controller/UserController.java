package org.example.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String getShowAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @PostMapping("/add")
    public String addUser(
            @RequestParam("name") String name,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam("email") String email,
            Model model) {

        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);
        try {
            userService.saveUser(user);
            return "redirect:/users";
        } catch (DataIntegrityViolationException | IllegalArgumentException | ConstraintViolationException e) {
            model.addAttribute("error", e.getMessage());
            return "users";
        }
    }

    @PostMapping("/update")
    public String updateUser(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam("email") String email,
            Model model) {

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);
        try {
            userService.updateUser(user);
            return "redirect:/users";
        } catch (DataIntegrityViolationException | IllegalArgumentException | ConstraintViolationException e) {
            model.addAttribute("error", e.getMessage());
            return "users";
        }
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        try {
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", "User not found");
        }
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }
}
