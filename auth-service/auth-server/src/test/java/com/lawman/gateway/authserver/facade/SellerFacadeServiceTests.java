package com.lawman.gateway.authserver.facade;

import com.lawman.gateway.authclient.request.account.AccountRequest;
import com.lawman.gateway.authclient.request.account.RoleAssign;
import com.lawman.gateway.authclient.request.seller.SellerRequest;
import com.lawman.gateway.authclient.response.account.AccountResponse;
import com.lawman.gateway.authclient.response.seller.SellerResponse;
import com.lawman.gateway.authserver.configuration.GatewayConfigurationTest;
import com.lawman.gateway.authserver.exception.account.UsernameAlreadyExistedException;
import com.lawman.gateway.authserver.exception.customer.EmailAlreadyExistedException;
import com.lawman.gateway.authserver.exception.customer.PhoneNumberAlreadyExistedException;
import com.lawman.gateway.authserver.exception.seller.BrandNameAlreadyExistedException;
import com.lawman.gateway.authserver.service.AccountService;
import com.lawman.gateway.authserver.service.SellerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

@WebMvcTest(CustomerFacadeService.class)
@ContextConfiguration(classes = {GatewayConfigurationTest.class})
class SellerFacadeServiceTests {
  @MockBean
  private SellerService sellerService;

  @MockBean
  private AccountService accountService;

  @MockBean
  private AddressFacadeService addressFacadeService;

  @Autowired
  private SellerFacadeService sellerFacadeService;

  private static final String UID = UUID.randomUUID().toString();

  @Test
  void createSeller_WhenCreateSuccess_ReturnSellerResponse() {
    SellerRequest request = mockRequest();
    AccountRequest accountRequest = AccountRequest.builder()
          .username(request.getUsername())
          .password(request.getPassword())
          .confirmPassword(request.getConfirmPassword()).build();

    AccountResponse accountResponse = mockAccountResponse();

    Mockito.when(accountService.create(accountRequest, RoleAssign.SELLER.toString())).thenReturn(accountResponse);
    Mockito.when(sellerService.create(request, accountResponse.getId())).thenReturn(mockSellerResponse());
    Mockito.when(addressFacadeService.create(request.getAddress())).thenReturn(null);

    SellerResponse actual = sellerFacadeService.create(request);
    SellerResponse expected = mockSellerResponse();

    System.out.println(actual);
    System.out.println(expected);

    Assertions.assertEquals(actual, expected);
  }

  @Test
  void createSeller_WhenEmailAlreadyExisted_ThrowEmailAlreadyExistedException() {
    SellerRequest request = mockRequest();
    AccountRequest accountRequest = AccountRequest.builder()
          .username(request.getUsername())
          .password(request.getPassword())
          .confirmPassword(request.getConfirmPassword()).build();

    AccountResponse accountResponse = mockAccountResponse();

    Mockito.when(accountService.create(accountRequest, RoleAssign.SELLER.toString())).thenReturn(accountResponse);
    Mockito.when(sellerService.create(request, accountResponse.getId())).thenThrow(new EmailAlreadyExistedException());
    Mockito.when(addressFacadeService.create(request.getAddress())).thenReturn(null);

    Assertions.assertThrows(EmailAlreadyExistedException.class, () -> sellerFacadeService.create(request));
  }

  @Test
  void createSeller_WhenPhoneNumberAlreadyExisted_ThrowPhoneNumberAlreadyExistedException() {
    SellerRequest request = mockRequest();
    AccountRequest accountRequest = AccountRequest.builder()
          .username(request.getUsername())
          .password(request.getPassword())
          .confirmPassword(request.getConfirmPassword()).build();

    AccountResponse accountResponse = mockAccountResponse();

    Mockito.when(accountService.create(accountRequest, RoleAssign.SELLER.toString())).thenReturn(accountResponse);
    Mockito.when(sellerService.create(request, accountResponse.getId())).thenThrow(new PhoneNumberAlreadyExistedException());
    Mockito.when(addressFacadeService.create(request.getAddress())).thenReturn(null);

    Assertions.assertThrows(PhoneNumberAlreadyExistedException.class, () -> sellerFacadeService.create(request));
  }

  @Test
  void createSeller_WhenBrandNameAlreadyExisted_ThrowBrandNameAlreadyExistedException() {
    SellerRequest request = mockRequest();
    AccountRequest accountRequest = AccountRequest.builder()
          .username(request.getUsername())
          .password(request.getPassword())
          .confirmPassword(request.getConfirmPassword()).build();

    AccountResponse accountResponse = mockAccountResponse();

    Mockito.when(accountService.create(accountRequest, RoleAssign.SELLER.toString())).thenReturn(accountResponse);
    Mockito.when(sellerService.create(request, accountResponse.getId())).thenThrow(new BrandNameAlreadyExistedException());
    Mockito.when(addressFacadeService.create(request.getAddress())).thenReturn(null);

    Assertions.assertThrows(BrandNameAlreadyExistedException.class, () -> sellerFacadeService.create(request));
  }

  @Test
  void createSeller_WhenUsernameAlreadyExisted_ThrowUsernameAlreadyExistedException() {
    SellerRequest request = mockRequest();
    AccountRequest accountRequest = AccountRequest.builder()
          .username(request.getUsername())
          .password(request.getPassword())
          .confirmPassword(request.getConfirmPassword()).build();

    Mockito.when(accountService.create(accountRequest, RoleAssign.SELLER.toString()))
          .thenThrow(new UsernameAlreadyExistedException());
    Mockito.when(addressFacadeService.create(request.getAddress())).thenReturn(null);

    Assertions.assertThrows(UsernameAlreadyExistedException.class, () -> sellerFacadeService.create(request));
  }


  private SellerRequest mockRequest() {
    return SellerRequest.builder()
          .brandName("ShopSport")
          .username("hieuPTIT")
          .password("Hieu230708@")
          .confirmPassword("Hieu230708@")
          .fullName("Nguyen Minh Hieu")
          .phoneNumber("0987975814")
          .email("hieunm123.ptit@gmail.com")
          .dob("23/08/2001")
          .build();
  }

  private SellerResponse mockSellerResponse() {
    return SellerResponse.builder()
          .id(UID)
          .brandName("ShopSport")
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
}
