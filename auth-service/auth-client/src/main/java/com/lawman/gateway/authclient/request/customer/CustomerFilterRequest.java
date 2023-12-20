package com.lawman.gateway.authclient.request.customer;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerFilterRequest {
  private Integer status;
  private String username;
  private String fullName;
  private String phoneNumber;
  private String sellerPhoneNumber;
  private String province;
  private String district;
  private String wards;
}
