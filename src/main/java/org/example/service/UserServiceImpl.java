package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.dao.UserDao;
import org.example.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    public void saveUser(User user) {
        validateUser(user);
        userDao.saveUser(user);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        if (userDao.getUserById(user.getId()) == null) {
            throw new EntityNotFoundException("User not found with id: " + user.getId());
        }
        validateUser(user);
        userDao.updateUser(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    private void validateUser(User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .findFirst()
                    .orElse("Validation failed");
            throw new IllegalArgumentException(message);
        }

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
