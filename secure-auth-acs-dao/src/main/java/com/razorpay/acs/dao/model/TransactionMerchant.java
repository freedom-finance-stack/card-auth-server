package com.razorpay.acs.dao.model;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import lombok.*;

@Entity
@Data
@Table(name = "transaction_merchant")
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@Builder
@ToString(exclude = {"transaction"})
public class TransactionMerchant extends BaseEntity<String> {
  @Id
  @Column(name = "transaction_id")
  private String id;

  @Column(name = "acquirer_merchant_id")
  private String acquirerMerchantId;

  @Column(name = "merchant_name")
  private String merchantName;

  @Column(name = "merchant_country_code")
  private Short merchantCountryCode;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "transaction_id", referencedColumnName = "id")
  @MapsId
  private Transaction transaction;
}
