package org.freedomfinancestack.razorpay.cas.acs.service.parser;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.freedomfinancestack.razorpay.cas.acs.service.parser.impl.AppChallengeRequestParser;
import org.freedomfinancestack.razorpay.cas.acs.service.parser.impl.BrowserChallengeRequestParser;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeRequestParserFactory {
    private final BrowserChallengeRequestParser browserChallengeRequestParser;
    private final AppChallengeRequestParser appChallengeRequestParser;

    public ChallengeRequestParser getService(DeviceChannel deviceChannel)
            throws TransactionDataNotValidException {
        switch (deviceChannel) {
            case APP -> {
                return appChallengeRequestParser;
            }
            case BRW -> {
                return browserChallengeRequestParser;
            }
        }
        throw new TransactionDataNotValidException(InternalErrorCode.INVALID_REQUEST);
    }
}
