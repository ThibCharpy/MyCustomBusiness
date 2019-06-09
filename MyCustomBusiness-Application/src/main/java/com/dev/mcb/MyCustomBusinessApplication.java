package com.dev.mcb;

import com.dev.mcb.auth.UserAuthenticator;
import com.dev.mcb.auth.UserAuthorizer;
import com.dev.mcb.core.UserEntity;
import com.dev.mcb.filter.CORSServletFilter;
import com.dev.mcb.resource.LoginResource;
import com.dev.mcb.resource.UserResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zone.dragon.dropwizard.HK2Bundle;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class MyCustomBusinessApplication extends Application<MyCustomBusinessConfiguration> {

    //TODO: make the password not editable
    //TODO: update the database either with mysql patches or liquibase patches
    //TODO: add tests

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

        environment.jersey().register(new MyCustomBusinessBinder(configuration, environment, hibernate.getSessionFactory()));

        setUpAuth(configuration, environment);

        // Resource
        environment.jersey().register(new UserResource());
        environment.jersey().register(new LoginResource());

        // Filters
        environment.servlets().addFilter("CORSServletFilter", new CORSServletFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    }

    private void setUpAuth(MyCustomBusinessConfiguration configuration, Environment environment) {
        AuthFilter basicCredentialAuthFilter = new BasicCredentialAuthFilter.Builder<UserEntity>()
                .setAuthenticator(new UserAuthenticator())
                .setAuthorizer(new UserAuthorizer())
                .setRealm("SUPER SECRET STUFF")
                .buildAuthFilter();

        environment.jersey().register(new AuthDynamicFeature(basicCredentialAuthFilter));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(UserEntity.class));
    }
}
