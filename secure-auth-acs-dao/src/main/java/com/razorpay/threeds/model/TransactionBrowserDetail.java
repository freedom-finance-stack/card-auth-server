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
@Data
@Table(name = "transaction_browser_detail")
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE transaction_browser_details SET deleted_at = now() WHERE id=?")
@SQLDeleteAll( sql="UPDATE transaction_browser_details SET deleted_at = now() ")
public class TransactionBrowserDetail {
    @Id
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "javascript_enabled")
    private Boolean javascriptEnabled;

    @Column(name = "ip")
    private String ip;

    @Column(name = "accept_header")
    private String acceptHeader;
}
