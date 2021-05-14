package com.epam.esm.persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EntityScan("com.epam.esm.persistence.model.entity")
@EnableJpaAuditing
public class JpaConfig {
}
