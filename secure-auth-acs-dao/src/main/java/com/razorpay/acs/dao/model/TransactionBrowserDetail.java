package com.razorpay.acs.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Data
@Table(name = "transaction_browser_detail")
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class TransactionBrowserDetail extends BaseEntity {

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
