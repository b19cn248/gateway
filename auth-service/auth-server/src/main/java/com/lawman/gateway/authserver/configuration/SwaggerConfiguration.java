package com.lawman.gateway.authserver.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.lawman.gateway.authclient.constant.AuthClientConstants.SwaggerConstant.*;

@Configuration
public class SwaggerConfiguration {

  private SecurityScheme createAPIKeyScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.HTTP)
          .bearerFormat(JWT)
          .scheme(TOKEN_TYPE);
  }

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI().addSecurityItem(new SecurityRequirement().
                addList(AUTHENTICATION))
          .components(new Components().addSecuritySchemes
                (TITLE, createAPIKeyScheme()))
          .info(new Info().title(TITLE)
                .description(DESCRIPTION));
  }
}
