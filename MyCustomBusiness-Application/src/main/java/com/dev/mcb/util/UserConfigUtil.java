package com.dev.mcb.util;

import com.dev.mcb.core.UserConfigEntity;
import com.dev.mcb.core.UserEntity;
import com.dev.mcb.core.enums.UserConfigType;
import com.dev.mcb.dao.impl.UserDAOImpl;

import javax.inject.Inject;
import java.util.Optional;

public class UserConfigUtil {

    @Inject
    private UserDAOImpl userDAO;

    /**
     * Retrieve user salt
     * @param userEntity a user
     * @return the user salt
     */
    public Optional<UserConfigEntity> getSalt(UserEntity userEntity) {
        return getConfiguration(userEntity, UserConfigType.SALT);
    }

    /**
     * Retrieve user private key
     * @param userEntity a user
     * @return the user private key
     */
    public Optional<UserConfigEntity> getPrivateKey(UserEntity userEntity) {
        return getConfiguration(userEntity, UserConfigType.KEY);
    }

    /**
     * Retrieve a {@link UserConfigEntity} for a user
     * @param userEntity a user
     * @param userConfigType a configuration type
     * @return a configuration
     */
    private Optional<UserConfigEntity> getConfiguration(UserEntity userEntity, UserConfigType userConfigType) {
        return userEntity.getConfigurations().stream()
                .filter(userConfigEntity -> userConfigEntity.getUser().getId().equals(userEntity.getId()))
                .filter(userConfigEntity -> userConfigType.toString().equals(userConfigEntity.getKey()))
                .findFirst();
    }
}
