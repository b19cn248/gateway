package com.lawman.gateway.authserver.facade;

import com.lawman.gateway.authclient.response.auth.LoginResponse;

public interface AuthenticationFacadeService {
  LoginResponse login(String username, String password);
}
