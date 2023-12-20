package com.lawman.gateway.authserver.controller;


import com.lawman.gateway.authclient.request.account.ResetPasswordRequest;
import com.lawman.gateway.authclient.request.auth.LoginRequest;
import com.lawman.gateway.authclient.response.auth.LoginResponse;
import com.lawman.gateway.authserver.entity.Account;
import com.lawman.gateway.authserver.facade.AuthenticationFacadeService;
import com.lawman.gateway.authserver.response.ResponseGeneral;
import com.lawman.gateway.authserver.service.AccountService;
import com.lawman.gateway.authserver.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CommonConstants.*;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationFacadeService authenticationFacadeService;
  private final MessageService messageService;
  private final AccountService accountService;

  @PostMapping("login")
  public ResponseGeneral<LoginResponse> login(
        @RequestBody LoginRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(login)  request: {}", request);
    return ResponseGeneral.of(
          HttpStatus.OK.value(),
          messageService.getMessage(SUCCESS, language),
          authenticationFacadeService.login(
                request.getUsername(),
                request.getPassword()
          )
    );
  }

  @GetMapping("/forgot-password")
  public ResponseGeneral<Void> forgotPassword(
        @RequestParam String email,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(forgotPassword) email:{}", email);

    accountService.forgotPassword(email);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language)
    );
  }

  @PostMapping("/reset-password")
  public ResponseGeneral<Void> resetPassword(
        @RequestParam String token,
        @RequestParam String email,
        @RequestBody ResetPasswordRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(forgotPassword) request:{}", request);
    request.setEmail(email);
    request.setToken(token);

    Account account = accountService.getByResetPasswordToken(token);

    accountService.updatePassword(account.getId(), request.getPassword());

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language)
    );
  }
}