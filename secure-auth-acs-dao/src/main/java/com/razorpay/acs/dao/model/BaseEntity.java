package com.razorpay.acs.dao.model;

import com.razorpay.acs.dao.annotation.SoftDeleteRead;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@MappedSuperclass
//@SoftDeleteRead
abstract public class BaseEntity<T> implements Serializable {
    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "modified_at", nullable = false)
    @UpdateTimestamp
    private Timestamp modifiedAt;

    @Column(name = "deleted_at")
    private Timestamp deleted_at;

    public abstract T getId() ;
}
