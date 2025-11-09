package org.example.controllers;

import org.example.models.User;
import org.example.service.UserService;
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
    public String getShowAllUsers(@RequestParam(value = "id", required = false) Long id,
                                  Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @PostMapping("/add")
    public String addUser(
            @RequestParam("name") String name,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam("email") String email) {

        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);
        userService.saveUser(user);
        return "redirect:/users";
    }

    @PostMapping("/update")
    public String updateUser(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam("email") String email) {

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);
        userService.updateUser(user);
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }
}
