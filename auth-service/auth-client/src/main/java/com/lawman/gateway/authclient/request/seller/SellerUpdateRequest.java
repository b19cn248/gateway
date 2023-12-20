package com.lawman.gateway.authclient.request.seller;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SellerUpdateRequest {
  @NotNull
  @Length(min = 1, max = 128)
  private String brandName;

  private String warehouseId;

  @NotNull
  @Length(min = 2, max = 50)
  private String fullName;

  private String phoneNumber;
  private String email;

  private String dob;

  private String addressId;
}
