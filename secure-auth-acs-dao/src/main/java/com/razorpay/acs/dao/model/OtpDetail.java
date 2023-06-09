package com.razorpay.acs.dao.model;

import com.razorpay.acs.dao.enums.OtpVerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

import javax.persistence.*;

@Entity
@Table(name = "otp_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE otp_details SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE otp_details SET deleted_at = now() ")
public class OtpDetail {
    @Id
    private String id;

    @Column(name = "otp_id", nullable = false)
    private String otpId;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "verification_status")
    @Enumerated(EnumType.STRING)
    private OtpVerificationStatus verificationStatus;

    @Column(name = "resend_count")
    private Integer resendCount;

}

