package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.OperationNotSupportedException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.OOBServiceLocator;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.impl.MockOOBService;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.impl.ULTestOOBService;
import org.freedomfinancestack.razorpay.cas.dao.enums.OOBType;
import org.freedomfinancestack.razorpay.cas.dao.model.OOBConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.freedomfinancestack.razorpay.cas.acs.data.AuthConfigDtoData.createAuthConfigDto;
import static org.freedomfinancestack.razorpay.cas.acs.data.AuthenticationDtoData.createAuthenticationDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OOBAuthenticationServiceTest {
    @Mock
    Logger log;
    @Mock
    OOBServiceLocator oobServiceLocator;
    @InjectMocks
    OOBAuthenticationService oobAuthenticationService;


    /**
     * Check the case if condition case
     * @throws ThreeDSException
     */

    @Test
    void authenticateExceptionSuccess() throws ThreeDSException {

        Transaction transaction = Transaction.builder().id("1").build();
        AuthenticationDto authenticationDto1 = AuthenticationDto.builder().authConfigDto(null).transaction(transaction).build();
        OperationNotSupportedException exception1 =  assertThrows(OperationNotSupportedException.class ,()-> oobAuthenticationService.authenticate(authenticationDto1));
        assertEquals("Missing OOB Type", exception1.getMessage());

        AuthConfigDto authConfigDto = new AuthConfigDto();
        AuthenticationDto authenticationDto2 = AuthenticationDto.builder().authConfigDto(authConfigDto).transaction(transaction).build();
        authenticationDto2.getAuthConfigDto().setOobConfig(null);
        OperationNotSupportedException exception2 =  assertThrows(OperationNotSupportedException.class ,()-> oobAuthenticationService.authenticate(authenticationDto2));
        assertEquals("Missing OOB Type", exception2.getMessage());

        authConfigDto.setOobConfig(new OOBConfig());
        AuthenticationDto authenticationDto3 = AuthenticationDto.builder().authConfigDto(authConfigDto).transaction(transaction).build();
        authenticationDto3.getAuthConfigDto().getOobConfig().setOobType(null);
        OperationNotSupportedException exception3 =  assertThrows(OperationNotSupportedException.class ,()-> oobAuthenticationService.authenticate(authenticationDto3));
        assertEquals("Missing OOB Type", exception3.getMessage());

    }


    @Mock
    ULTestOOBService ulTestOOBService;

    /**
     * checks the authenticate function when OOB Type is UL_TEST;
     * @throws ThreeDSException
     */

    @Test
    void authenticateSuccessTest_ULTEST() throws ThreeDSException {
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        OOBType oobType = OOBType.getOOBType(1);

        AuthConfigDto authConfigDto1 = createAuthConfigDto(oobType);
        AuthenticationDto authenticationDto = createAuthenticationDto(authConfigDto1);
        authenticationDto.setAuthConfigDto(authConfigDto1);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticated(Boolean.TRUE);
        authResponse.setDisplayMessage(InternalConstants.CHALLENGE_CORRECT_OTP_TEXT);

        when(oobServiceLocator.locateOOBService(OOBType.valueOf("UL_TEST"))).thenReturn(ulTestOOBService);
        when(ulTestOOBService.authenticate(authenticationDto)).thenReturn(authResponse);
        AuthResponse actualAuthResponse = oobAuthenticationService.authenticate(authenticationDto);
        assertNotNull(actualAuthResponse);
        assertEquals(authResponse.getDisplayMessage(), actualAuthResponse.getDisplayMessage());
    }
    @Mock
    MockOOBService mockOOBService;

    /**
     * checks the authenticate function when OOB Type is MOCK;
     * @throws ThreeDSException
     */
    @Test
    void authenticateSuccessTest_MockTest() throws ThreeDSException {
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        OOBType oobType = OOBType.getOOBType(2);

        AuthConfigDto authConfigDto1 = createAuthConfigDto(oobType);
        AuthenticationDto authenticationDto = createAuthenticationDto(authConfigDto1);
        authenticationDto.setAuthConfigDto(authConfigDto1);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticated(Boolean.TRUE);
        authResponse.setDisplayMessage(InternalConstants.CHALLENGE_CORRECT_OTP_TEXT);

        when(oobServiceLocator.locateOOBService(OOBType.MOCK)).thenReturn(mockOOBService);
        when(mockOOBService.authenticate(authenticationDto)).thenReturn(authResponse);
        AuthResponse actualAuthResponse = oobAuthenticationService.authenticate(authenticationDto);
        assertNotNull(actualAuthResponse);
        assertEquals(authResponse.getDisplayMessage(), actualAuthResponse.getDisplayMessage()); // oobType -> UL_TEST
    }

}