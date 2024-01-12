package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.OperationNotSupportedException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.OOBService;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.OOBServiceLocator;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.impl.MockOOBService;
import org.freedomfinancestack.razorpay.cas.acs.service.oob.impl.ULTestOOBService;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.enums.OOBType;
import org.freedomfinancestack.razorpay.cas.dao.model.OOBConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.freedomfinancestack.razorpay.cas.acs.data.AuthConfigTestData.createAuthConfigDto;
import static org.freedomfinancestack.razorpay.cas.acs.data.AuthConfigTestData.createAuthenticationDto;
import static org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData.createSampleAppTransaction;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OOBAuthenticationServiceTest {
    @Mock OOBServiceLocator oobServiceLocator;
    @InjectMocks OOBAuthenticationService oobAuthenticationService;

    @Test
    void authenticate_Failure_isNull() throws ThreeDSException{

        Transaction transaction = Transaction.builder().id("1").build();
        AuthenticationDto authenticationDto1 =
                AuthenticationDto.builder().authConfigDto(null).transaction(transaction).build();
        OperationNotSupportedException exception1 =
                assertThrows(
                        OperationNotSupportedException.class,
                        () -> oobAuthenticationService.authenticate(authenticationDto1));
        assertEquals("Missing OOB Type", exception1.getMessage());

        AuthConfigDto authConfigDto = new AuthConfigDto();
        AuthenticationDto authenticationDto2 =
                AuthenticationDto.builder()
                        .authConfigDto(authConfigDto)
                        .transaction(transaction)
                        .build();
        authenticationDto2.getAuthConfigDto().setOobConfig(null);
        OperationNotSupportedException exception2 =
                assertThrows(
                        OperationNotSupportedException.class,
                        () -> oobAuthenticationService.authenticate(authenticationDto2));
        assertEquals("Missing OOB Type", exception2.getMessage());

        authConfigDto.setOobConfig(new OOBConfig());
        AuthenticationDto authenticationDto3 =
                AuthenticationDto.builder()
                        .authConfigDto(authConfigDto)
                        .transaction(transaction)
                        .build();
        authenticationDto3.getAuthConfigDto().getOobConfig().setOobType(null);
        OperationNotSupportedException exception3 =
                assertThrows(
                        OperationNotSupportedException.class,
                        () -> oobAuthenticationService.authenticate(authenticationDto3));
        assertEquals("Missing OOB Type", exception3.getMessage());
    }

    /**
     * checks the authenticate function when OOB Type is UL_TEST or MOCk;
     *
     * @throws ThreeDSException
     */
    @ParameterizedTest
    @CsvSource({"1", "2"})
    void authenticateSuccessTest(String oob) throws ThreeDSException {

        OOBType oobType = OOBType.getOOBType(Integer.parseInt(oob));

        AuthConfigDto authConfigDto1 =
                createAuthConfigDto(oobType, true, true, AuthType.OTP, AuthType.UNKNOWN);
        AuthenticationDto authenticationDto =
                createAuthenticationDto(authConfigDto1, createSampleAppTransaction(), "");
        authenticationDto.setAuthConfigDto(authConfigDto1);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticated(Boolean.TRUE);
        authResponse.setDisplayMessage(InternalConstants.CHALLENGE_CORRECT_OTP_TEXT);

        OOBService oobService =
                oobType.getValue() == 1 ? mock(ULTestOOBService.class) : mock(MockOOBService.class);
        when(oobServiceLocator.locateOOBService(oobType)).thenReturn(oobService);
        when(oobService.authenticate(authenticationDto)).thenReturn(authResponse);
        AuthResponse actualAuthResponse = oobAuthenticationService.authenticate(authenticationDto);
        assertNotNull(actualAuthResponse);
        assertEquals(authResponse.getDisplayMessage(), actualAuthResponse.getDisplayMessage());
        assertTrue(authResponse.isAuthenticated());
    }
}
