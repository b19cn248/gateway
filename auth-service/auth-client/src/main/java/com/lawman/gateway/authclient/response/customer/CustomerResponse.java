package com.lawman.gateway.authclient.response.customer;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lawman.gateway.authclient.response.address.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerResponse {
  private String id;
  private Double bonusCoin;
  private Integer status;
  private String sellerPhoneNumber;
  private String username;
  private String fullName;
  private String phoneNumber;
  private String email;
  private String dob;
  private AddressResponse customerAddress;

  public CustomerResponse(
        String id,
        Double bonusCoin,
        Integer status,
        String sellerPhoneNumber,
        String username,
        String fullName,
        String phoneNumber,
        String email,
        String dob
  ) {
    this.id = id;
    this.bonusCoin = bonusCoin;
    this.status = status;
    this.sellerPhoneNumber = sellerPhoneNumber;
    this.username = username;
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.dob = dob;
  }
}
