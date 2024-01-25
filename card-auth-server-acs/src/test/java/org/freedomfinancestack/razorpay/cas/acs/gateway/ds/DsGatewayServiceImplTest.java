 package org.freedomfinancestack.razorpay.cas.acs.gateway.ds;

 import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
 import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;
 import org.freedomfinancestack.razorpay.cas.acs.gateway.mock.DsGatewayServiceMock;
 import org.freedomfinancestack.razorpay.cas.contract.RREQ;
 import org.freedomfinancestack.razorpay.cas.contract.RRES;
 import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.mockito.junit.jupiter.MockitoExtension;
 import org.springframework.web.client.RestTemplate;

 import static org.freedomfinancestack.razorpay.cas.acs.data.RREQTestData.*;
 import static org.junit.jupiter.api.Assertions.assertEquals;
 import static org.junit.jupiter.api.Assertions.assertThrows;
 import static org.mockito.ArgumentMatchers.any;
 import static org.mockito.Mockito.when;

 @ExtendWith(MockitoExtension.class)
 class DsGatewayServiceImplTest {

    @Mock MasterCardDsHttpsGatewayService masterCardDsHttpsGatewayService;
    @Mock DsGatewayServiceMock dsGatewayServiceMock;
    @Mock RestTemplate masterCardDsRestTemplate;
    @Mock RestTemplate visaDsRestTemplate;
    @Mock
    VisaDsHttpsGatewayService visaDsHttpsGatewayService;
    @InjectMocks
    DsGatewayServiceImpl dsGatewayService;

    @Test
    void sendRReq_Happypath(){
        //todo httpsGatewayService.sendRequestWithRetry()
    }
     @Test
     void sendError_Happypath(){
         //todo httpsGatewayService.sendRequestWithRetry()
     }

    @Test
    public void sendRReq_HTTPGateWay_Exception() {
        RREQ rreq = getValidRReq();
        Network network = Network.getNetwork((byte) 3);
        assertThrows(
                IllegalArgumentException.class, () -> dsGatewayService.sendRReq(network, rreq));
    }

    @Test
    public void sendRReq_isMock() throws ACSValidationException {
        RREQ rreq = getValidRReq();
        Network network = Network.getNetwork((byte) 1);
        GatewayConfig.ServiceConfig serviceConfig1 = new GatewayConfig.ServiceConfig();
        when(visaDsHttpsGatewayService.getServiceConfig()).thenReturn(serviceConfig1);
        serviceConfig1.setMock(true);
        RRES rres = getValidRRes();
        when(dsGatewayServiceMock.sendRReq(network, rreq)).thenReturn(rres);
        assertEquals(rres, dsGatewayService.sendRReq(network, rreq));
    }

    @Test
    public void sendRReq() throws ACSValidationException {
        RREQ rreq = getValidRReq();
        Network network = Network.getNetwork((byte) 1);
        GatewayConfig.ServiceConfig serviceConfig1 = new GatewayConfig.ServiceConfig();
        serviceConfig1.setMock(false);
        when(visaDsHttpsGatewayService.getServiceConfig()).thenReturn(serviceConfig1);

        String rresString =
                "{\n"
                        + "        \"threeDSServerTransID\": \"b3c9516d-47ab-41b5-8e6e-401d3cae691a\",\n"
                        + "        \"acsTransID\": \"d8ac9cd2-e72f-4918-93d0-24a684826394\",\n"
                        + "        \"dsTransID\": \"f52253dd-3a06-4fbc-afe7-688e426dd26c\",\n"
                        + "        \"messageType\": \"RRes\",\n"
                        + "        \"messageVersion\": \"2.2.0\",\n"
                        + "        \"resultsStatus\": \"01\"\n"
                        + "      }";
        RRES expectedRRES = getValidRRes();


        when(visaDsHttpsGatewayService.sendRequestWithRetry(any(), any(), any(), any()))
                .thenReturn(rresString);
        RRES actualRRES = dsGatewayService.sendRReq(network, rreq);
        assertEquals(expectedRRES.getResultsStatus(), actualRRES.getResultsStatus());
    }

    @Test
    public void sendRReq_ACSValidationException_Exception() throws ACSValidationException {
        RREQ rreq = getValidRReq();
        Network network = Network.getNetwork((byte) 1);
        GatewayConfig.ServiceConfig serviceConfig1 = new GatewayConfig.ServiceConfig();
        serviceConfig1.setMock(false);
        when(visaDsHttpsGatewayService.getServiceConfig()).thenReturn(serviceConfig1);
        when(visaDsHttpsGatewayService.sendRequestWithRetry(any(), any(), any(), any()))
                .thenReturn("Hello");
        assertThrows(ACSValidationException.class, ()-> dsGatewayService.sendRReq(network, rreq));
    }

 }
