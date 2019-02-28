package com.dev.mcb;

import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.dao.impl.UserDAOImpl;
import com.dev.mcb.filter.CorsServletFilter;
import com.dev.mcb.mapper.UserMapper;
import com.dev.mcb.resource.UserResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import zone.dragon.dropwizard.HK2Bundle;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class MyCustomBusinessApplication extends Application<MyCustomBusinessConfiguration> {

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

        // DAOs
        final UserDAO userDAO = new UserDAOImpl(hibernate.getSessionFactory());

        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                // Mappers
                bind(new UserMapper()).to(UserMapper.class);
            }
        });

        // Resources
        environment.jersey().register(new UserResource(userDAO));

        environment.servlets().addFilter("CorsServletFilter", new CorsServletFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    }
}
