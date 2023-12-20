package com.lawman.gateway.authserver.controller;

import com.lawman.gateway.authclient.request.seller.SellerRequest;
import com.lawman.gateway.authclient.request.seller.SellerUpdateRequest;
import com.lawman.gateway.authclient.response.common.PageResponse;
import com.lawman.gateway.authclient.response.seller.SellerResponse;
import com.lawman.gateway.authserver.facade.SellerFacadeService;
import com.lawman.gateway.authserver.response.ResponseGeneral;
import com.lawman.gateway.authserver.service.MessageService;
import com.lawman.gateway.authserver.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CommonConstants.*;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CustomerMessage.CREATE_CUSTOMER_SUCCESS;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.SellerMessage.UPDATE_SELLER_SUCCESS;

@RestController
@RequestMapping("/api/v1/sellers")
@RequiredArgsConstructor
@Slf4j
public class SellerController {
  private final MessageService messageService;
  private final SellerFacadeService sellerFacadeService;
  private final SellerService sellerService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<SellerResponse> create(
        @RequestBody @Valid SellerRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language) {
    log.info("(create)request: {}", request);

    return ResponseGeneral.ofCreated(
          messageService.getMessage(CREATE_CUSTOMER_SUCCESS, language),
          sellerFacadeService.create(request));
  }

  @PutMapping("{id}")
  public ResponseGeneral<SellerResponse> update(
        @PathVariable String id,
        @RequestBody @Valid SellerUpdateRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(create)request: {}", request);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(UPDATE_SELLER_SUCCESS, language),
          sellerService.update(id, request));
  }

  @GetMapping
  public ResponseGeneral<PageResponse<SellerResponse>> list(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "all", defaultValue = "false", required = false) boolean isAll,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list) keyword: {}, size: {}, page: {}, isAll: {}", keyword, size, page, isAll);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          sellerService.list(keyword, page, size, isAll));
  }

  @GetMapping("{id}")
  public ResponseGeneral<SellerResponse> get(
        @PathVariable String id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(get) id:{}", id);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          sellerService.detail(id)
    );
  }

  @DeleteMapping("{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable String id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(delete) id:{}", id);
    sellerService.remove(id);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language)
    );
  }
}
