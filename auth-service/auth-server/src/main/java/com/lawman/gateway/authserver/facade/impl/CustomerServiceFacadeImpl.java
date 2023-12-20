package com.lawman.gateway.authserver.facade.impl;

import com.lawman.gateway.authclient.request.account.AccountRequest;
import com.lawman.gateway.authclient.request.account.RoleAssign;
import com.lawman.gateway.authclient.request.customer.CustomerRequest;
import com.lawman.gateway.authclient.response.account.AccountResponse;
import com.lawman.gateway.authclient.response.customer.CustomerResponse;
import com.lawman.gateway.authserver.exception.seller.SellerPhoneNumberNotFoundException;
import com.lawman.gateway.authserver.facade.AddressFacadeService;
import com.lawman.gateway.authserver.facade.CustomerFacadeService;
import com.lawman.gateway.authserver.service.AccountService;
import com.lawman.gateway.authserver.service.CustomerService;
import com.lawman.gateway.authserver.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class CustomerServiceFacadeImpl implements CustomerFacadeService {

  private final AccountService accountService;
  private final CustomerService customerService;
  private final SellerService sellerService;
  private final AddressFacadeService addressFacadeService;

  @Override
  @Transactional
  public CustomerResponse create(CustomerRequest request) {
    log.info("(create) request:{}", request);

    checkSellerPhoneNumberNotFound(request.getSellerPhoneNumber());

    AccountRequest accountRequest = new AccountRequest(
          request.getUsername(),
          request.getPassword(),
          request.getConfirmPassword()
    );

    AccountResponse accountResponse = accountService.create(accountRequest, RoleAssign.CUSTOMER.toString());

    CustomerResponse customerResponse = customerService.create(request, accountResponse.getId());
    customerResponse.setUsername(accountResponse.getUsername());
    customerResponse.setCustomerAddress(addressFacadeService.create(request.getAddress()));

    return customerResponse;
  }

  private void checkSellerPhoneNumberNotFound(String phoneNumber) {
    log.info("(checkSellerPhoneNumberNotFound) phoneNumber:{}", phoneNumber);
    if (!sellerService.existsByPhoneNumber(phoneNumber)) {
      log.error("(checkSellerPhoneNumberNotFound) ====================> SellerPhoneNumberNotFoundException");
      throw new SellerPhoneNumberNotFoundException();
    }
  }
}
