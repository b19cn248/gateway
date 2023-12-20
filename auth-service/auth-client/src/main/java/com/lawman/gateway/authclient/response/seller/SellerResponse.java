package com.lawman.gateway.authclient.response.seller;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lawman.gateway.authclient.response.address.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class SellerResponse {
  private String id;
  private String brandName;
  private String username;
  private String fullName;
  private String phoneNumber;
  private String email;
  private String dob;
  private AddressResponse address;

  public SellerResponse(String id, String brandName, String username, String fullName, String phoneNumber, String email, String dob) {
    this.id = id;
    this.brandName = brandName;
    this.username = username;
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.dob = dob;
  }
}
