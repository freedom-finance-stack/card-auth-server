package org.freedomfinancestack.razorpay.cas.dao.model;

import javax.persistence.*;

import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureEntityType;
import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feature")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class Feature extends BaseEntity<String> {
    @Id private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    private FeatureEntityType entityType;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private FeatureName name;

    private String properties;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;
}
