package com.lawman.gateway.authserver.repository;

import com.lawman.gateway.authclient.response.customer.CustomerResponse;
import com.lawman.gateway.authserver.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends BaseRepository<Customer> {

  boolean existsByEmail(String email);

  boolean existsByPhoneNumber(String phoneNumber);

  @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END\s
        FROM Seller s WHERE s.phoneNumber = :sellerPhone
        """)
  boolean existsSellerPhone(String sellerPhone);

  @Query("""
        select new com.lawman.gateway.authclient.response.customer.CustomerResponse\s
        (c.id, c.bonusCoin, c.status, c.sellerPhoneNumber, a.username, c.fullName, c.phoneNumber, c.email, c.dob)\s
        from Customer c join Account a on c.id = a.id\s
          WHERE (:keyword IS NULL OR :keyword = '' OR a.username LIKE %:keyword%) and c.isDeleted = false\s
        """)
  List<CustomerResponse> getAll(String keyword);

  @Query(value = """
        select count(c) FROM Customer c JOIN Account a ON c.id = a.id\s
        WHERE :keyword is null  or lower(c.fullName) LIKE %:keyword% or lower(a.username) LIKE %:keyword%\s
        and c.isDeleted = false\s
        """)
  Integer countSearch(String keyword);

  @Query("""
        select new com.lawman.gateway.authclient.response.customer.CustomerResponse\s
        (c.id, c.bonusCoin, c.status, c.sellerPhoneNumber, a.username, c.fullName, c.phoneNumber, c.email, c.dob)\s
        from Customer c join Account a on c.id = a.id\s
        where :keyword is null or lower(c.fullName) LIKE %:keyword% or lower(a.username) LIKE %:keyword%
        and c.isDeleted = false\s
        """)
  List<CustomerResponse> searchByKeyword(String keyword, Pageable pageable);


  @Query("""
        select new com.lawman.gateway.authclient.response.customer.CustomerResponse\s
        (c.id, c.bonusCoin, c.status, c.sellerPhoneNumber, a.username, c.fullName, c.phoneNumber, c.email, c.dob)\s
        from Customer c join Account a on c.id = a.id and c.id = :id\s
        """)
  Optional<CustomerResponse> detail(String id);

  @Query("""
        UPDATE Customer c set c.isDeleted = true where c.id = :id
        """)
  @Modifying
  @Transactional
  void softDelete(String id);
}
