package org.ffs.razorpay.cas.dao.model;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "network")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class Network extends BaseEntity<String> {
    @Id private String id;

    @Column(nullable = false)
    private Byte code;

    @Enumerated(EnumType.STRING)
    private org.ffs.razorpay.cas.dao.enums.Network name;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;
}
