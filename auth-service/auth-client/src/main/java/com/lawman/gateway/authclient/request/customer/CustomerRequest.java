package com.lawman.gateway.authclient.request.customer;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lawman.gateway.authclient.request.address.AddressRequest;
import com.lawman.gateway.authclient.validation.account.ConfirmPasswordValidation;
import com.lawman.gateway.authclient.validation.account.PasswordValidation;
import com.lawman.gateway.authclient.validation.account.UsernameValidation;
import com.lawman.gateway.authclient.validation.user.DateformatValidation;
import com.lawman.gateway.authclient.validation.user.EmailValidation;
import com.lawman.gateway.authclient.validation.user.PhoneNumberValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@ConfirmPasswordValidation(originalField = "password", confirmationField = "confirmPassword")
public class CustomerRequest {
  private String sellerPhoneNumber;
  @UsernameValidation
  private String username;

  @PasswordValidation
  private String password;
  @PasswordValidation
  private String confirmPassword;
  private String fullName;
  @PhoneNumberValidation
  private String phoneNumber;
  @EmailValidation
  private String email;
  @DateformatValidation
  private String dob;
  private AddressRequest address;
}
