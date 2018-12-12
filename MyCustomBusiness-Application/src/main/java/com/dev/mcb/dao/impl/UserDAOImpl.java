package com.dev.mcb.dao.impl;

import com.dev.mcb.core.User;
import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.util.HibernateUtil;
import io.dropwizard.hibernate.AbstractDAO;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl extends AbstractDAO<UserDAO> {

    private Session session;

    private final static Logger logger = Logger.getLogger(UserDAOImpl.class);

    public UserDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
