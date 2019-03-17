package com.dev.mcb;

import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.dao.impl.UserDAOImpl;
import com.dev.mcb.filter.CorsServletFilter;
import com.dev.mcb.mapper.UserMapper;
import com.dev.mcb.resource.UserResource;
import com.dev.mcb.util.HashedPasswordUtil;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zone.dragon.dropwizard.HK2Bundle;

import javax.inject.Singleton;
import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class MyCustomBusinessApplication extends Application<MyCustomBusinessConfiguration> {

    //TODO: store encrypted passwords
    //TODO: add tests
    //TODO: make the password not editable
    //TODO: add oauth integration
    //TODO create the login/lgout resources
    //TODO: update the database either with mysql patches or liquibase patches

    private static final Logger LOGGER = LoggerFactory.getLogger(MyCustomBusinessApplication.class);

    private static final String APPLICATION_NAME = "My Custom Business";

    public static void main(String[] args) throws Exception {
        new MyCustomBusinessApplication().run(args);
    }

    private final HibernateBundle<MyCustomBusinessConfiguration> hibernate =
            new ScanningHibernateBundle<MyCustomBusinessConfiguration>(
                    "com.dev.mcb.core"
            ) {
                @Override
                public DataSourceFactory getDataSourceFactory(MyCustomBusinessConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return APPLICATION_NAME;
    }

    @Override
    public void initialize(Bootstrap<MyCustomBusinessConfiguration> bootstrap) {
        bootstrap.addBundle(this.hibernate);
        HK2Bundle.addTo(bootstrap);
    }

    @Override
    public void run(MyCustomBusinessConfiguration configuration,
                    Environment environment) {
        LOGGER.info("Start application !");

        // DAOs
        //final UserDAO userDAO = new UserDAOImpl(hibernate.getSessionFactory());

        environment.jersey().register(new MyCustomBusinessBinder(configuration, environment, hibernate.getSessionFactory()));

        // Resources
        //environment.jersey().register(new UserResource(userDAO));
        environment.jersey().register(new UserResource());

        // Filters
        environment.servlets().addFilter("CorsServletFilter", new CorsServletFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    }
}
