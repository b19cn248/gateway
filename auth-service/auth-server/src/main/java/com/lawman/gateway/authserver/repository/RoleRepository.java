package com.lawman.gateway.authserver.repository;


import com.lawman.gateway.authserver.entity.Role;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends BaseRepository<Role> {

  @Query("SELECT r.id from Role r where r.name = :name")
  String getIdByRole(String name);
}
