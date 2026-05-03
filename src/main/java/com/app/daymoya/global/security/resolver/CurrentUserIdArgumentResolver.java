package com.app.daymoya.global.security.resolver;

import com.app.daymoya.global.exception.BizException;
import com.app.daymoya.global.security.annotation.CurrentUserId;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.app.daymoya.global.exception.ErrorCode.INVALID_SECURITY_CONTEXT;

@Component
public class CurrentUserIdArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CurrentUserId.class)
      && Long.class.equals(parameter.getParameterType());
  }

  @Override
  public @Nullable Object resolveArgument(
    MethodParameter parameter,
    @Nullable ModelAndViewContainer mavContainer,
    NativeWebRequest webRequest,
    @Nullable WebDataBinderFactory binderFactory
  ) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new BizException(INVALID_SECURITY_CONTEXT);
    }

    Object principal = authentication.getPrincipal();

    if (!(principal instanceof Long userId)) {
      throw new BizException(INVALID_SECURITY_CONTEXT);
    }

    return userId;
  }

}
