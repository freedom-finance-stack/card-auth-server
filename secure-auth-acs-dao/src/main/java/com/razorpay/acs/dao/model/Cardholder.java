package com.razorpay.acs.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cardholder")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@SoftDelatable("cardholders") // todo combiane this annotation in one.. Combine with table as well
@SQLDelete(sql = "UPDATE cardholders SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE cardholders SET deleted_at = now() ")
public class Cardholder {
    @Id
    private String id;

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

