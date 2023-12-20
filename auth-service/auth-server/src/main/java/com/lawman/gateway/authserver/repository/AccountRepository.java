package com.lawman.gateway.authserver.repository;

import com.lawman.gateway.authserver.entity.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccountRepository extends BaseRepository<Account> {

  boolean existsByUsername(String username);

  Optional<Account> findAccountByUsername(String username);

  @Query(value = "SELECT r.name from Account  a JOIN Role r ON a.id = :id and a.roleId = r.id")
  String getRole(String id);

  @Query("UPDATE Account a set a.failedAttempt = ?2 where a.id = ?1")
  @Transactional
  @Modifying
  void increaseFailedAttempts(String id, int failedAttempt);

  @Query("UPDATE Account a set a.failedAttempt = 0 , a.lockTime = null where a.id = :id")
  @Transactional
  @Modifying
  void reset(String id);

  @Query("SELECT a from Account a join User u on a.id = u.id and u.email = :email")
  Optional<Account> findByEmail(String email);

  Optional<Account> findByResetPasswordToken(String token);

  @Query("UPDATE Account a set a.password = :password where a.id = :id")
  @Transactional
  @Modifying
  void updatePassword(String id, String password);
}
