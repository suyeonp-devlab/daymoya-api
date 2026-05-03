package com.app.daymoya.global.security;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityAuditorAware implements AuditorAware<Long> {

  private static final Long SYSTEM_USER_ID = 0L;

  @NonNull
  @Override
  public Optional<Long> getCurrentAuditor() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.of(SYSTEM_USER_ID);
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof Long userId) {
      return Optional.of(userId);
    }

    return Optional.of(SYSTEM_USER_ID);
  }

}
