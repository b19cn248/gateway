package com.lawman.gateway.authclient.request.customer;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class CustomerUpdateRequest {
  private String sellerPhoneNumber;
  private String fullName;
  private String phoneNumber;
  private String email;
  private String dob;
}
