package org.freedomfinancestack.extensions.hsm.command.factory;

import org.freedomfinancestack.extensions.hsm.command.HSMCommand;
import org.freedomfinancestack.extensions.hsm.command.enums.HSMCommandType;

public interface HSMCommandFactory {

    HSMCommand getHSMCommand(HSMCommandType hsmCommandType);
}
