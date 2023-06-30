package com.razorpay.threeds.constant;

public enum AuthenticationMethod {
  THREE_DS_1_0_2_FRICTIONLESS_FLOW("00", "0"),
  CHALLENGE_FLOW_USING_STATIC_PASSCODE("01", "1"),
  CHALLENGE_FLOW_USING_OTP_VIA_SMS("02", "2"),
  CHALLENGE_FLOW_USING_OTP_VIA_KEY("03", "3"),
  CHALLENGE_FLOW_USING_OTP_VIA_APP("04", "4"),
  CHALLENGE_FLOW_USING_OTP_VIA_OTHER("05", "5"),
  CHALLENGE_FLOW_USING_KBA("06", "6"),
  CHALLENGE_FLOW_USING_OOB_WITH_BIOMATRIC("07", "7"),
  CHALLENGE_FLOW_USING_OOB_WITH_APP("08", "8"),
  CHALLENGE_FLOW_USING_OOB_WITH_OTHER("09", "9"),
  CHALLENGE_FLOW_USING_OTHER_METHOD("10", "A"),
  PUSH_CONFIRMATION("11", "C"),
  ISSUER_ACS_AUTH_METHOD_1("92", "G"),
  ISSUER_ACS_AUTH_METHOD_2("93", "H"),
  ISSUER_ACS_AUTH_METHOD_3("94", "I"),
  ISSUER_ACS_AUTH_METHOD_4("95", "J"),
  ISSUER_ACS_AUTH_METHOD_5("96", "K"),
  FRICTIONLESS_RBA_REVIEW("97", "D"),
  ATTEMPT_SERVER_RESPONDING("98", "E"),
  FRICTIONLESS_RBA("99", "F"),
  ;

  private final String code;
  private final String indicator;

  private AuthenticationMethod(String code, String indicator) {
    this.code = code;
    this.indicator = indicator;
  }

  public String getCode() {
    return code;
  }

  public String getIndicator() {
    return indicator;
  }
}
