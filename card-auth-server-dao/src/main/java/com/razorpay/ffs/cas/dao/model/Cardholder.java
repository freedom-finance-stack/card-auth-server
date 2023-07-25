package com.razorpay.ffs.cas.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cardholder")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class Cardholder extends BaseEntity<String> {
    @Id private String id;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "email_id")
    private String emailId;

    private String dob;

    private String name;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;
}
