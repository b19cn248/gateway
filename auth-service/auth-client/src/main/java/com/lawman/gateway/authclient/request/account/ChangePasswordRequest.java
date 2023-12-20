package com.lawman.gateway.authclient.request.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lawman.gateway.authclient.validation.account.ConfirmPasswordValidation;
import com.lawman.gateway.authclient.validation.account.PasswordValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ConfirmPasswordValidation(originalField = "newPassword", confirmationField = "confirmPassword")
@Builder
public class ChangePasswordRequest {

  @PasswordValidation
  private String password;
  @PasswordValidation
  private String newPassword;
  @PasswordValidation
  private String confirmPassword;
}
