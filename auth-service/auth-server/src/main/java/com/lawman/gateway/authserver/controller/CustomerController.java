package com.lawman.gateway.authserver.controller;

import com.lawman.gateway.authclient.request.customer.CustomerRequest;
import com.lawman.gateway.authclient.request.customer.CustomerUpdateRequest;
import com.lawman.gateway.authclient.response.common.PageResponse;
import com.lawman.gateway.authclient.response.customer.CustomerResponse;
import com.lawman.gateway.authserver.facade.CustomerFacadeService;
import com.lawman.gateway.authserver.response.ResponseGeneral;
import com.lawman.gateway.authserver.service.CustomerService;
import com.lawman.gateway.authserver.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CommonConstants.*;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CustomerMessage.CREATE_CUSTOMER_SUCCESS;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.SellerMessage.UPDATE_SELLER_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@Slf4j
public class CustomerController {
  private final CustomerFacadeService customerFacadeService;
  private final MessageService messageService;
  private final CustomerService customerService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<CustomerResponse> create(
        @RequestBody @Valid CustomerRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language) {
    log.info("(create)request: {}", request);

    return ResponseGeneral.ofCreated(
          messageService.getMessage(CREATE_CUSTOMER_SUCCESS, language),
          customerFacadeService.create(request));
  }

  @PutMapping("{id}")
  public ResponseGeneral<CustomerResponse> update(
        @PathVariable String id,
        @RequestBody @Valid CustomerUpdateRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(update)request: {}", request);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(UPDATE_SELLER_SUCCESS, language),
          customerService.update(id, request));
  }

  @GetMapping
  public ResponseGeneral<PageResponse<CustomerResponse>> list(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "all", defaultValue = "false", required = false) boolean isAll,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list) keyword: {}, size: {}, page: {}, isAll: {}", keyword, size, page, isAll);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          customerService.list(keyword, page, size, isAll));
  }

  @GetMapping("{id}")
  public ResponseGeneral<CustomerResponse> get(
        @PathVariable String id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(get) id:{}", id);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          customerService.detail(id)
    );
  }

  @DeleteMapping("{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable String id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(delete) id:{}", id);
    customerService.remove(id);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language)
    );
  }

}
