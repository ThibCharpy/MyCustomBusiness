package com.dev.mcb.dao;

import com.dev.mcb.core.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    /**
     * Retrieve a user by its id
     * @param id user id
     * @return the User entity
     */
    public Optional<UserEntity> findById(Long id);

    /**
     * Create a new user
     * @param user the new user
     * @return the created user
     */
    public UserEntity create(UserEntity user);

    /**
     * Update the user in parameter
     * @param user the new user
     * @return the updated user
     */
    public UserEntity update(UserEntity user);

    /**
     * Delete a user by its id
     * @param userId the user id
     */
    public void delete(long userId);

    /**
     * Retrieve every user
     * @return the list of users
     */
    public List findAll();

}
