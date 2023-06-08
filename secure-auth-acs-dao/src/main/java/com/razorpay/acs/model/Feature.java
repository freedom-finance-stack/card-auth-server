package com.razorpay.acs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "features")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE features SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE features SET deleted_at = now() ")
public class Feature {
    @Id
    private String id;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    private boolean active;

    private String name;

    private String properties;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

}
