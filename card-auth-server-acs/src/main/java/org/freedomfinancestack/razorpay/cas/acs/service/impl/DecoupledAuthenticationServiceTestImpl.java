package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.DecoupledAuthenticationRequest;
import org.freedomfinancestack.razorpay.cas.acs.dto.DecoupledAuthenticationResponse;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul.POrqService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul.POrs;
import org.freedomfinancestack.razorpay.cas.acs.service.DecoupledAuthenticationService;
import org.freedomfinancestack.razorpay.cas.acs.service.ResultRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionService;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("DecoupledAuthenticationServiceTest")
@RequiredArgsConstructor
public class DecoupledAuthenticationServiceTestImpl implements DecoupledAuthenticationService {
    private final POrqService porqService;
    private final TransactionService transactionService;
    private final ResultRequestService resultRequestService;

    @Override
    public DecoupledAuthenticationResponse processAuthenticationRequest(
            Transaction transaction, DecoupledAuthenticationRequest decoupledAuthenticationRequest)
            throws ThreeDSException, ACSDataAccessException {
        POrs pores =
                porqService.sendPOrq(
                        transaction.getId(),
                        transaction.getTransactionReferenceDetail().getThreedsServerTransactionId(),
                        transaction.getMessageVersion());
        if (pores == null) {
            return null;
        }
        return new DecoupledAuthenticationResponse(pores.isP_isOobSuccessful());
    }
}
