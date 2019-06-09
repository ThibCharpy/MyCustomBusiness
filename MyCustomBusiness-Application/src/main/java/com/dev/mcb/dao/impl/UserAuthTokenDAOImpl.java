package com.dev.mcb.dao.impl;

import com.dev.mcb.core.UserAuthToken;
import com.dev.mcb.dao.UserAuthTokenDAO;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Optional;

public class UserAuthTokenDAOImpl extends AbstractDAO<UserAuthToken> implements UserAuthTokenDAO {

    public UserAuthTokenDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserAuthToken findByToken(String token) {
        return uniqueResult((Query<UserAuthToken>) namedQuery("com.dev.mcb.core.UserEntity.findByToken")
                .setParameter("token", token));
    }

    @Override
    public UserAuthToken insert(UserAuthToken userAuthToken) {
        return persist(userAuthToken);
    }

    @Override
    public void delete(String token) {
        Optional.ofNullable(findByToken(token)).ifPresent(userAuthToken -> currentSession().delete(userAuthToken));
    }
}
