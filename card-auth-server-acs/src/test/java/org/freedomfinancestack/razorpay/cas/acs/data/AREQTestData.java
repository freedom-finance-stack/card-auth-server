package org.freedomfinancestack.razorpay.cas.acs.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.enums.ACSRenderingType;
import org.freedomfinancestack.razorpay.cas.dao.enums.ECI;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;

import static org.freedomfinancestack.razorpay.cas.acs.data.TestUtil.replaceData;

public class AREQTestData {
    public static final String SAMPLE_THREE_DS_SERVER_TRANS_ID = "threeDSRequestorID_UTSB";
    public static final String SAMPLE_ACS_REFERENCE_NUMBER = "sampleAcsReferenceNumber";
    public static final String SAMPLE_ACS_TRANS_ID = "279301f0-b090-4dfe-b7dc-c7861ea5c1cc";
    public static final String SAMPLE_DS_REFERENCE_NUMBER = "2bbfe3a7-d8af-4675-8e77-4bbab80d197a";
    public static final String SAMPLE_DS_TRANS_ID = "2c0f40ad-ca51-47ab-8da9-348b162673aa";
    public static final String SAMPLE_SDK_TRANS_ID = "2cd28420-fa5c-4d11-8073-735c79b3da93";

    public static AREQ createSampleAREQ() {
        AREQ areq = new AREQ();
        areq.setThreeDSRequestorURL("https://example.com/3ds-requestor-url");
        areq.setThreeDSCompInd("Y");
        areq.setThreeDSRequestorAuthenticationInd("02");
        areq.setThreeDSRequestorID(SAMPLE_THREE_DS_SERVER_TRANS_ID);
        areq.setThreeDSRequestorName("Example Requestor");
        areq.setThreeDSServerRefNumber("serverRefNumber");
        areq.setThreeDSServerTransID(SAMPLE_THREE_DS_SERVER_TRANS_ID);
        areq.setThreeDSServerURL("https://example.com/3ds-server-url");
        areq.setThreeRIInd("02");
        areq.setAcquirerBIN("123456");
        areq.setAcquirerMerchantID("9876543210001");
        areq.setBrowserAcceptHeader("text/html");
        areq.setBrowserJavaEnabled("true");
        areq.setBrowserLanguage("en-US");
        areq.setBrowserColorDepth("24");
        areq.setBrowserScreenHeight("1080");
        areq.setBrowserScreenWidth("1920");
        areq.setBrowserTZ("0");
        areq.setBrowserUserAgent(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
                        + " Chrome/58.0.3029.110 Safari/537.3");
        areq.setAcctNumber("4111111111111111");
        areq.setDeviceChannel("02");

        areq.setThreeDSRequestorChallengeInd("01");
        areq.setThreeDSRequestorAuthenticationInfo(
                createSampleThreeDSRequestorAuthenticationInfo());
        areq.setThreeDSRequestorPriorAuthenticationInfo(
                createSampleThreeDSRequestorPriorAuthenticationInfo());
        areq.setAddrMatch("Y");
        areq.setAcctInfo(createSampleCardholderAccountInformation());
        areq.setAcctID("exampleAcctID");
        areq.setMerchantRiskIndicator(createSampleThreeDSMerchantFields());
        areq.setMerchantCountryCode("840");
        areq.setNotificationURL("https://simulator/");
        // Set values for conditional fields
        areq.setThreeDSServerOperatorID("sampleOperatorID");
        areq.setThreeDSServerOperatorID("exampleServerOperatorID");
        areq.setAcctType("02");
        areq.setBroadInfo(createSampleBrodInfo());
        areq.setBrowserIP("192.168.0.1");
        areq.setCardExpiryDate("2212");
        areq.setBillAddrCity("ExampleCity");
        areq.setBillAddrCountry("840");
        areq.setBillAddrLine1("ExampleLine1");
        areq.setBillAddrLine2("ExampleLine2");
        areq.setBillAddrLine3("ExampleLine3");
        areq.setBillAddrPostCode("12345");
        areq.setBillAddrState("AZ");
        areq.setEmail("example@example.com");
        areq.setHomePhone(createSamplePhone("1234567890"));
        areq.setMobilePhone(createSamplePhone("9876543210"));
        areq.setCardholderName("John Doe");
        areq.setShipAddrCity("ShippingCity");
        areq.setShipAddrCountry("840");
        areq.setShipAddrLine1("ShippingLine1");
        areq.setShipAddrLine2("ShippingLine2");
        areq.setShipAddrLine3("ShippingLine3");
        areq.setShipAddrPostCode("54321");
        areq.setShipAddrState("AZ");
        areq.setWorkPhone(createSamplePhone("5551234567"));
        areq.setDeviceInfo("exampleDeviceInfo");
        areq.setDsReferenceNumber("exampleDsReferenceNumber");
        areq.setDsTransID(SAMPLE_DS_TRANS_ID);
        areq.setDsURL("http://example.com/3ds");
        areq.setPayTokenInd("true");
        areq.setMcc("7922");
        areq.setPurchaseDate("20231121113432");
        areq.setPurchaseExponent("2");
        areq.setPurchaseAmount("5628");
        areq.setPurchaseCurrency("840");
        areq.setMessageExtension(List.of(createSampleMessageExtension()));
        areq.setMessageVersion("2.1.0");
        areq.setMessageCategory("01");
        areq.setMessageType("AReq");
        areq.setRecurringExpiry("202512");
        areq.setRecurringFrequency("02");
        areq.setSdkEncData("exampleSdkEncData");
        areq.setTransType("01");
        areq.setMerchantName("test");

        return areq;
    }

