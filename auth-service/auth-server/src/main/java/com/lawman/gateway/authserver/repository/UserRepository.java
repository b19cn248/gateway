package com.lawman.gateway.authserver.repository;

import com.lawman.gateway.authserver.entity.User;

public interface UserRepository extends BaseRepository<User> {

  boolean existsByPhoneNumber(String phoneNumber);

  boolean existsByEmail(String email);
}
