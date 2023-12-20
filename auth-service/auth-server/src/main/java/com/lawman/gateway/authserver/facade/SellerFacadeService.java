package com.lawman.gateway.authserver.facade;


import com.lawman.gateway.authclient.request.seller.SellerRequest;
import com.lawman.gateway.authclient.response.seller.SellerResponse;

public interface SellerFacadeService {

  SellerResponse create(SellerRequest request);
}
