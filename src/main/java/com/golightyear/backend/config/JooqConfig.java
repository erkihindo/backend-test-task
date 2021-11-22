package com.golightyear.backend.config;

import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @PostConstruct
    void init() {
        System.getProperties().setProperty("org.jooq.no-logo", "true");
    }

}
