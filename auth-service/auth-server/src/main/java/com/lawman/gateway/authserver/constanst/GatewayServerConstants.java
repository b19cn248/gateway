package com.lawman.gateway.authserver.constanst;

public class GatewayServerConstants {

  private GatewayServerConstants() {
  }

  public static class CommonConstants {

    private CommonConstants() {
    }

    public static final String ENCODING_UTF_8 = "UTF-8";

    public static final String LANGUAGE = "Accept-Language";
    public static final String DEFAULT_LANGUAGE = "en";

    public static final String SUCCESS = "success";

    public static final String MESSAGE_I18N_PATH = "classpath:i18n/messages";

    public static final String SYSTEM = "SYSTEM";
    public static final String ANONYMOUS = "ANONYMOUS";
  }

  public static class AuthConstant {

    private AuthConstant() {
    }

    public static final String TYPE_TOKEN = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final int AUTHORIZATION_TYPE_SIZE = 7;
    public static final String INVALID_TOKEN = "Token is invalid";
    public static final String EXPIRED_TOKEN = "Token is expired";

    public static final String CLAIMS = "claims";

    public static final String UN_CHECKED = "unchecked";
    public static final String REFRESH_TOKEN = "RefreshToken";

    public static final String[] REQUEST_WITHOUT_AUTHENTICATION = {
          "/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**"
    };

    public static final int MAX_FAILED_ATTEMPTS = 3;

    public static final Long LOCK_TIME = (long) (60 * 1000);

    public static final String FORGOT_PASSWORD_URL = "http://localhost:8081/api/v1/auth/reset-password?token=";

    public static final String EMAIL_PARAM = "&email=";

    public static final String FROM_EMAIL = "hieunm123.ptit@gmail.com";

    public static final String EMAIL_TITLE = "Shop Sport Support";

    public static final String SUBJECT = "Here's the link to reset your password";

    public static final String LINK = "link";
  }

  public static class AccountMessage {
    private AccountMessage() {

    }

    public static final String CHANGE_PASSWORD_SUCCESS = "com.gateway.server.controller.AccountController.changPassword";
  }

  public static class SellerMessage {
    private SellerMessage() {

    }

    public static final String UPDATE_SELLER_SUCCESS = "com.gateway.server.controller.SellerController.update";
  }

  public static class CustomerMessage {

    private CustomerMessage() {
    }

    public static final String CREATE_CUSTOMER_SUCCESS = "com.gateway.server.controller.CustomerController.create";
    public static final String CREATE_SELLER_SUCCESS = "com.gateway.server.controller.CustomerController.create";
  }

  public static class ExceptionMessage {
    private ExceptionMessage() {

    }

    public static final String ACCOUNT_NOT_FOUND_EXCEPTION = "com.gateway.server.exception.account.AccountNotFoundException";

    public static final String PASSWORD_INCORRECT_EXCEPTION = "com.gateway.server.exception.account.PasswordIncorrectException";

    public static final String SELLER_PHONE_NUMBER_NOT_FOUND_EXCEPTION = "com.gateway.server.exception.seller.SellerPhoneNumberNotFoundException";

    public static final String EMAIL_ALREADY_EXISTED_EXCEPTION = "com.gateway.server.exception.customer.EmailAlreadyExistedException";

    public static final String PHONE_NUMBER_ALREADY_EXISTED_EXCEPTION = "com.gateway.server.exception.customer.PhoneNumberAlreadyExistedException";

  }

  public static class AuthenticationConstant {
    private AuthenticationConstant() {
    }

    public static final String REFRESH_TOKEN_KEY = "refresh_token";
    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String CLAIM_USERNAME_KEY = "username";
    public static final String CLAIM_AUTHORITIES_KEY = "role";
  }

}
