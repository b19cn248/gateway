package com.lawman.gateway.authserver.exception.seller;

import com.lawman.gateway.authserver.exception.base.ConflictException;

public class BrandNameAlreadyExistedException extends ConflictException {

  public BrandNameAlreadyExistedException() {
    setCode("com.gateway.server.exception.seller.BrandNameAlreadyExistedException");
  }
}
