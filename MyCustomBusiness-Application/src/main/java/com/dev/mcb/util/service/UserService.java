package com.dev.mcb.util.service;

import com.dev.mcb.User;
import com.dev.mcb.core.UserEntity;
import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.mapper.UserMapper;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserService {

    @Inject
    UserDAO userDAO;

    @Inject
    UserMapper userMapper;

    public Optional<User> findUserById(long userId) {
        Optional<UserEntity> userEntity = userDAO.findById(userId);
        return userEntity.map(entity -> userMapper.from(entity));
    }

    public User createUser(User newUser) {
        //TODO: to be implemented
        return null;
    }

    public Optional<User> updateUser(long userId, User updatedUser) {
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
