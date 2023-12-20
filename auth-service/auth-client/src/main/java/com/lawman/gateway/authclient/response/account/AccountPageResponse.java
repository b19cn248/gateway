package com.lawman.gateway.authclient.response.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor(staticName = "of")
public class AccountPageResponse {
  private List<AccountResponse> accounts;
  private int countAccounts;
}
