package com.razorpay.acs.dao.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class HSMConfigPK implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "institution_id", length = 4)
  private String institutionId;

  @Column(name = "network", length = 2)
  private String networkId;
}
