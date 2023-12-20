package com.lawman.gateway.authserver.utils;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthConstant.EMAIL_PARAM;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthConstant.FORGOT_PASSWORD_URL;

public interface GenerateUtils {

  static String generateUrlForgotPassword(String token, String email) {
    return FORGOT_PASSWORD_URL + token + EMAIL_PARAM + email;
  }
}
