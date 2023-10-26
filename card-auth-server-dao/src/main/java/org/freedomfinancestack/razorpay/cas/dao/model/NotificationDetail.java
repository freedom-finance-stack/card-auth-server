package org.freedomfinancestack.razorpay.cas.dao.model;


import jakarta.persistence.*;
import org.freedomfinancestack.razorpay.cas.dao.enums.NotificationStatus;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class NotificationDetail extends BaseEntity<String> {
    @Id private String id;

    private String channel;

    private String entity_type;

    @Column(name = "entity_id", nullable = false)
    private String entity_id;

    private String destination;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private String responses;

    private String provider;

    private Integer attempts;
}
