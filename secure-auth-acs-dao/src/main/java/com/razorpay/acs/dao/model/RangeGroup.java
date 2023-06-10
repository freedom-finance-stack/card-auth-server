package com.razorpay.acs.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "range_group")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Where(clause = "deleted_at is null")
public class RangeGroup extends BaseEntity {
    @Id
    private String id;

    @Column(name = "institution_id")
    private String institutionId;

    private String name;

    private String description;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

//    @ManyToMany
//    @JoinTable(name = "tlds_idn_mappings", joinColumns = @JoinColumn(name = ID), inverseJoinColumns =
//    @JoinColumn(name = IDNLanguage.LANGUAGE_ID))
//    private Set<IDNLanguage> idnLanguages;


}

