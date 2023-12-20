package com.lawman.gateway.authserver.repository;

import com.lawman.gateway.authclient.response.seller.SellerResponse;
import com.lawman.gateway.authserver.entity.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SellerRepository extends BaseRepository<Seller> {
  boolean existsByPhoneNumber(String phoneNumber);

  boolean existsByEmail(String email);

  boolean existsByBrandName(String brandName);

  @Query("""
        SELECT new com.lawman.gateway.authclient.response.seller.SellerResponse
        (s.id, s.brandName, a.username, s.fullName, s.phoneNumber, s.email, s.dob)
        FROM Seller s JOIN Account a on a.id = s.id
        WHERE (:keyword IS NULL OR :keyword = '' OR a.username LIKE %:keyword%) and s.isDeleted = false\s
        """)
  List<SellerResponse> list(String keyword, Pageable pageable);

  @Query("""
        SELECT new com.lawman.gateway.authclient.response.seller.SellerResponse
        (s.id, s.brandName, a.username, s.fullName, s.phoneNumber, s.email, s.dob)
        FROM Seller s JOIN Account a on a.id = s.id
        WHERE (:keyword IS NULL OR :keyword = '' OR a.username LIKE %:keyword%) and s.isDeleted = false\s
        """)
  List<SellerResponse> listAll(String keyword);

  @Query("""
        SELECT count(s)
        FROM Seller s JOIN Account a on a.id = s.id
        WHERE (:keyword IS NULL OR :keyword = '' OR a.username LIKE %:keyword%) and s.isDeleted = false\s
        """)
  Integer count(String keyword);

  @Query("""
        SELECT new com.lawman.gateway.authclient.response.seller.SellerResponse
        (s.id, s.brandName, a.username, s.fullName, s.phoneNumber, s.email, s.dob)
        FROM Seller s JOIN Account a on a.id = s.id
        WHERE a.id = :id
        """)
  Optional<SellerResponse> detail(String id);

  @Query("""
        UPDATE Seller s set s.isDeleted = true where s.id = :id
        """)
  @Modifying
  @Transactional
  void softDelete(String id);

}
