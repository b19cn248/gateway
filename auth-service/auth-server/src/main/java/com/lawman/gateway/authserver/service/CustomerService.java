package com.lawman.gateway.authserver.service;

import com.lawman.gateway.authclient.request.customer.CustomerRequest;
import com.lawman.gateway.authclient.request.customer.CustomerUpdateRequest;
import com.lawman.gateway.authclient.response.common.PageResponse;
import com.lawman.gateway.authclient.response.customer.CustomerResponse;
import com.lawman.gateway.authserver.entity.Customer;
import com.lawman.gateway.authserver.service.base.BaseService;

public interface CustomerService extends BaseService<Customer> {

  CustomerResponse create(CustomerRequest request, String accountId);

  CustomerResponse update(String id, CustomerUpdateRequest request);

  CustomerResponse detail(String id);

  PageResponse<CustomerResponse> list(String keyword, int page, int size, boolean isAll);

  void remove(String id);
}
