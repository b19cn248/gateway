package com.lawman.gateway.authserver.service.customer;

import com.lawman.gateway.authclient.request.customer.CustomerRequest;
import com.lawman.gateway.authclient.request.customer.CustomerUpdateRequest;
import com.lawman.gateway.authclient.response.customer.CustomerResponse;
import com.lawman.gateway.authserver.configuration.GatewayConfigurationTest;
import com.lawman.gateway.authserver.entity.Customer;
import com.lawman.gateway.authserver.exception.customer.CustomerNotFoundException;
import com.lawman.gateway.authserver.exception.customer.EmailAlreadyExistedException;
import com.lawman.gateway.authserver.exception.customer.PhoneNumberAlreadyExistedException;
import com.lawman.gateway.authserver.exception.seller.SellerPhoneNumberNotFoundException;
import com.lawman.gateway.authserver.repository.CustomerRepository;
import com.lawman.gateway.authserver.service.CustomerService;
import com.lawman.gateway.authserver.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(UserService.class)
@ContextConfiguration(classes = {GatewayConfigurationTest.class})
class CustomerServiceTests {

  @MockBean
  private CustomerRepository repository;

  @Autowired
  private CustomerService customerService;

  private static final String UID = UUID.randomUUID().toString();


  @Test
  void createCustomer_WhenCreateSuccess_ReturnCustomer() {
    CustomerRequest request = mockRequest();
    Customer customer = mockCustomer();

    Mockito.when(repository.save(customer)).thenReturn(customer);

    CustomerResponse response = customerService.create(request, UID);

    check(customer, response);
  }

  @Test
  void createCustomer_WhenEmailAlreadyExisted_ThrowEmailAlreadyExistedException() {
    CustomerRequest request = mockRequest();

    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(true);

    Assertions.assertThrows(EmailAlreadyExistedException.class, () -> customerService.create(request, UID));
  }


  @Test
  void createCustomer_WhenPhoneNumberAlreadyExisted_ThrowPhoneNumberAlreadyExistedException() {
    CustomerRequest request = mockRequest();

    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

    Assertions.assertThrows(PhoneNumberAlreadyExistedException.class, () -> customerService.create(request, UID));
  }

  @Test
  void update_WhenInputValid_ReturnCustomerResponse() {

    CustomerUpdateRequest request = mockUpdateRequest();
    Customer customer = mockCustomer();

    System.out.println(customer.getId());

    Mockito.when(repository.existsSellerPhone(request.getSellerPhoneNumber())).thenReturn(true);
    Mockito.when(repository.findById(UID)).thenReturn(Optional.of(customer));

    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(false);
    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
    Mockito.when(repository.save(customer)).thenReturn(customer);

    CustomerResponse response = customerService.update(UID, request);

    check(customer, response);

  }

  @Test
  void update_WhenCustomerNotFound_ThenThrowCustomerNotFoundException() {
    CustomerUpdateRequest request = mockUpdateRequest();

    Mockito.when(repository.existsSellerPhone(request.getSellerPhoneNumber())).thenReturn(true);
    Mockito.when(repository.findById(UID)).thenReturn(Optional.empty());

    Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.update(UID, request));
  }

  @Test
  void update_WhenSellerPhoneNumberNotFound_ThenThrowSellerNotFoundNotFoundException() {
    CustomerUpdateRequest request = mockUpdateRequest();


    Mockito.when(repository.existsSellerPhone(request.getSellerPhoneNumber())).thenReturn(false);

    Assertions.assertThrows(SellerPhoneNumberNotFoundException.class, () -> customerService.update(UID, request));
  }


  @Test
  void update_WhenEmailAlreadyExisted_ThenThrowEmailAlreadyExistedException() {
    CustomerUpdateRequest request = mockUpdateRequest();
    Customer customer = mockCustomer();

    Mockito.when(repository.existsSellerPhone(request.getSellerPhoneNumber())).thenReturn(true);
    Mockito.when(repository.findById(UID)).thenReturn(Optional.of(customer));
    Mockito.when(repository.existsByEmail(request.getEmail())).thenReturn(true);

    Assertions.assertThrows(EmailAlreadyExistedException.class, () -> customerService.update(UID, request));
  }

  @Test
  void update_WhenPhoneNumberAlreadyExisted_ThenThrowPhoneNumberAlreadyExistedException() {
    CustomerUpdateRequest request = mockUpdateRequest();
    Customer customer = mockCustomer();

    Mockito.when(repository.existsSellerPhone(request.getSellerPhoneNumber())).thenReturn(true);
    Mockito.when(repository.findById(UID)).thenReturn(Optional.of(customer));
    Mockito.when(repository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

    Assertions.assertThrows(PhoneNumberAlreadyExistedException.class, () -> customerService.update(UID, request));
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

  private Customer mockCustomer() {
    return new Customer(
          UID,
          "Nguyen Minh Hieu",
          "0987654121",
          "hieunm1234.ptit@gmail.com",
          "23/08/2001",
          1,
          "0987333222"
    );
  }

  private CustomerUpdateRequest mockUpdateRequest() {
    return CustomerUpdateRequest.builder()
          .sellerPhoneNumber("0987976814")
          .fullName("Nguyen Minh Hieu")
          .phoneNumber("0987654321")
          .email("hieunm123.ptit@gmail.com")
          .dob("23/08/2001")
          .build();
  }

  private void check(Customer customer, CustomerResponse response) {
    assertThat(customer.getId()).isEqualTo(UID);
    assertThat(customer.getId()).isEqualTo(response.getId());
    assertThat(customer.getFullName()).isEqualTo(response.getFullName());
    assertThat(customer.getPhoneNumber()).isEqualTo(response.getPhoneNumber());
    assertThat(customer.getEmail()).isEqualTo(response.getEmail());
    assertThat(customer.getStatus()).isEqualTo(response.getStatus());
    assertThat(customer.getSellerPhoneNumber()).isEqualTo(response.getSellerPhoneNumber());
    assertThat(customer.getBonusCoin()).isEqualTo(0.0);
  }
}
