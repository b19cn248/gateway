package com.lawman.gateway.authserver.service.impl;

import com.lawman.gateway.authclient.request.customer.CustomerRequest;
import com.lawman.gateway.authclient.request.customer.CustomerUpdateRequest;
import com.lawman.gateway.authclient.response.common.PageResponse;
import com.lawman.gateway.authclient.response.customer.CustomerResponse;
import com.lawman.gateway.authserver.entity.Customer;
import com.lawman.gateway.authserver.exception.customer.CustomerNotFoundException;
import com.lawman.gateway.authserver.exception.customer.EmailAlreadyExistedException;
import com.lawman.gateway.authserver.exception.customer.PhoneNumberAlreadyExistedException;
import com.lawman.gateway.authserver.exception.seller.SellerPhoneNumberNotFoundException;
import com.lawman.gateway.authserver.repository.CustomerRepository;
import com.lawman.gateway.authserver.service.CustomerService;
import com.lawman.gateway.authserver.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Objects;

@Slf4j
public class CustomerServiceImpl extends BaseServiceImpl<Customer> implements CustomerService {
  private final CustomerRepository repository;

  public CustomerServiceImpl(CustomerRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public CustomerResponse create(CustomerRequest request, String accountId) {
    log.info("(create) request:{}, accountId:{}", request, accountId);

    checkExistedEmailAndPhoneNumber(request.getEmail(), request.getPhoneNumber());

    Customer customer = repository.save(toEntity(request, accountId));

    log.info("(create) customer:{}", customer);

    return toDTO(customer);
  }

  @Override
  public CustomerResponse update(String id, CustomerUpdateRequest request) {
    log.info("(update) id:{}, request:{}", id, request);

    checkExistsSellerPhoneNumber(request.getSellerPhoneNumber());

    Customer customer = this.find(id);

    checkExistedPreUpdate(customer, request);

    this.updateCustomer(customer, request);

    return toDTO(update(customer));
  }

  @Override
  public CustomerResponse detail(String id) {
    log.info("(detail) id:{}", id);
    return repository.detail(id).orElseThrow(CustomerNotFoundException::new);
  }

  @Override
  public PageResponse<CustomerResponse> list(String keyword, int page, int size, boolean isAll) {
    log.info("(list) keyword:{}, page:{}, size:{}, isAll:{}", keyword, page, size, isAll);

    List<CustomerResponse> customers = isAll ?
          repository.getAll(keyword) :
          repository.searchByKeyword(keyword, PageRequest.of(page, size));

    return PageResponse.of(
          customers,
          repository.countSearch(keyword)
    );
  }

  @Override
  public void remove(String id) {
    log.info("(remove) id:{}", id);
    this.find(id);

    repository.softDelete(id);
  }

  private void checkExistsSellerPhoneNumber(String sellerPhoneNumber) {
    if (!repository.existsSellerPhone(sellerPhoneNumber)) {
      log.error("(checkExistsSellerPhoneNumber) ====================> SellerPhoneNumberNotFoundException");
      throw new SellerPhoneNumberNotFoundException();
    }
  }

  private Customer find(String id) {
    log.debug("(find) id:{}", id);
    return repository.findById(id).orElseThrow(CustomerNotFoundException::new);
  }

  private void checkExistedEmailAndPhoneNumber(String email, String phoneNumber) {
    log.info("(checkExistedEmailAndPhoneNumber) email:{}, phoneNumber: {}", email, phoneNumber);
    if (repository.existsByEmail(email)) {
      log.error("(checkExistedEmailAndPhoneNumber) ====================> EmailAlreadyExistedException");
      throw new EmailAlreadyExistedException();
    }
    if (repository.existsByPhoneNumber(phoneNumber)) {
      log.error("(checkExistedEmailAndPhoneNumber) ====================> PhoneNumberAlreadyExistedException");
      throw new PhoneNumberAlreadyExistedException();
    }
  }

  private void checkExistedPreUpdate(Customer customer, CustomerUpdateRequest request) {
    if (Objects.nonNull(request.getEmail())
          && !request.getEmail().equals(customer.getEmail())
          && repository.existsByEmail(request.getEmail())
    ) {
      throw new EmailAlreadyExistedException();
    }

    if (Objects.nonNull(request.getPhoneNumber())
          && !request.getPhoneNumber().equals(customer.getPhoneNumber())
          && repository.existsByPhoneNumber(request.getPhoneNumber())
    ) {
      throw new PhoneNumberAlreadyExistedException();
    }
  }


  private Customer toEntity(CustomerRequest request, String accountId) {

    return new Customer(
          accountId,
          request.getFullName(),
          request.getPhoneNumber(),
          request.getEmail(),
          request.getDob(),
          1,
          request.getSellerPhoneNumber()
    );
  }

  private CustomerResponse toDTO(Customer customer) {
    return CustomerResponse.builder()
          .id(customer.getId())
          .bonusCoin(customer.getBonusCoin())
          .status(customer.getStatus())
          .sellerPhoneNumber(customer.getSellerPhoneNumber())
          .fullName(customer.getFullName())
          .phoneNumber(customer.getPhoneNumber())
          .email(customer.getEmail())
          .dob(customer.getDob())
          .build();
  }

  private void updateCustomer(Customer customer, CustomerUpdateRequest request) {

    log.debug("(updateCustomer) customer:{}, request:{}", customer, request);

    if (Objects.nonNull(request.getFullName())) {
      customer.setFullName(request.getFullName());
    }
    if (Objects.nonNull(request.getPhoneNumber())) {
      customer.setPhoneNumber(request.getPhoneNumber());
    }
    if (Objects.nonNull(request.getEmail())) {
      customer.setEmail(request.getEmail());
    }
    if (Objects.nonNull(request.getDob())) {
      customer.setDob(request.getDob());
    }
    if (Objects.nonNull(request.getSellerPhoneNumber())) {
      customer.setSellerPhoneNumber(request.getSellerPhoneNumber());
    }
  }
}
