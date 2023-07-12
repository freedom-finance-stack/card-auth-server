package com.razorpay.acs.dao.model;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import com.razorpay.acs.dao.enums.OtpVerificationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "otp_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class OtpDetail extends BaseEntity<String> {
    @Id private String id;

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
