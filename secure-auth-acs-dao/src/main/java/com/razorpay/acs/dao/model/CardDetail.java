package com.razorpay.acs.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "card_detail")
@Table(name = "card_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
// need to add this where clause in all the entities to make soft delete work, as Where is non inherited annotation couldn't find easy way
public class CardDetail extends BaseEntity  {
    @Id
    private String id;

    @Column(name = "cardholder_id", nullable = false)
    private String cardholderId;

    @Column(name = "range_id", nullable = false)
    private String rangeId;

    @Column(name = "institution_id", nullable = false)
    private String institutionId;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_expiry")
    private String cardExpiry;

    private Boolean blocked;

    @Column(name = "network_code")
    private String networkCode;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

}