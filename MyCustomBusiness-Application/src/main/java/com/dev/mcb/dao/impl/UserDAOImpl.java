package com.dev.mcb.dao.impl;

import com.dev.mcb.core.UserEntity;
import com.dev.mcb.dao.UserDAO;
import io.dropwizard.hibernate.AbstractDAO;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDAOImpl extends AbstractDAO<UserEntity> implements UserDAO {

    private final static Logger logger = Logger.getLogger(UserDAOImpl.class);

    public UserDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public UserEntity findById(Long id) {
        return get(id);
    }

    @Override
    public long create(UserEntity user) {
        return persist(user).getId();
    }

    @Override
    public UserEntity update(UserEntity user) {
        //TODO: to be implemented
        return null;
    }

    @Override
    public void delete(long userId) {
        //TODO: to be implemented
    }

    @Override
    public List<UserEntity> findAll() {
        return list(namedQuery("com.dev.mcb.core.User.findAll"));
    }
}
