package com.lawman.gateway.authserver.controller;

import com.lawman.gateway.authclient.request.account.ChangePasswordRequest;
import com.lawman.gateway.authserver.response.ResponseGeneral;
import com.lawman.gateway.authserver.service.AccountService;
import com.lawman.gateway.authserver.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AccountMessage.CHANGE_PASSWORD_SUCCESS;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CommonConstants.DEFAULT_LANGUAGE;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CommonConstants.LANGUAGE;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

  private final AccountService accountService;
  private final MessageService messageService;

  @PostMapping("{id}/change-password")
  public ResponseGeneral<Void> changePassword(
        @PathVariable String id,
        @RequestBody @Valid ChangePasswordRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(changePassword) id : {} ,requestDTO: {}", id, request);
    accountService.changePassword(id, request);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(CHANGE_PASSWORD_SUCCESS, language)
    );
  }
}
