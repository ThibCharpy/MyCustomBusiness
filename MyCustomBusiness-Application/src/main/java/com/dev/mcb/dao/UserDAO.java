package com.dev.mcb.dao;

import com.dev.mcb.core.UserEntity;

import java.util.List;

public interface UserDAO {

    /**
     * Retrieve a user by its id
     * @param id user id
     * @return the User entity
     */
    UserEntity findById(Long id);

    /**
     * Retrieve a user by its email
     * @param email user email
     * @return the User entity
     */
    UserEntity findByEmail(String email);

    /**
     * Create a new user
     * @param user the new user
     * @return the created user
     */
    UserEntity create(UserEntity user);

    /**
     * Update the user in parameter
     * @param user the new user
     * @return the updated user
     */
    UserEntity update(UserEntity user);

    /**
     * Delete a user by its id
     * @param userId the user id
     */
    void delete(long userId);

    /**
     * Retrieve every user
     * @return the list of users
     */
    List<UserEntity> findAll();

}