    public static Phone createSamplePhone(String phoneStr) {
        Phone phone = new Phone();
        phone.setCc("+91");
        phone.setSubscriber(phoneStr);
        return phone;
    }

    public static ThreeDSMerchantFeilds createSampleThreeDSMerchantFeilds() {
        ThreeDSMerchantFeilds merchantFields = new ThreeDSMerchantFeilds();
        merchantFields.setShipIndicator("Y");
        merchantFields.setDeliveryTimeframe("03");
        merchantFields.setDeliveryEmailAddress("merchant@example.com");
        merchantFields.setReorderItemsInd("Y");
        merchantFields.setPreOrderPurchaseInd("N");
        merchantFields.setPreOrderDate("20221231");
        merchantFields.setGiftCardAmount("50.00");
        merchantFields.setGiftCardCurr("USD");
        merchantFields.setGiftCardCount("1");
        return merchantFields;
    }

    public static CardholderAccountInformation createSampleCardholderAccountInformation() {
        CardholderAccountInformation accountInformation = new CardholderAccountInformation();
        accountInformation.setChAccAgeInd("03");
        accountInformation.setChAccDate("20220101");
        accountInformation.setChAccChangeInd("04");
        accountInformation.setChAccChange("20201201");
        accountInformation.setChAccPwChangeInd("01");
        accountInformation.setChAccPwChange("20201115");
        accountInformation.setShipAddressUsageInd("01");
        accountInformation.setShipAddressUsage("20211231");
        accountInformation.setTxnActivityDay("01");
        accountInformation.setTxnActivityYear("2022");
        accountInformation.setProvisionAttemptsDay("02");
        accountInformation.setNbPurchaseAccount("5");
        accountInformation.setSuspiciousAccActivity("01");
        accountInformation.setShipNameIndicator("01");
        accountInformation.setPaymentAccInd("02");
        accountInformation.setPaymentAccAge("20200801");
        return accountInformation;
    }

    public static ThreeDSRequestorPriorAuthenticationInfo
            createSampleThreeDSRequestorPriorAuthenticationInfo() {
        ThreeDSRequestorPriorAuthenticationInfo priorAuthenticationInfo =
                new ThreeDSRequestorPriorAuthenticationInfo();
        priorAuthenticationInfo.setThreeDSReqPriorAuthData("samplePriorAuthData");
        priorAuthenticationInfo.setThreeDSReqPriorAuthMethod("02");
        priorAuthenticationInfo.setThreeDSReqPriorAuthTimestamp("20221231235959");
        priorAuthenticationInfo.setThreeDSReqPriorRef("samplePriorRef");
        return priorAuthenticationInfo;
    }

