package com.razorpay.acs.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "network")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class Network extends BaseEntity<String>  {
    @Id
    private String id;

    @Column(nullable = false)
    private Byte code;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

}