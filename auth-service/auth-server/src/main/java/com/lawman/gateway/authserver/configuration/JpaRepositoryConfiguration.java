package com.lawman.gateway.authserver.configuration;

import com.lawman.gateway.authserver.configuration.auditor.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
      basePackages = "com.lawman.gateway.authserver"
)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaRepositoryConfiguration {

  @Bean
  public AuditorAware<String> auditorProvider() {
    return new AuditorAwareImpl();
  }
}
