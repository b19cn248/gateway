package com.lawman.gateway.authserver.service.impl;

import com.lawman.gateway.authclient.request.seller.SellerRequest;
import com.lawman.gateway.authclient.request.seller.SellerUpdateRequest;
import com.lawman.gateway.authclient.response.common.PageResponse;
import com.lawman.gateway.authclient.response.seller.SellerResponse;
import com.lawman.gateway.authserver.entity.Seller;
import com.lawman.gateway.authserver.exception.customer.EmailAlreadyExistedException;
import com.lawman.gateway.authserver.exception.customer.PhoneNumberAlreadyExistedException;
import com.lawman.gateway.authserver.exception.seller.BrandNameAlreadyExistedException;
import com.lawman.gateway.authserver.exception.seller.SellerNotFoundException;
import com.lawman.gateway.authserver.repository.SellerRepository;
import com.lawman.gateway.authserver.service.SellerService;
import com.lawman.gateway.authserver.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

@Slf4j
public class SellerServiceImpl extends BaseServiceImpl<Seller> implements SellerService {

  private final SellerRepository repository;

  public SellerServiceImpl(SellerRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public boolean existsByPhoneNumber(String phoneNumber) {
    return repository.existsByPhoneNumber(phoneNumber);
  }

  @Override
  public SellerResponse create(SellerRequest request, String accountId) {
    log.info("(create) request:{}, accountId:{}", request, accountId);

    checkExistedEmailAndPhoneNumberAndBrandName(
          request.getEmail(),
          request.getPhoneNumber(),
          request.getBrandName()
    );

    return toDTO(repository.save(toEntity(request, accountId)));
  }

  @Override
  public SellerResponse update(String id, SellerUpdateRequest request) {
    log.info("(update) id:{}, request:{}", id, request);

    Seller existedSeller = this.find(id);

    checkExistedPreUpdate(existedSeller, request);

    this.updateSeller(existedSeller, request);

    return toDTO(update(existedSeller));
  }

  @Override
  public PageResponse<SellerResponse> list(String keyword, int page, int size, boolean isAll) {
    log.info("(list) keyword:{}, page:{}, size:{}, isAll:{}", keyword, page, size, isAll);

    Pageable pageable = PageRequest.of(page, size);

    List<SellerResponse> sellers = isAll
          ? repository.listAll(keyword)
          : repository.list(keyword, pageable);

    return PageResponse.of(
          sellers,
          repository.count(keyword)
    );
  }

  @Override
  public SellerResponse detail(String id) {
    log.info("(detail) id:{}", id);
    return repository.detail(id).orElseThrow(SellerNotFoundException::new);
  }

  @Override
  public void remove(String id) {
    log.info("(remove) id:{}", id);
    this.find(id);

    repository.softDelete(id);
  }

  private Seller find(String id) {
    return repository.findById(id).orElseThrow(SellerNotFoundException::new);
  }

  private void checkExistedPreUpdate(Seller seller, SellerUpdateRequest request) {
    if (Objects.nonNull(request.getEmail())
          && !request.getEmail().equals(seller.getEmail())
          && repository.existsByEmail(request.getEmail())
    ) {
      throw new EmailAlreadyExistedException();
    }

    if (Objects.nonNull(request.getPhoneNumber())
          && !request.getPhoneNumber().equals(seller.getPhoneNumber())
          && repository.existsByPhoneNumber(request.getPhoneNumber())
    ) {
      throw new PhoneNumberAlreadyExistedException();
    }

    if (Objects.nonNull(request.getBrandName())
          && !request.getBrandName().equals(seller.getBrandName())
          && repository.existsByBrandName(request.getBrandName())
    ) {
      throw new BrandNameAlreadyExistedException();
    }
  }

  private void checkExistedEmailAndPhoneNumberAndBrandName(String email, String phoneNumber, String brandName) {
    log.info("(checkExistedEmailAndPhoneNumberAndBrandName) email:{}, phoneNumber: {}, brandName:{}",
          email, phoneNumber, brandName);
    if (repository.existsByEmail(email)) {
      log.error("(checkExistedEmailAndPhoneNumberAndBrandName) ==================> EmailAlreadyExistedException");
      throw new EmailAlreadyExistedException();
    }
    if (repository.existsByPhoneNumber(phoneNumber)) {
      log.error("(checkExistedEmailAndPhoneNumberAndBrandName) ==================> PhoneNumberAlreadyExistedException");
      throw new PhoneNumberAlreadyExistedException();
    }
    if (repository.existsByBrandName(brandName)) {
      log.error("(checkExistedEmailAndPhoneNumberAndBrandName) ====================> BrandNameAlreadyExistedException");
      throw new BrandNameAlreadyExistedException();
    }
  }

  private Seller toEntity(SellerRequest request, String accountId) {
    return new Seller(
          accountId,
          request.getFullName(),
          request.getPhoneNumber(),
          request.getEmail(),
          request.getDob(),
          request.getBrandName()
    );
  }

  private void updateSeller(Seller seller, SellerUpdateRequest request) {
    if (Objects.nonNull(request.getBrandName())) {
      seller.setBrandName(request.getBrandName());
    }
    if (Objects.nonNull(request.getWarehouseId())) {
      seller.setWarehouseId(request.getWarehouseId());
    }
    if (Objects.nonNull(request.getFullName())) {
      seller.setFullName(request.getFullName());
    }
    if (Objects.nonNull(request.getPhoneNumber())) {
      seller.setPhoneNumber(request.getPhoneNumber());
    }
    if (Objects.nonNull(request.getEmail())) {
      seller.setEmail(request.getEmail());
    }
    if (Objects.nonNull(request.getDob())) {
      seller.setDob(request.getDob());
    }
    if (Objects.nonNull(request.getAddressId())) {
      seller.setAddressId(request.getAddressId());
    }
  }

  private SellerResponse toDTO(Seller seller) {
    return SellerResponse.builder()
          .id(seller.getId())
          .brandName(seller.getBrandName())
          .fullName(seller.getFullName())
          .phoneNumber(seller.getPhoneNumber())
          .email(seller.getEmail())
          .dob(seller.getDob())
          .build();
  }
}
