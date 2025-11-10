package org.example.service;

import org.example.dao.UserDao;
import org.example.models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

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
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        return user;
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        validateUser(user);
        userDao.saveUser(user);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        validateUser(user);
        userDao.updateUser(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    private void validateUser(User user) {

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        } else if (user.getName().trim().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters long");
        } else if (user.getName().trim().length() > 50) {
            throw new IllegalArgumentException("Name must be no more than 50 characters long");
        }

        if (user.getAge() < 0) {
            throw new IllegalArgumentException("Age must be positive");
        } else if (user.getAge() > 150) {
            throw new IllegalArgumentException("Age must be realistic (max 150)");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        } else if (!validateEmail(user.getEmail().trim())) {
            throw new IllegalArgumentException("Email should be valid");
        } else if (user.getEmail().trim().length() > 100) {
            throw new IllegalArgumentException("Email must be no more than 100 characters long");
        }
    }

    private boolean validateEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

}
