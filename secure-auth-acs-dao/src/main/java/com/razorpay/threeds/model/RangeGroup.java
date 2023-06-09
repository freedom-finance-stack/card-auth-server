package com.razorpay.threeds.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;

@Entity
@Table(name = "range_groups")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SQLDelete(sql = "UPDATE range_groups SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE range_groups SET deleted_at = now() ")
public class RangeGroup {
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

    @ManyToMany
    @JoinTable(name = "tlds_idn_mappings", joinColumns = @JoinColumn(name = ID), inverseJoinColumns =
    @JoinColumn(name = IDNLanguage.LANGUAGE_ID))
    private Set<IDNLanguage> idnLanguages;


}

