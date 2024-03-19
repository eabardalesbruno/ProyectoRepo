package com.proriberaapp.ribera;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class R2dbcConfiguration {

    @Value("${spring.r2dbc.url}")
    private String r2dbcUrl;

    @Value("${spring.r2dbc.username}")
    private String r2dbcUsername;

    @Value("${spring.r2dbc.password}")
    private String r2dbcPassword;

    @Value("${spring.r2dbc.schema}")
    private String r2dbcSchema;

    //@Bean
    public PostgresqlConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host("postgres.inclub.world")
                        .port(5432)
                        .database("ribera")
                        .username(r2dbcUsername)
                        .password(r2dbcPassword)
                        .schema(r2dbcSchema)
                        .build()
        );
    }
}