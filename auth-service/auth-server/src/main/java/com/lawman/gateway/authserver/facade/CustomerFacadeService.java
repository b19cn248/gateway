package com.lawman.gateway.authserver.facade;

import com.lawman.gateway.authclient.request.customer.CustomerRequest;
import com.lawman.gateway.authclient.response.customer.CustomerResponse;

public interface CustomerFacadeService {

  CustomerResponse create(CustomerRequest request);
}
