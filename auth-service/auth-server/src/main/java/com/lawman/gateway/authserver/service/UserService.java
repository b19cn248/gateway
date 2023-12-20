package com.lawman.gateway.authserver.service;

import com.lawman.gateway.authserver.entity.User;
import com.lawman.gateway.authserver.service.base.BaseService;

public interface UserService extends BaseService<User> {

  void checkEmailAndPhoneNumberPreUpdate(String email, String phoneNumber, String newEmail, String newPhoneNumber);

}
