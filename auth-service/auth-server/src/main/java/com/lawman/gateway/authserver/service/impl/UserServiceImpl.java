package com.lawman.gateway.authserver.service.impl;

import com.lawman.gateway.authserver.entity.User;
import com.lawman.gateway.authserver.exception.customer.EmailAlreadyExistedException;
import com.lawman.gateway.authserver.exception.customer.PhoneNumberAlreadyExistedException;
import com.lawman.gateway.authserver.repository.UserRepository;
import com.lawman.gateway.authserver.service.UserService;
import com.lawman.gateway.authserver.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
  private final UserRepository repository;

  public UserServiceImpl(UserRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public void checkEmailAndPhoneNumberPreUpdate(String email, String phoneNumber, String newEmail, String newPhoneNumber) {
    log.info("(checkEmailAndPhoneNumberPreUpdate) email:{}, phoneNumber:{}, newEmail:{}, newPhoneNumber:{}",
          email, phoneNumber, newEmail, newPhoneNumber);

    if (Objects.nonNull(newEmail)
          && !newEmail.equals(email)
          && repository.existsByEmail(newEmail)
    ) {
      throw new EmailAlreadyExistedException();
    }

    if (Objects.nonNull(newPhoneNumber)
          && !newPhoneNumber.equals(phoneNumber)
          && repository.existsByPhoneNumber(newPhoneNumber)
    ) {
      throw new PhoneNumberAlreadyExistedException();
    }
  }

}
