package com.lawman.gateway.authserver.facade;

import com.lawman.gateway.authclient.request.account.AccountRequest;
import com.lawman.gateway.authclient.request.account.RoleAssign;
import com.lawman.gateway.authclient.request.customer.CustomerRequest;
import com.lawman.gateway.authclient.response.account.AccountResponse;
import com.lawman.gateway.authclient.response.customer.CustomerResponse;
import com.lawman.gateway.authserver.configuration.GatewayConfigurationTest;
import com.lawman.gateway.authserver.exception.seller.SellerPhoneNumberNotFoundException;
import com.lawman.gateway.authserver.service.AccountService;
import com.lawman.gateway.authserver.service.CustomerService;
import com.lawman.gateway.authserver.service.SellerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(CustomerFacadeService.class)
@ContextConfiguration(classes = {GatewayConfigurationTest.class})
class CustomerFacadeServiceTests {

  @MockBean
  private SellerService sellerService;

  @MockBean
  private CustomerService customerService;

  @MockBean
  private AccountService accountService;

  @MockBean
  private AddressFacadeService addressFacadeService;

  @Autowired
  private CustomerFacadeService customerFacadeService;

  private static final String UID = UUID.randomUUID().toString();

  @Test
  void createCustomer_WhenCreateSuccess_ReturnCustomer() {
    CustomerRequest request = mockRequest();
    AccountRequest accountRequest = AccountRequest.builder()
          .username(request.getUsername())
          .password(request.getPassword())
          .confirmPassword(request.getConfirmPassword()).build();

    AccountResponse accountResponse = mockAccountResponse();

    Mockito.when(accountService.create(accountRequest, RoleAssign.CUSTOMER.toString())).thenReturn(accountResponse);
    Mockito.when(sellerService.existsByPhoneNumber(request.getSellerPhoneNumber())).thenReturn(true);
    Mockito.when(customerService.create(request, accountResponse.getId())).thenReturn(mockCustomerResponse());
    Mockito.when(addressFacadeService.create(request.getAddress())).thenReturn(null);


    CustomerResponse actual = customerFacadeService.create(request);
    CustomerResponse expected = mockCustomerResponse();

    check(actual, expected);

  }

  @Test
  void createCustomer_WhenSellerPhoneNumberNotFound_ThrowSellerPhoneNumberNotFoundException() {
    CustomerRequest request = mockRequest();
    AccountRequest accountRequest = AccountRequest.builder()
          .username(request.getUsername())
          .password(request.getPassword())
          .confirmPassword(request.getConfirmPassword()).build();

    AccountResponse accountResponse = mockAccountResponse();

    Mockito.when(accountService.create(accountRequest, RoleAssign.CUSTOMER.toString())).thenReturn(accountResponse);
    Mockito.when(sellerService.existsByPhoneNumber(request.getSellerPhoneNumber())).thenReturn(false);
    Mockito.when(customerService.create(request, accountResponse.getId())).thenReturn(mockCustomerResponse());

    Assertions.assertThrows(SellerPhoneNumberNotFoundException.class, () -> customerFacadeService.create(request));

  }


  private CustomerRequest mockRequest() {
    return CustomerRequest.builder()
          .sellerPhoneNumber("0987333222")
          .username("hieuPTIT")
          .password("Hieu230708@")
          .confirmPassword("Hieu230708@")
          .fullName("Nguyen Minh Hieu")
          .phoneNumber("0987654321")
          .email("hieunm123.ptit@gmail.com")
          .dob("23/08/2001")
          .build();
  }

  private CustomerResponse mockCustomerResponse() {
    return CustomerResponse.builder()
          .id(UID)
          .status(1)
          .sellerPhoneNumber("0987333222")
          .username("hieuPTIT")
          .fullName("Nguyen Minh Hieu")
          .phoneNumber("0987654321")
          .email("hieunm123.ptit@gmail.com")
          .dob("23/08/2001")
          .build();
  }

  private AccountResponse mockAccountResponse() {
    return AccountResponse.builder()
          .id(UID)
          .username("hieuPTIT")
          .activated(true)
          .build();
  }

  private void check(CustomerResponse actual, CustomerResponse expect) {
    assertThat(actual.getId()).isEqualTo(expect.getId());
    assertThat(actual.getFullName()).isEqualTo(expect.getFullName());
    assertThat(actual.getPhoneNumber()).isEqualTo(expect.getPhoneNumber());
    assertThat(actual.getEmail()).isEqualTo(expect.getEmail());
    assertThat(actual.getStatus()).isEqualTo(expect.getStatus());
    assertThat(actual.getSellerPhoneNumber()).isEqualTo(expect.getSellerPhoneNumber());
  }


}
