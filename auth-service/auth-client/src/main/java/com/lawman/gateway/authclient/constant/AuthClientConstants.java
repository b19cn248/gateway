package com.lawman.gateway.authclient.constant;

public class AuthClientConstants {
  private AuthClientConstants() {

  }

  public static class ValidationMessage {

    private ValidationMessage() {

    }

    public static final String CONFIRM_PASSWORD_NOT_MATCH = "Confirm password does not match";
    public static final String WRONG_FORMAT_PASSWORD = "Wrong format password";
    public static final String INVALID_USERNAME = "Invalid Username";
    public static final String INVALID_DATE_FORMAT = "Invalid date format";
    public static final String INVALID_EMAIL = "Invalid email";
    public static final String INVALID_PHONE_NUMBER = "Invalid phone Number";

  }

  public static class SwaggerConstant {
    private SwaggerConstant() {
    }

    public static final String JWT = "JWT";
    public static final String TOKEN_TYPE = "bearer";
    public static final String AUTHENTICATION = "Bearer Authentication";
    public static final String TITLE = "API Gateway";
    public static final String DESCRIPTION = "This document describe the api of gateway";
  }
}
