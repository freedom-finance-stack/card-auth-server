package org.freedomfinancestack.extensions.hsm.luna.utils;

import java.util.Date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LunaConstants {

    public static final String EE0802_CMD = "EE0802";

    public static final String FUNC_MODIFIER_00 = "00";

    public static final String HSM_VERSION_HEADER = "0101";

    public static final String LUNA_EFT_HSM = "LunaEFT";

    public static final int HSM_HEADER_LENGTH = 4;

    public static final String HSM_SUCCESSFUL_RESPONSE = "00";

    public static volatile long hsmEchoTime = 0;

    public static synchronized void setHSMEchoTime() {
        hsmEchoTime = new Date().getTime();
    }
}
