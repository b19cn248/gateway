package com.lawman.gateway.authclient.response.customer;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerInfo {
  private String id;
  private String customerName;
  private String customerPhoneNumber;

  public CustomerInfo(String id, String customerName, String customerPhoneNumber) {
    this.id = id;
    this.customerName = customerName;
    this.customerPhoneNumber = customerPhoneNumber;
  }
}
