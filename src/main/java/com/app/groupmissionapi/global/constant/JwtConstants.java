package com.app.groupmissionapi.global.constant;

public final class JwtConstants {

  private JwtConstants() {}

  public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
  public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

  public static final String EMAIL_CLAIM = "email";
  public static final String TOKEN_TYPE_CLAIM = "tokenType";

  public static final String ACCESS_TOKEN_TYPE = "ACCESS";
  public static final String REFRESH_TOKEN_TYPE = "REFRESH";

  public static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

}
