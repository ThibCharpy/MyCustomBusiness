package com.dev.mcb.auth;

import com.dev.mcb.core.UserEntity;
import io.dropwizard.auth.Authorizer;

public class UserAuthorizer implements Authorizer<UserEntity> {

    @Override
    public boolean authorize(UserEntity userEntity, String s) {
        return userEntity != null;
    }
}
