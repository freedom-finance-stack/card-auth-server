package com.razorpay.acs.dao.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class InstitutionAcsUrlPK implements Serializable {

  @Column(name = "institution_id")
  private String institutionId;

  @Column(name = "device_channel")
  private String deviceChannel;

  @Column(name = "network_code")
  private String networkCode;
}
