package com.dev.mcb;

import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.dao.impl.UserDAOImpl;
import com.dev.mcb.mapper.UserMapper;
import com.dev.mcb.resource.LoginResource;
import com.dev.mcb.resource.UserResource;
import com.dev.mcb.util.service.UserService;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class MyCustomBusinessApplication extends Application<MyCustomBusinessConfiguration> {

    private static final String APPLICATION_NAME = "My Custom Business";

    private final HibernateBundle<MyCustomBusinessConfiguration> hibernate =
            new HibernateBundle<MyCustomBusinessConfiguration>(User.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(MyCustomBusinessConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(String[] args) throws Exception {
        new MyCustomBusinessApplication().run(args);
    }

    @Override
    public String getName() {
        return APPLICATION_NAME;
    }

    @Override
    public void initialize(Bootstrap<MyCustomBusinessConfiguration> bootstrap) {
        bootstrap.addBundle(this.hibernate);
    }

    @Override
    public void run(MyCustomBusinessConfiguration myCustomBusinessConfiguration,
                    Environment environment) throws Exception {

        // DAOs
        final UserDAO userDAO = new UserDAOImpl(hibernate.getSessionFactory());

        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {

                // Services
                bind(new UserService()).to(UserService.class);

                // Mappers
                bind(new UserMapper()).to(UserMapper.class);

                // DAOs
                bind(userDAO).to(UserDAO.class);
            }
        });

        environment.jersey().register(LoginResource.class);
        environment.jersey().register(UserResource.class);
    }
}
