package com.dev.mcb;

import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.dao.impl.UserDAOImpl;
import com.dev.mcb.mapper.UserMapper;
import com.dev.mcb.util.HashedPasswordUtil;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

public class MyCustomBusinessBinder extends AbstractBinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyCustomBusinessBinder.class);

    private MyCustomBusinessConfiguration configuration;

    private final Environment environment;

    private final SessionFactory sessionFactory;

    public MyCustomBusinessBinder(MyCustomBusinessConfiguration configuration, Environment environment, SessionFactory sessionFactory) {
        this.configuration = configuration;
        this.environment = environment;
        this.sessionFactory = sessionFactory;
    }

    @Override
    protected void configure() {
        LOGGER.info("Starting application binding");

        bind(configuration);

        UserDAO userDAO = new UserDAOImpl(this.sessionFactory);
        bind(userDAO).to(UserDAO.class);

        bind(UserMapper.class).to(UserMapper.class).in(Singleton.class);

        bind(HashedPasswordUtil.class).to(HashedPasswordUtil.class);

        LOGGER.info("End binding for service application");
    }
}
