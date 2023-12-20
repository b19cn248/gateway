package com.lawman.gateway.authserver.entity.base;

import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
public class BaseEntityWithUpdater extends BaseEntity {

  @LastModifiedBy
  private String lastUpdatedBy;

  @LastModifiedDate
  private Long lastUpdatedAt;

}

