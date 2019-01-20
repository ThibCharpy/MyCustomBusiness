package com.dev.mcb.health;

import com.codahale.metrics.health.HealthCheck;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

public class DatabaseHealthCheck extends HealthCheck {

    private final Jdbi dbi;
    private final String validationQuery;

    public DatabaseHealthCheck(Jdbi dbi, String validationQuery) {
        this.dbi = dbi;
        this.validationQuery = validationQuery;
    }

    @Override
    protected Result check() throws Exception {
        try {
            final Handle handle = dbi.open();
            handle.execute(validationQuery);
            handle.close();
        } catch (Exception e) {
            return Result.unhealthy("Database is not running!");
        }

        return Result.healthy();
    }
}
