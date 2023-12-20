package com.lawman.gateway.authserver.service.account;

import com.lawman.gateway.authclient.request.account.AccountRequest;
import com.lawman.gateway.authclient.request.account.ChangePasswordRequest;
import com.lawman.gateway.authclient.response.account.AccountResponse;
import com.lawman.gateway.authserver.configuration.GatewayConfigurationTest;
import com.lawman.gateway.authserver.entity.Account;
import com.lawman.gateway.authserver.exception.account.AccountNotFoundException;
import com.lawman.gateway.authserver.exception.account.PasswordIncorrectException;
import com.lawman.gateway.authserver.exception.account.UsernameAlreadyExistedException;
import com.lawman.gateway.authserver.repository.AccountRepository;
import com.lawman.gateway.authserver.service.AccountService;
import com.lawman.gateway.authserver.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@WebMvcTest(UserService.class)
@ContextConfiguration(classes = {GatewayConfigurationTest.class})
class AccountServiceTests {

  @MockBean
  private AccountRepository accountRepository;

  @Autowired
  private AccountService accountService;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @Test
  void createAccount_WhenCreateSuccess_ReturnAccountResponse() {
    AccountRequest request = mockRequest();
    Account account = mockAccount();

    Mockito.when(accountRepository.save(any(Account.class))).thenReturn(account);

    AccountResponse response = accountService.create(request, null);
    System.out.println(response);

    check(account, response);
  }

  @Test
  void createAccount_WhenUserNameAlreadyExisted_ThrowUsernameAlreadyExistedException() {
    AccountRequest request = mockRequest();

    Mockito.when(accountRepository.existsByUsername(request.getUsername())).thenReturn(true);

    Assertions.assertThrows(UsernameAlreadyExistedException.class, () -> accountService.create(request, null));
  }

  @Test
  void testChangePasswordSuccess() {
    String accountId = "testAccountId";
    String currentPassword = "currentPassword";
    String newPassword = "newPassword";

    Account account = new Account();
    account.setId(accountId);
    account.setPassword(passwordEncoder.encode(currentPassword));

    System.out.println(account.getPassword());

    ChangePasswordRequest request = new ChangePasswordRequest();
    request.setPassword(currentPassword);
    request.setNewPassword(newPassword);

    when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
    when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
    when(passwordEncoder.matches(currentPassword,account.getPassword())).thenReturn(true);

    accountService.changePassword(accountId, request);

    Assertions.assertEquals("encodedNewPassword", account.getPassword());
  }

  @Test
  void testChangePasswordAccountNotFound() {
    String nonExistentAccountId = "nonExistentAccountId";
    ChangePasswordRequest request = new ChangePasswordRequest();

    request.setPassword("currentPassword");
    request.setNewPassword("newPassword");

    when(accountRepository.findById(nonExistentAccountId)).thenReturn(Optional.empty());

    Assertions.assertThrows(AccountNotFoundException.class, () ->
          accountService.changePassword(nonExistentAccountId, request));
  }

  @Test
  void testChangePasswordAccountNotFound1() {
    String nonExistentAccountId = "nonExistentAccountId";
    ChangePasswordRequest request = new ChangePasswordRequest();

    String currentPassword = "currentPassword";
    String newPassword = "newPassword";
    request.setPassword(currentPassword);
    request.setNewPassword(newPassword);

    Account account = mockAccount();

    when(accountRepository.findById(nonExistentAccountId)).thenReturn(Optional.of(account));
    when(passwordEncoder.matches(currentPassword, account.getPassword())).thenReturn(false);

    Assertions.assertThrows(PasswordIncorrectException.class, () ->
          accountService.changePassword(nonExistentAccountId, request));
  }


  private AccountRequest mockRequest() {
    return AccountRequest.builder()
          .username("user1")
          .password("Abcd1234@")
          .confirmPassword("Abcd1234@")
          .build();
  }

  private Account mockAccount() {
    return new Account("1", "user1", "Abcd1234@", "1", 0);
  }

  private void check(Account account, AccountResponse response) {
    assertThat(response).isNotNull();
    assertThat(account.getId()).isEqualTo(response.getId());
    assertThat(account.getUsername()).isEqualTo(response.getUsername());
    assertThat(response.isActivated()).isFalse();
  }

}
