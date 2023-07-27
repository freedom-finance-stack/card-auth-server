package org.ffs.razorpay.cas.acs.constant;

import java.util.Date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LunaHSMConstants {

    public static final String LUNA_HSM = "LunaHSM";

    public static final String INTERNAL_DIAGNOSTIC_CMD = "00";

    public static final String INTERNAL_CVV_GENERATE_CMD = "01";

    public static final String DIAGNOSTIC_CMD_01 = "01";

    public static final String HSM_SUCCESSFUL_RESPONSE = "00";

    public static final String EE0802_CMD = "EE0802";

    public static final String FUNC_MODIFIER_00 = "00";

    public static final String HSM_VERSION_HEADER = "0101";

    public static volatile long hsmEchoTime = 0;

    public static synchronized void setHSMEchoTime() {
        hsmEchoTime = new Date().getTime();
    }
}
