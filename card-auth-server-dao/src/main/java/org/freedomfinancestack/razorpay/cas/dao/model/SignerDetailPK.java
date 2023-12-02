package org.freedomfinancestack.razorpay.cas.dao.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SignerDetailPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "institution_id")
    private String institutionId;

    @Column(name = "network_code", length = 2)
    private byte networkCode;
}
