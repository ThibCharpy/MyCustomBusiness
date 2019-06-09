package com.dev.mcb.dao;

import com.dev.mcb.core.UserAuthToken;

public interface UserAuthTokenDAO {

    /**
     * Retrieve a user by its token
     * @param token user token
     * @return the {@link UserAuthToken} entity
     */
    UserAuthToken findByToken(String token);

    /**
     * Insert a new user's token
     * @param userAuthToken a {@link UserAuthToken} entity
     * @return the inserted {@link UserAuthToken} entity
     */
    UserAuthToken insert(UserAuthToken userAuthToken);

    /**
     * Delete a user by its id
     * @param token the token to suppress
     */
    void delete(String token);
}
