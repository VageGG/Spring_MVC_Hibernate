package org.example.dao;

import org.example.models.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();
    User getUserById(long id);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(long id);
}
