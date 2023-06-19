package com.razorpay.acs.dao.model;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Data
@Table(name = "transaction_browser_detail")
@ToString(exclude = {"transaction"})
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@Builder
public class TransactionBrowserDetail extends BaseEntity<String> {

    @Id
    //@Column(name = "transaction_id")
    private String id;

    @Column(name = "javascript_enabled")
    private Boolean javascriptEnabled;

    @Column(name = "ip")
    private String ip;

    @Column(name = "accept_header")
    private String acceptHeader;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    @MapsId
    private Transaction transaction ;

}
