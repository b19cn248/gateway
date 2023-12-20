package com.lawman.gateway.authserver.service.impl;


import com.lawman.gateway.authserver.entity.Role;
import com.lawman.gateway.authserver.repository.BaseRepository;
import com.lawman.gateway.authserver.service.RoleService;
import com.lawman.gateway.authserver.service.base.BaseServiceImpl;

public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
  public RoleServiceImpl(BaseRepository<Role> repository) {
    super(repository);
  }
}
