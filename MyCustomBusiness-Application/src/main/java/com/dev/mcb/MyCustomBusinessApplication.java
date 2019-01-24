package com.dev.mcb;

import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.dao.impl.UserDAOImpl;
import com.dev.mcb.health.DatabaseHealthCheck;
import com.dev.mcb.mapper.UserMapper;
import com.dev.mcb.resource.LoginResource;
import com.dev.mcb.resource.UserResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.jdbi.v3.core.Jdbi;
import zone.dragon.dropwizard.HK2Bundle;

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
                    Environment environment) throws Exception {


        //final JdbiFactory factory = new JdbiFactory();
        //final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");

        // DAOs
        //final UserDAO userDAO = new UserDAOImpl(hibernate.getSessionFactory());

        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                // Mappers
                bind(new UserMapper()).to(UserMapper.class);
            }
        });

        environment.jersey().register(new UserResource(new UserDAOImpl(hibernate.getSessionFactory())));

        //environment.healthChecks().register("health",
                //new DatabaseHealthCheck(jdbi, configuration.getDataSourceFactory().getValidationQuery()));
    }
}
