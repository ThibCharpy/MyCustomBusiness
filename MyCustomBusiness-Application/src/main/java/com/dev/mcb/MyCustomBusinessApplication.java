package com.dev.mcb;

import com.dev.mcb.resource.LoginResource;
import com.dev.mcb.resource.UserResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        environment.jersey().register(LoginResource.class);
        environment.jersey().register(UserResource.class);
    }
}
