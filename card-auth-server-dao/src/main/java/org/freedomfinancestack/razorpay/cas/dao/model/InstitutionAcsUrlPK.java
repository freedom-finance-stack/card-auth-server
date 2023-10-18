package org.freedomfinancestack.razorpay.cas.dao.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionAcsUrlPK implements Serializable {

    @Column(name = "institution_id")
    private String institutionId;

    @Column(name = "device_channel")
    private String deviceChannel;

    @Column(name = "network_code")
    private Byte networkCode;
}
