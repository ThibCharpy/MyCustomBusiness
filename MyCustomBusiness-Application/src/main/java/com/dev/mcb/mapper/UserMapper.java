package com.dev.mcb.mapper;

import com.dev.mcb.User;
import com.dev.mcb.core.UserEntity;

import java.util.Optional;

public class UserMapper {

    /**
     * Convert User entity to user api object
     * @param userEntity user entity
     * @return user api object
     */
    public static User from(UserEntity userEntity) {
        User user = new User();
        user.id = userEntity.getId();
        user.username = userEntity.getUsername();
        user.email = userEntity.getEmail();
        return user;
    }

    /**
     * Convert User api object to user entity
     * @param user the api object
     * @return user entity
     */
    public static UserEntity to(User user) {
        return Optional.ofNullable(user)
                .map(e -> {
                    UserEntity userEntity = new UserEntity();
                    Optional.ofNullable(e.id).ifPresent(userEntity::setId);
                    Optional.ofNullable(e.username).ifPresent(userEntity::setUsername);
                    Optional.ofNullable(e.email).ifPresent(userEntity::setEmail);
                    Optional.ofNullable(e.password).ifPresent(userEntity::setPassword);
                    return userEntity;
                })
                .orElse(null);
    }

}
