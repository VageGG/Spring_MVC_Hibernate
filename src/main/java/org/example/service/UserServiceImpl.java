package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dao.UserDao;
import org.example.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        return user;
    }

    @Transactional
    @Override
    public void saveOrUpdateUser(User user) {
        existsEmail(user);
        userDao.saveOrUpdateUser(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    private void existsEmail(User user) {
        if (user.getId() == null) {
            if (userDao.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        } else {
            User existing = userDao.getUserById(user.getId());
            if (!Objects.equals(existing.getEmail(), user.getEmail()) &&
                    userDao.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        }
    }
}
