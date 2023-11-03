package org.freedomfinancestack.razorpay.cas.dao.model;

import org.freedomfinancestack.razorpay.cas.dao.enums.OtpVerificationStatus;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "otp_transaction_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class OtpTransactionDetail extends BaseEntity<String> {
    @Id private String id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "verification_status")
    @Enumerated(EnumType.STRING)
    private OtpVerificationStatus verificationStatus;

    @Column(name = "value")
    private String value;
}
