package com.lawman.gateway.authserver.entity;

import com.lawman.gateway.authserver.entity.base.BaseEntityWithUpdater;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntityWithUpdater {
  private String fullName;
  private String phoneNumber;
  private String email;
  private String dob;
  private String addressId;
  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  public User(String id, String fullName, String phoneNumber, String email, String dob) {
    super.setId(id);
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.dob = dob;
  }
}
