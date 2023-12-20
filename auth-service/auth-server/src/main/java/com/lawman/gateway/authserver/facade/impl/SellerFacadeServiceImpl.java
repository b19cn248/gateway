package com.lawman.gateway.authserver.facade.impl;

import com.lawman.gateway.authclient.request.account.AccountRequest;
import com.lawman.gateway.authclient.request.account.RoleAssign;
import com.lawman.gateway.authclient.request.seller.SellerRequest;
import com.lawman.gateway.authclient.response.account.AccountResponse;
import com.lawman.gateway.authclient.response.seller.SellerResponse;
import com.lawman.gateway.authserver.facade.AddressFacadeService;
import com.lawman.gateway.authserver.facade.SellerFacadeService;
import com.lawman.gateway.authserver.service.AccountService;
import com.lawman.gateway.authserver.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class SellerFacadeServiceImpl implements SellerFacadeService {

  private final AccountService accountService;
  private final SellerService sellerService;
  private final AddressFacadeService addressFacadeService;

  @Override
  @Transactional
  public SellerResponse create(SellerRequest request) {
    log.info("(create) request:{}", request);

    AccountRequest accountRequest = new AccountRequest(
          request.getUsername(),
          request.getPassword(),
          request.getConfirmPassword()
    );

    AccountResponse accountResponse = accountService.create(accountRequest, RoleAssign.SELLER.toString());

    SellerResponse sellerResponse = sellerService.create(request, accountResponse.getId());
    sellerResponse.setUsername(accountResponse.getUsername());
    sellerResponse.setAddress(addressFacadeService.create(request.getAddress()));

    return sellerResponse;
  }
}
