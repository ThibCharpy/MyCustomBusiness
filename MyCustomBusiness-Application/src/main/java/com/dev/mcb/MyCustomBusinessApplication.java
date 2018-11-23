package com.dev.mcb;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MyCustomBusinessApplication extends Application<MyCustomBusinessConfiguration> {

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
        return "My Custom Business !";
    }

    @Override
    public void initialize(Bootstrap<MyCustomBusinessConfiguration> bootstrap) {
        bootstrap.addBundle(this.hibernate);
    }

    @Override
    public void run(MyCustomBusinessConfiguration myCustomBusinessConfiguration,
                    Environment environment) throws Exception {

    }
}
