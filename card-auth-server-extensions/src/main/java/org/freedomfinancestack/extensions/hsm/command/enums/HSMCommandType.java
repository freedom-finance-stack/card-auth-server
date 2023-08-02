package org.freedomfinancestack.extensions.hsm.command.enums;

import java.util.Objects;

public enum HSMCommandType {
    NO_OP_HSM(HSMCommandTypeConstants.NO_OP_HSM),

    LUNA_HSM(HSMCommandTypeConstants.LUNA_HSM);

    HSMCommandType(String name) {
        this.name = name;
    }

    private final String name;

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Function to return specified {@link HSMCommandType} with respective name
     *
     * @param name of the HSM Command {@link String}
     * @return {@link HSMCommandType}
     */
    public static HSMCommandType fromStringType(String name) {
        for (HSMCommandType commandType : HSMCommandType.values()) {
            if (Objects.equals(commandType.name, name)) {
                return commandType;
            }
        }
        throw new IllegalArgumentException("Invalid HSMCommandType constant: " + name);
    }

    public interface HSMCommandTypeConstants {

        String NO_OP_HSM = "NoOpHSM";

        String LUNA_HSM = "LunaHSM";
    }
}
