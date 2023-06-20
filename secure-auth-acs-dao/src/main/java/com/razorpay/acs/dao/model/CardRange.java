package com.razorpay.acs.dao.model;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import com.razorpay.acs.dao.enums.AuthType;
import com.razorpay.acs.dao.enums.CardDetailsStore;
import com.razorpay.acs.dao.enums.CardRangeStatus;
import com.razorpay.acs.dao.enums.CardType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_range")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class CardRange extends BaseEntity<String> {
  @Id private String id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "range_group_id", referencedColumnName = "id")
  @MapsId
  private CardRangeGroup cardRangeGroup;

  @Column(name = "start_range")
  private Long startRange;

  @Column(name = "end_range")
  private Long endRange;

  @Column(name = "attempt_allowed")
  private Byte attemptAllowed;

  @Column(name = "block_on_exceed_attempt", nullable = false)
  private Byte blockOnExceedAttempt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private CardRangeStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "card_type", nullable = false)
  private CardType cardType;

  @Enumerated(EnumType.STRING)
  @Column(name = "auth_type", nullable = false)
  private AuthType authType;

  @Column(name = "description")
  private String description;

  @Column(name = "whitelisting_allowed")
  private boolean whitelistingAllowed;

  @Enumerated(EnumType.STRING)
  @Column(name = "card_details_store")
  private CardDetailsStore cardDetailsStore;

  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @Column(name = "modified_by")
  private String modifiedBy;

  @Column(name = "deleted_by")
  private String deletedBy;
}
