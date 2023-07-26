package org.ffs.razorpay.cas.dao.model;

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
