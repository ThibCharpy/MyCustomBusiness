package com.dev.mcb.dao;

import com.dev.mcb.core.User;

import java.util.List;

public interface UserDAO {

    /**
     * Create a user
     * @param user the user dto to create
     */
    public void createUser(User user);

    /**
     * Get a user by its id
     * @param user_id the customer id
     * @return the customer with id user_id as dto
     */
    public User readUser(Long user_id);

    /**
     * Update a user
     * @param user the new user datas
     */
    public boolean updateUser(User user);

    /**
     * Delete a user by its id
     * @param user the user to delete
     */
    public boolean deleteUser(User user);

    /**
     * Get all user in database
     * @return list of user
     */
    public List listUser();
}
