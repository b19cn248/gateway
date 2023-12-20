package com.lawman.gateway.authserver.facade;

import com.lawman.gateway.authclient.request.address.AddressRequest;
import com.lawman.gateway.authclient.response.address.AddressResponse;
import org.springframework.stereotype.Service;

@Service
public interface AddressFacadeService {
  AddressResponse create(AddressRequest request);
}
