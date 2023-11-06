package org.freedomfinancestack.razorpay.cas.dao.model;

import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rendering_type_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class RenderingTypeConfig extends BaseEntity<RenderingTypeConfigPK> {

    @EmbeddedId private RenderingTypeConfigPK renderingTypeConfigPK;

    @Column(name = "default_render_option")
    private String defaultRenderOption;

    @Column(name = "acs_ui_type")
    private String acsUiType;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    public RenderingTypeConfigPK getId() {
        return renderingTypeConfigPK;
    }

    public void setId(RenderingTypeConfigPK renderingTypeConfigPK) {
        this.renderingTypeConfigPK = renderingTypeConfigPK;
    }
}
