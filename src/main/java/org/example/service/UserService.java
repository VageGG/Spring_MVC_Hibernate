package org.example.service;

import org.example.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(long id);
    void saveOrUpdateUser(User user);
    void deleteUser(long id);
}
