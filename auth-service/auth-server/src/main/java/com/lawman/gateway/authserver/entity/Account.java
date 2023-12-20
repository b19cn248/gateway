package com.lawman.gateway.authserver.entity;

import com.lawman.gateway.authserver.entity.base.BaseEntityWithUpdater;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntityWithUpdater {
  private String username;
  private String password;
  @Column(name = "is_active")
  private Boolean isActive;

  @Column(name = "is_deleted")
  private Boolean isDeleted;
  private Integer failedAttempt;
  private Long lockTime;
  private String resetPasswordToken;
  private String roleId;

  public Account(String id, String username, String password, String roleId, int failedAttempt) {
    super.setId(id);
    this.username = username;
    this.password = password;
    this.roleId = roleId;
    this.failedAttempt = 0;
  }
}
