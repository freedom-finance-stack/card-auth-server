package com.razorpay.acs.dao.model;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "transaction_browser_detail")
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@Builder
public class TransactionBrowserDetail extends BaseEntity<String> {

  @Id
  // @Column(name = "transaction_id")
  private String id;

  @Column(name = "javascript_enabled")
  private Boolean javascriptEnabled;

  @Column(name = "ip")
  private String ip;

  @Column(name = "accept_header")
  private String acceptHeader;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "transaction_id", referencedColumnName = "id")
  @MapsId
  private Transaction transaction;
}
