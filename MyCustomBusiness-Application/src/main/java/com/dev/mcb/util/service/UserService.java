package com.dev.mcb.util.service;

import com.dev.mcb.User;
import com.dev.mcb.core.UserEntity;
import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.mapper.UserMapper;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {

    private UserDAO userDAO;

    @Inject
    private UserMapper userMapper;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Optional<User> findUserById(long userId) {
        Optional<UserEntity> userEntity = userDAO.findById(userId);
        return userEntity.map(entity -> userMapper.from(entity));
    }

    public User createUser(User newUser) {
        UserEntity newUserEntity = userMapper.map(newUser);
        return userMapper.from(userDAO.create(newUserEntity));
    }

    public User updateUser(long userId, User updatedUser) {
        UserEntity updateUserEntity = userMapper.map(updatedUser);
        updateUserEntity.setId(userId);
        return userMapper.from(userDAO.update(updateUserEntity));
    }

    public void deleteUser(long userId) {
        userDAO.delete(userId);
    }

    public List<User> findAll() {
        return userDAO.findAll().stream().map(userMapper::from).collect(Collectors.toList());
    }
}
