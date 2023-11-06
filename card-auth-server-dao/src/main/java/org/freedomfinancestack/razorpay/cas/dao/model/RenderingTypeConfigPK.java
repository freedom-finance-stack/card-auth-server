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
public class RenderingTypeConfigPK implements Serializable {
    @Column(name = "institution_id")
    private String institutionId;

    @Column(name = "card_range_id")
    private String cardRangeId;

    @Column(name = "acs_interface")
    private String acsInterface;

    @Column(name = "acs_ui_template")
    private String acsUiTemplate;
}
