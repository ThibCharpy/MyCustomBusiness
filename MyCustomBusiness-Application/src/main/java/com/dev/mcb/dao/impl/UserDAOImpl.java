package com.dev.mcb.dao.impl;

import com.dev.mcb.core.UserEntity;
import com.dev.mcb.dao.UserDAO;
import io.dropwizard.hibernate.AbstractDAO;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDAOImpl extends AbstractDAO<UserEntity> implements UserDAO {

    private final static Logger LOGGER = Logger.getLogger(UserDAOImpl.class);

    public UserDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public UserEntity findById(Long id) {
        LOGGER.debug("Getting user entity with id: "+id);
        return get(id);
    }

    @Override
    public UserEntity create(UserEntity user) {
        LOGGER.debug("Creating new entity: "+user);
        return persist(user);
    }

    @Override
    public UserEntity update(UserEntity user) {
        LOGGER.debug("Updating entity: "+user);
        return persist(user);
    }

    @Override
    public void delete(long userId) {
        LOGGER.debug("Deleting entity with id: "+userId);
        Optional.ofNullable(findById(userId)).ifPresent(userEntity -> currentSession().delete(userEntity));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UserEntity> findAll() {
        LOGGER.debug("Getting every users");
        return list((Query<UserEntity>) namedQuery("com.dev.mcb.core.UserEntity.findAll"));
    }
}
