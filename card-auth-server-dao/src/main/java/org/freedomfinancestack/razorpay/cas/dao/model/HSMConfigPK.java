package org.freedomfinancestack.razorpay.cas.dao.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class HSMConfigPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "institution_id", length = 4)
    private String institutionId;

    @Column(name = "network", length = 2)
    private byte networkId;
}
