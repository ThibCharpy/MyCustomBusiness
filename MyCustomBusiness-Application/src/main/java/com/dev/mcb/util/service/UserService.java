package com.dev.mcb.util.service;

import com.dev.mcb.User;
import com.dev.mcb.dao.UserDAO;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

public class UserService {

    @Inject
    UserDAO userDAO;

    public User findUserById(long userId) {
        //TODO: to be implemented
        return null;
    }

    public User createUser(User newUser) {
        //TODO: to be implemented
        return null;
    }

    public User updateUser(User updatedUser) {
        //TODO: to be implemented
        return null;
    }

    public void deleteUser(long userId) {
        //TODO: to be implemented
    }

    public List<User> findAll() {
        //TODO: to be implemented
        return Collections.emptyList();
    }
}
