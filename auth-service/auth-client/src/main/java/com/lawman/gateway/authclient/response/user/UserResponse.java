package com.lawman.gateway.authclient.response.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
  private String id;
  private String username;
  private String fullName;
  private String phoneNumber;
  private String email;
  private String dob;

  public UserResponse(String id, String username, String fullName, String phoneNumber, String email, String dob) {
    this.id = id;
    this.username = username;
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.dob = dob;
  }
}
