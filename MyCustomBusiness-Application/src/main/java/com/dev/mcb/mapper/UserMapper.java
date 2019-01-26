package com.dev.mcb.mapper;

import com.dev.mcb.User;
import com.dev.mcb.core.UserEntity;

public class UserMapper {

    //TODO: password must be no convert in datas

    public User from(UserEntity userEntity) {
        User user = new User();
        user.setUsername(userEntity.getUsername());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        return user;
    }

    public UserEntity map(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        return userEntity;
    }

}
