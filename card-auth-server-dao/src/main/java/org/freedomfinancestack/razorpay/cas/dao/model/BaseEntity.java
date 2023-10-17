package org.freedomfinancestack.razorpay.cas.dao.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseEntity<T> implements Serializable {
    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "modified_at", nullable = false)
    @UpdateTimestamp
    private Timestamp modifiedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    public abstract T getId();
}
