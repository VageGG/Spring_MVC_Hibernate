package org.example.dao;

import org.example.models.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();
    User getUserById(long id);
    void saveOrUpdateUser(User user);
    void deleteUser(long id);
}
