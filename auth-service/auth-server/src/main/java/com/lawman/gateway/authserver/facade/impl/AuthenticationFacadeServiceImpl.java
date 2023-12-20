package com.lawman.gateway.authserver.facade.impl;

import com.lawman.gateway.authclient.response.auth.LoginResponse;
import com.lawman.gateway.authserver.entity.Account;
import com.lawman.gateway.authserver.exception.account.AccountHasBeenLockedException;
import com.lawman.gateway.authserver.exception.account.PasswordIncorrectException;
import com.lawman.gateway.authserver.facade.AuthenticationFacadeService;
import com.lawman.gateway.authserver.service.AccountService;
import com.lawman.gateway.authserver.service.JwtTokenService;
import com.lawman.gateway.authserver.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthConstant.MAX_FAILED_ATTEMPTS;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthenticationConstant.ACCESS_TOKEN_KEY;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthenticationConstant.REFRESH_TOKEN_KEY;


@Slf4j
@RequiredArgsConstructor
public class AuthenticationFacadeServiceImpl implements AuthenticationFacadeService {

  private final AccountService accountService;
  private final JwtTokenService jwtTokenService;
  private final RedisService redisService;

  @Override
  public LoginResponse login(String username, String password) {
    log.info("(login) username:{}, password:{}", username, password);

    Account account = accountService.getByUsername(username);

    log.info("(account) : {}", account);

    if (!accountService.equalPassword(password, account.getPassword())) {
      handleIncorrectPassword(account);
    } else if (isAccountLocked(account)) {
      throw new AccountHasBeenLockedException();
    }
    return unlockAndSave(account);
  }

  private void handleIncorrectPassword(Account account) {

    log.debug("(handleIncorrectPassword) account: {}", account);

    if (Objects.isNull(account.getLockTime())) {
      if (account.getFailedAttempt() < MAX_FAILED_ATTEMPTS - 1) {
        accountService.increaseFailedAttempts(account);
        throw new PasswordIncorrectException();
      } else {
        accountService.lock(account);
        throw new AccountHasBeenLockedException();
      }
    } else {
      if (System.currentTimeMillis() < account.getLockTime()) {
        throw new AccountHasBeenLockedException();
      } else {
        accountService.resetFailedAttemptsAndLockTime(account.getId());
        throw new PasswordIncorrectException();
      }
    }
  }

  private boolean isAccountLocked(Account account) {
    log.debug("(isAccountLocked) account : {}", account);
    return Objects.nonNull(account.getLockTime()) && System.currentTimeMillis() < account.getLockTime();
  }

  private LoginResponse unlockAndSave(Account account) {
    log.debug("(unlockAndSave) account:{}", account);
    accountService.resetFailedAttemptsAndLockTime(account.getId());
    return saveAndReturn(account);
  }

  private LoginResponse saveAndReturn(Account account) {

    log.debug("(saveAndReturn) account:{}", account);

    String accessToken = jwtTokenService.generateAccessToken(account);
    String refreshToken = jwtTokenService.generateRefreshToken(
          account.getId(),
          account.getUsername()
    );

    redisService.hashSet(ACCESS_TOKEN_KEY, account.getId(), accessToken);
    redisService.hashSet(REFRESH_TOKEN_KEY, account.getId(), refreshToken);

    return new LoginResponse(
          accessToken,
          refreshToken
    );
  }
}
