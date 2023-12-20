package com.lawman.gateway.authserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends User {
  private Double bonusCoin;
  private Integer status;
  private String sellerPhoneNumber;

  public Customer(String id, String fullName, String phoneNumber, String email, String dob, Integer status, String sellerPhoneNumber) {
    super(id, fullName, phoneNumber, email, dob);
    this.bonusCoin = 0.0;
    this.status = status;
    this.sellerPhoneNumber = sellerPhoneNumber;
  }


}
