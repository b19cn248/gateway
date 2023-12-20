package com.lawman.gateway.authserver.exception.seller;

import com.lawman.gateway.authserver.exception.base.NotFoundException;

public class SellerNotFoundException extends NotFoundException {
  public SellerNotFoundException() {
    setCode("com.gateway.server.exception.sellers.SellerNotFoundException");
  }
}
