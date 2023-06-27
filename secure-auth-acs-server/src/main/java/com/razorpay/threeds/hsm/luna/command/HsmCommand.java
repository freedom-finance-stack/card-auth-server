package com.razorpay.threeds.hsm.luna.command;

import com.razorpay.threeds.hsm.luna.domain.InternalHsmMsg;

public abstract class HsmCommand {
  public String cmdCode;
  public String functionModifier;
  public int cmdLength;
  public InternalHsmMsg internalMsg;

  public abstract void initialize(InternalHsmMsg internalMsg);

  public abstract byte[] encode();

  public abstract byte[] decode();

  public abstract int processResponse(byte[] respMsg);

  public abstract byte[] serialize();

  public abstract int getCmdLength();
}
