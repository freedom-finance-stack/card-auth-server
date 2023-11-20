package org.freedomfinancestack.razorpay.cas.dao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.freedomfinancestack.razorpay.cas.contract.enums.UIType;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionUiConfigPK  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "institution_id")
    private String institutionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private AuthType authType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ui_type")
    private UIType uiType;

}
