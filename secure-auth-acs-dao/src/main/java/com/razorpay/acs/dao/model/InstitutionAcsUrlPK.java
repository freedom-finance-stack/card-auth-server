package com.razorpay.acs.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

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
