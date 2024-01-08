package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.DecoupledAuthenticationRequest;
import org.freedomfinancestack.razorpay.cas.acs.dto.DecoupledAuthenticationResponse;
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

    // NOTE: This approach assume that Decoupled authentication will finish in SYNC request, it will
    // get response in same request.
    // Ideally, to make it true async we need to expose endpoint that authentication server will
    // call after completing authentication,
    // meanwhile timer thread might mark transaction timeout in case of time taken is longer. 2nd
    // approach should be considered for decoupled authentication
    // add factory method, once more than one implementation
    @Override
    public DecoupledAuthenticationResponse processAuthenticationRequest(
            Transaction transaction, DecoupledAuthenticationRequest decoupledAuthenticationRequest)
            throws ThreeDSException {
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
