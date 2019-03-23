package com.dev.mcb.auth;

import com.dev.mcb.core.UserConfigEntity;
import com.dev.mcb.core.UserEntity;
import com.dev.mcb.core.enums.UserConfigType;
import com.dev.mcb.dao.impl.UserDAOImpl;
import com.dev.mcb.util.HashedPasswordUtil;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserAuthenticator implements Authenticator<BasicCredentials, UserEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticator.class);

    @Inject
    private UserDAOImpl userDAO;

    @Inject
    private HashedPasswordUtil hashedPasswordUtil;

    @Override
    public Optional<UserEntity> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        LOGGER.debug("Trying to authenticate user with username(email): {}", basicCredentials.getUsername());

        UserEntity userEntity = userDAO.findByEmail(basicCredentials.getUsername());
        List<UserConfigEntity> saltConfig = userEntity.getConfigurations().stream()
                .filter(userConfigEntity -> userConfigEntity.getUser().getId().equals(userEntity.getId()))
                .filter(userConfigEntity -> UserConfigType.SALT.toString().equals(userConfigEntity.getKey()))
                .collect(Collectors.toList());

        if (saltConfig.size() > 1) {
            LOGGER.error("Retrieved multiple configurations value");
            throw new IllegalStateException("Multiple configuration values retrieved");
        }

        String salt = saltConfig.get(0).getValue();

        if (hashedPasswordUtil.passwordEqualToHash(basicCredentials.getPassword(), userEntity.getPassword(), salt)) {
            return Optional.of(userEntity);
        }

        return Optional.empty();
    }
}
