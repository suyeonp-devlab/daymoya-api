package com.app.daymoya.global.security.resolver;

import com.app.daymoya.global.exception.CustomException;
import com.app.daymoya.global.security.annotation.CurrentUserId;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;

import static com.app.daymoya.global.exception.ErrorCode.INVALID_SECURITY_CONTEXT;

@Component
public class CurrentUserIdArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CurrentUserId.class)
      && Long.class.equals(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter
                               ,ModelAndViewContainer mavContainer
                               ,NativeWebRequest webRequest
                               ,WebDataBinderFactory binderFactory) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      throw new CustomException(INVALID_SECURITY_CONTEXT);
    }

    Object principal = authentication.getPrincipal();

    if (principal == null || "anonymousUser".equals(principal)) {
      throw new CustomException(INVALID_SECURITY_CONTEXT);
    }

    if (!(principal instanceof Long userId)) {
      throw new CustomException(INVALID_SECURITY_CONTEXT);
    }

    return userId;
  }

}