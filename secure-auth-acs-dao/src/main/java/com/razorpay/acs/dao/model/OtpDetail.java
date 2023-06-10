package com.razorpay.acs.dao.model;

import com.razorpay.acs.dao.enums.OtpVerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "otp_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class OtpDetail extends BaseEntity {
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

