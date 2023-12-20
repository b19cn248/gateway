package com.lawman.gateway.authserver.service.seller;

import com.lawman.gateway.authclient.request.seller.SellerRequest;
import com.lawman.gateway.authclient.response.seller.SellerResponse;
import com.lawman.gateway.authserver.configuration.GatewayConfigurationTest;
import com.lawman.gateway.authserver.entity.Seller;
import com.lawman.gateway.authserver.exception.customer.EmailAlreadyExistedException;
import com.lawman.gateway.authserver.exception.customer.PhoneNumberAlreadyExistedException;
import com.lawman.gateway.authserver.exception.seller.BrandNameAlreadyExistedException;
import com.lawman.gateway.authserver.repository.SellerRepository;
import com.lawman.gateway.authserver.service.SellerService;
import com.lawman.gateway.authserver.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.mockito.Mockito.any;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(UserService.class)
@ContextConfiguration(classes = {GatewayConfigurationTest.class})
class SellerServiceTests {

  @MockBean
  private SellerRepository sellerRepository;

  @Autowired
  private SellerService sellerService;

  private static final String UID = UUID.randomUUID().toString();

  @Test
  void createSeller_WhenCreateSuccess_ReturnSeller() {
    SellerRequest request = mockRequest();
    Seller seller = mockSeller();

    Mockito.when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

    SellerResponse actual = sellerService.create(request, UID);

    assertThat(actual).isEqualTo(mockResponse());
  }

  @Test
  void createSeller_WhenEmailAlreadyExisted_ThrowEmailAlreadyExistedException() {
    SellerRequest request = mockRequest();

    Mockito.when(sellerRepository.existsByEmail(request.getEmail())).thenReturn(true);

    Assertions.assertThrows(EmailAlreadyExistedException.class, () -> sellerService.create(request, UID));
  }

  @Test
  void createSeller_WhenPhoneNumberAlreadyExisted_ThrowPhoneNumberAlreadyExistedException() {
    SellerRequest request = mockRequest();

    Mockito.when(sellerRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

    Assertions.assertThrows(PhoneNumberAlreadyExistedException.class, () -> sellerService.create(request, UID));
  }


  @Test
  void createSeller_WhenBrandNameAlreadyExisted_ThrowBrandNameAlreadyExistedException() {
    SellerRequest request = mockRequest();

    Mockito.when(sellerRepository.existsByBrandName(request.getBrandName())).thenReturn(true);

    Assertions.assertThrows(BrandNameAlreadyExistedException.class, () -> sellerService.create(request, UID));
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


  private Seller mockSeller() {
    return new Seller(
          UID,
          "Nguyen Minh Hieu",
          "0987975814",
          "hieunm123.ptit@gmail.com",
          "23/08/2001",
          "ShopSport"
    );
  }

  private SellerResponse mockResponse() {
    return SellerResponse.builder()
          .id(UID)
          .brandName("ShopSport")
          .fullName("Nguyen Minh Hieu")
          .phoneNumber("0987975814")
          .email("hieunm123.ptit@gmail.com")
          .dob("23/08/2001")
          .build();
  }
}
