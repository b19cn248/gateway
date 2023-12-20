package com.lawman.gateway.authserver.configuration.auditor;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CommonConstants.ANONYMOUS;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CommonConstants.SYSTEM;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {
  @Override
  public @NonNull Optional<String> getCurrentAuditor() {

    log.info("Da toi ham audit");

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (Objects.nonNull(authentication) && !this.isAnonymous()) {
      return Optional.ofNullable(authentication.getPrincipal().toString());
    }
    return Optional.of(SYSTEM);
  }

  private boolean isAnonymous() {
    return SecurityContextHolder.getContext().getAuthentication().getName().equals(ANONYMOUS);
  }
}

