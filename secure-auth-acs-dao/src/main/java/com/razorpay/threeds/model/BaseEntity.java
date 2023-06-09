package com.razorpay.threeds.model;

import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@MappedSuperclass
@Where(clause = "deleted_at is null") // todo test this
abstract  public class BaseEntity implements Serializable {
    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "modified_at", nullable = false)
    @UpdateTimestamp
    private Timestamp modifiedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;
}
