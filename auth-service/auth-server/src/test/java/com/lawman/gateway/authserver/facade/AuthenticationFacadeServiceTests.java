package com.lawman.gateway.authserver.facade;

import com.lawman.gateway.authclient.response.auth.LoginResponse;
import com.lawman.gateway.authserver.configuration.GatewayConfigurationTest;
import com.lawman.gateway.authserver.entity.Account;
import com.lawman.gateway.authserver.exception.account.AccountHasBeenLockedException;
import com.lawman.gateway.authserver.exception.account.PasswordIncorrectException;
import com.lawman.gateway.authserver.exception.account.UsernameNotFoundException;
import com.lawman.gateway.authserver.service.AccountService;
import com.lawman.gateway.authserver.service.JwtTokenService;
import com.lawman.gateway.authserver.service.RedisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthConstant.LOCK_TIME;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthenticationConstant.ACCESS_TOKEN_KEY;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthenticationConstant.REFRESH_TOKEN_KEY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@WebMvcTest(AuthenticationFacadeService.class)
@ContextConfiguration(classes = {GatewayConfigurationTest.class})
class AuthenticationFacadeServiceTests {

  @MockBean
  private AccountService accountService;

  @MockBean
  private JwtTokenService jwtTokenService;

  @MockBean
  private RedisService redisService;

  @Autowired
  private AuthenticationFacadeService authenticationFacadeService;

  private static final String UID = UUID.randomUUID().toString();
  private static final String ROLE_ID = UUID.randomUUID().toString();
  private static final String USER_NAME = "hieuPTIT";
  private static final String PASSWORD = "Hieu230708@";
  private static final String ACCESS_TOKEN = "mock_access_token";
  private static final String REFRESH_TOKEN = "mock_refresh_token";

  private static final String ROLE_NAME = "USER";

  @Test
  void login_WhenInputValid_ReturnLoginResponse() {

    Account account = mockAccount();
    account.setLockTime(null);

    testLoginSuccess(account);
  }

  @Test
  void Login_WhenTimeLockPeriodExpired_ReturnLoginResponse() {
    Account account = mockAccount();
    account.setLockTime(System.currentTimeMillis());
    account.setFailedAttempt(2);

    testLoginSuccess(account);

  }

  @Test
  void login_WhenPasswordIncorrect_ThrowPasswordIncorrectException() {
    Account account = mockAccount();

    when(accountService.equalPassword(PASSWORD, account.getPassword())).thenReturn(false);
    Mockito.when(accountService.getByUsername(USER_NAME)).thenReturn(account);

    assertThrows(PasswordIncorrectException.class, () -> authenticationFacadeService.login(USER_NAME, PASSWORD));
  }

  @Test
  void login_WhenAccountIsLockAndPasswordIncorrect_ThrowAccountHasBeenLockedException() {
    Account account = mockAccount();
    account.setFailedAttempt(2);

    Mockito.when(accountService.getByUsername(USER_NAME)).thenReturn(account);

    assertThrows(AccountHasBeenLockedException.class, () -> authenticationFacadeService.login(USER_NAME, PASSWORD));

  }

  @Test
  void login_WhenAccountIsLockedAndPasswordCorrect_ThrowAccountHasBeenLockedException() {
    Account account = mockAccount();

    account.setLockTime(System.currentTimeMillis() + LOCK_TIME);

    Mockito.when(accountService.getByUsername(USER_NAME)).thenReturn(account);
    when(accountService.equalPassword(PASSWORD, account.getPassword())).thenReturn(true);

    assertThrows(AccountHasBeenLockedException.class, () -> authenticationFacadeService.login(USER_NAME, PASSWORD));

  }


  @Test
  void login_WhenPasswordIncorrectAndAccountIsNonLocked_ThrowPasswordIncorrectException() {
    Account account = mockAccount();

    account.setLockTime(System.currentTimeMillis() - LOCK_TIME);

    Mockito.when(accountService.getByUsername(USER_NAME)).thenReturn(account);

    assertThrows(PasswordIncorrectException.class, () -> authenticationFacadeService.login(USER_NAME, PASSWORD));

  }

  @Test
  void login_WhenPasswordIncorrectAndAccountIsLocked_ThrowPasswordIncorrectException() {
    Account account = mockAccount();

    account.setLockTime(System.currentTimeMillis() + LOCK_TIME);

    Mockito.when(accountService.getByUsername(USER_NAME)).thenReturn(account);

    assertThrows(AccountHasBeenLockedException.class, () -> authenticationFacadeService.login(USER_NAME, PASSWORD));

  }

  @Test
  void login_WhenUsernameNotFound_ThrowUsernameNotFoundException() {
    when(accountService.getByUsername(USER_NAME)).thenThrow(new UsernameNotFoundException());

    assertThrows(UsernameNotFoundException.class, () -> authenticationFacadeService.login(USER_NAME, PASSWORD));
  }


  @Test
  void login_WhenPasswordIncorrect_ThrowPasswordMismatchException() {

    Account account = mockAccount();

    Mockito.when(accountService.getByUsername(USER_NAME)).thenReturn(account);

    doThrow(new UsernameNotFoundException()).when(accountService).equalPassword(PASSWORD, account.getPassword());

    assertThrows(UsernameNotFoundException.class, () -> authenticationFacadeService.login(USER_NAME, PASSWORD));
  }

  private Account mockAccount() {
    return new Account(
          UID,
          USER_NAME,
          PASSWORD,
          ROLE_ID,
          0
    );
  }

  private void testLoginSuccess(Account account) {
    Mockito.when(accountService.getByUsername(USER_NAME)).thenReturn(account);
    Mockito.when(accountService.getRole(account.getId())).thenReturn(ROLE_NAME);

    when(jwtTokenService.generateAccessToken(account)).thenReturn(ACCESS_TOKEN);
    when(jwtTokenService.generateRefreshToken(account.getId(), USER_NAME)).thenReturn(REFRESH_TOKEN);
    when(accountService.equalPassword(PASSWORD, account.getPassword())).thenReturn(true);

    LoginResponse expected = authenticationFacadeService.login(USER_NAME, PASSWORD);
    LoginResponse actual = new LoginResponse(
          ACCESS_TOKEN,
          REFRESH_TOKEN
    );

    Assertions.assertEquals(actual, expected);

    verify(redisService, times(1)).hashSet(ACCESS_TOKEN_KEY, account.getId(), ACCESS_TOKEN);
    verify(redisService, times(1)).hashSet(REFRESH_TOKEN_KEY, account.getId(), REFRESH_TOKEN);
  }
}
