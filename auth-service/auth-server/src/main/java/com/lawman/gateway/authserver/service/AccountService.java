package com.lawman.gateway.authserver.service;


import com.lawman.gateway.authclient.request.account.AccountRequest;
import com.lawman.gateway.authclient.request.account.ChangePasswordRequest;
import com.lawman.gateway.authclient.response.account.AccountResponse;
import com.lawman.gateway.authserver.entity.Account;
import com.lawman.gateway.authserver.service.base.BaseService;

public interface AccountService extends BaseService<Account> {
  AccountResponse create(AccountRequest request, String role);

  Account getByUsername(String username);

  String getRole(String id);

  boolean equalPassword(String passwordRaw, String passwordEncrypted);

  void increaseFailedAttempts(Account account);

  void resetFailedAttemptsAndLockTime(String id);

  void lock(Account account);

  Account findByEmail(String email);

  Account getByResetPasswordToken(String token);

  void updatePassword(String id, String password);

  void forgotPassword(String email);

  void changePassword(String id, ChangePasswordRequest request);
}
