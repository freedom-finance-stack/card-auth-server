package org.freedomfinancestack.razorpay.cas.dao.model;

import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "otp")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class Otp extends BaseEntity<String> {
    @Id private String id;

    private String channel;

    @Column(name = "otp_information_id", nullable = false)
    private String otpInformationId;

    private String destination;

    @Column(name = "otp_status")
    private String otpStatus;

    private String response;

    private String provider;

    private Integer attempts;
}
