package com.razorpay.acs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

import java.sql.Timestamp;

@Entity
@Table(name = "otp_information")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE otp_information SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE otp_information SET deleted_at = now() ")
public class OtpInformation {
    @Id
    private String id;

    @Column(name = "unique_id")
    private String uniqueId;

}
