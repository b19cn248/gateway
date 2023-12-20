package com.lawman.gateway.authserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seller extends User {
  private String brandName;
  private String warehouseId;

  public Seller(String id, String fullName, String phoneNumber, String email, String dob, String brandName) {
    super(id, fullName, phoneNumber, email, dob);
    this.brandName = brandName;
  }
}
