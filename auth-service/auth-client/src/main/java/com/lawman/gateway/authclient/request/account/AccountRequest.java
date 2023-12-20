package com.lawman.gateway.authclient.request.account;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {
  private String username;
  private String password;

  @NotNull
  @Length(min = 6, max = 64)
  private String confirmPassword;
}