    public static ThreeDSRequestorAuthenticationInfo
            createSampleThreeDSRequestorAuthenticationInfo() {
        ThreeDSRequestorAuthenticationInfo info = new ThreeDSRequestorAuthenticationInfo();
        info.setThreeDSReqAuthMethod("01"); // Sample value, adjust as needed
        info.setThreeDSReqAuthTimestamp(
                "2023-01-01T12:00:00Z"); // Sample timestamp, adjust as needed
        info.setThreeDSReqAuthData("sampleAuthData"); // Sample value, adjust as needed
        return info;
    }

    public static ARES createSampleARES() {
        ARES ares = new ARES();
        ares.setThreeDSServerTransID(SAMPLE_THREE_DS_SERVER_TRANS_ID);
        ares.setAcsReferenceNumber(SAMPLE_ACS_REFERENCE_NUMBER);
        ares.setAcsTransID(SAMPLE_ACS_TRANS_ID);
        ares.setDsReferenceNumber(SAMPLE_DS_REFERENCE_NUMBER);
        ares.setDsTransID(SAMPLE_DS_TRANS_ID);
        ares.setSdkTransID(SAMPLE_SDK_TRANS_ID);

        ares.setTransStatus(
                TransactionStatus.SUCCESS.getStatus()); // Set an example value, adjust as needed

        // Set optional fields
        ares.setCardholderInfo("sampleCardholderInfo");

        // Set conditional fields
        ares.setAcsChallengeMandated("Y");
        ares.setAcsOperatorID("sampleOperatorID");
        ares.setAcsRenderingType(createMockACSRenderingType());
        ares.setAcsSignedContent("sampleSignedContent");
        ares.setAcsURL("http://sample-acs-url.com");
        ares.setAuthenticationType("02");
        ares.setAuthenticationValue("1234");
        ares.setBroadInfo("sampleBroadInfo");
        ares.setEci(ECI.VISA_SUCCESS.getValue());
        ares.setMessageExtension(Collections.singletonList(createSampleMessageExtension()));
        ares.setTransStatusReason("sampleTransStatusReason");

        // Set additional fields for 3DS Version 2.2.0
        ares.setAcsDecConInd("sampleAcsDecConInd");
        ares.setWhiteListStatus("sampleWhiteListStatus");
        ares.setWhiteListStatusSource("sampleWhiteListStatusSource");

        return ares;
    }

    public static MessageExtension createSampleMessageExtension() {
        MessageExtension messageExtension = new MessageExtension();
        messageExtension.setName("SampleName");
        messageExtension.setId("SampleID");
        messageExtension.setCriticalityIndicator(false);

        // Sample data map
        HashMap<String, Object> data = new HashMap<>();
        data.put("key1", "value1");
        data.put("key2", 123);
        data.put("key3", true);
        messageExtension.setData(data);

        return messageExtension;
    }

    public static ThreeDSMerchantFeilds createSampleThreeDSMerchantFields() {
        ThreeDSMerchantFeilds merchantFields = new ThreeDSMerchantFeilds();
        merchantFields.setShipIndicator("01");
        merchantFields.setDeliveryTimeframe("02");
        merchantFields.setDeliveryEmailAddress("sample@example.com");
        merchantFields.setReorderItemsInd("01");
        merchantFields.setPreOrderPurchaseInd("02");
        merchantFields.setPreOrderDate("20300101");
        merchantFields.setGiftCardAmount("100");
        merchantFields.setGiftCardCurr("840");
        merchantFields.setGiftCardCount("01");
        return merchantFields;
    }

    public static BrodInfo createSampleBrodInfo() {
        BrodInfo brodInfo = new BrodInfo();
        brodInfo.setData("Sample data");
        return brodInfo;
    }

    public static ACSRenderingType createMockACSRenderingType() {
        return new ACSRenderingType("sampleAcsInterface", "sampleAcsUiTemplate");
    }

    public static AREQ getAReq(Map<String, Object> areqTestData) {
        return replaceData(createSampleAREQ(), areqTestData);
    }
}
