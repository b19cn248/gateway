package com.lawman.gateway.authserver.service;

import com.lawman.gateway.authclient.request.seller.SellerRequest;
import com.lawman.gateway.authclient.request.seller.SellerUpdateRequest;
import com.lawman.gateway.authclient.response.common.PageResponse;
import com.lawman.gateway.authclient.response.seller.SellerResponse;
import com.lawman.gateway.authserver.entity.Seller;
import com.lawman.gateway.authserver.service.base.BaseService;

public interface SellerService extends BaseService<Seller> {

  boolean existsByPhoneNumber(String phoneNumber);

  SellerResponse create(SellerRequest request, String accountId);

  SellerResponse update(String id, SellerUpdateRequest request);

  PageResponse<SellerResponse> list(String keyword, int page, int size, boolean isAll);

  SellerResponse detail(String id);

  void remove(String id);
}
