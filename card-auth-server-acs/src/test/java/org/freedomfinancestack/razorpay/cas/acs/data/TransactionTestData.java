package org.freedomfinancestack.razorpay.cas.acs.data;

import java.sql.Timestamp;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.*;

public class TransactionTestData {
    public static final String SAMPLE_THREE_DS_SERVER_TRANS_ID =
            "2ddc9891-d085-411d-b068-933e30de8231";
    public static final String SAMPLE_ACS_REFERENCE_NUMBER = "sampleAcsReferenceNumber";
    public static final String SAMPLE_ACS_TRANS_ID = "279301f0-b090-4dfe-b7dc-c7861ea5c1cc";
    public static final String SAMPLE_DS_REFERENCE_NUMBER = "2bbfe3a7-d8af-4675-8e77-4bbab80d197a";
    public static final String SAMPLE_DS_TRANS_ID = "2c0f40ad-ca51-47ab-8da9-348b162673aa";
    public static final String SAMPLE_SDK_TRANS_ID = "2cd28420-fa5c-4d11-8073-735c79b3da93";

    public static Transaction createSampleBrwTransaction() {
        // Create a unique ID for the transaction
        // Create the main Transaction
        return Transaction.builder()
                .id(SAMPLE_ACS_TRANS_ID)
                .institutionId("I1")
                .cardRangeId("R1")
                .messageVersion("2.2.0")
                .messageCategory(MessageCategory.PA)
                .deviceChannel(DeviceChannel.BRW.getChannel())
                .phase(Phase.AREQ)
                .transactionStatus(TransactionStatus.CREATED)
                .transactionReferenceDetail(createSampleTransactionReferenceDetail())
                .transactionPurchaseDetail(createSampleTransactionPurchaseDetail())
                .transactionCardDetail(createSampleTransactionCardDetail())
                .transactionBrowserDetail(createSampleTransactionBrowserDetail())
                .transactionMerchant(createSampleTransactionMerchant())
                .transactionCardHolderDetail(createSampleTransactionCardHolderDetail())
                .build();
    }

    public static Transaction createSampleAppTransaction() {

        // Create the main Transaction
        return Transaction.builder()
                .id(SAMPLE_ACS_TRANS_ID)
                .institutionId("I1")
                .cardRangeId("R1")
                .messageVersion("2.2.0")
                .messageCategory(MessageCategory.PA)
                .deviceChannel(DeviceChannel.APP.getChannel())
                .deviceName(InternalConstants.ANDROID)
                .phase(Phase.AREQ)
                .transactionStatus(TransactionStatus.CREATED)
                .transactionReferenceDetail(createSampleTransactionReferenceDetail())
                .transactionPurchaseDetail(createSampleTransactionPurchaseDetail())
                .transactionSdkDetail(createSampleTransactionSdkDetail())
                .transactionCardDetail(createSampleTransactionCardDetail())
                .transactionMerchant(createSampleTransactionMerchant())
                .transactionCardHolderDetail(createSampleTransactionCardHolderDetail())
                .build();
    }

    public static TransactionBrowserDetail createSampleTransactionBrowserDetail() {
        return TransactionBrowserDetail.builder()
                .javascriptEnabled(true)
                .ip("127.0.0.1")
                .acceptHeader("text/html")
                .build();
    }

    public static TransactionMerchant createSampleTransactionMerchant() {
        return TransactionMerchant.builder()
                .acquirerMerchantId("acquirerMerchantId")
                .merchantName("Sample Merchant")
                .merchantCountryCode((short) 840) // Country code for the United States
                .build();
    }

    public static TransactionCardDetail createSampleTransactionCardDetail() {
        return TransactionCardDetail.builder()
                .cardNumber("4111111111111111")
                .cardholderName("John Doe")
                .cardExpiry("12/25")
                .networkCode(Network.VISA.getValue()) // Assuming 1 represents VISA network
                .build();
    }

    public static TransactionReferenceDetail createSampleTransactionReferenceDetail() {

        return TransactionReferenceDetail.builder()
                .threedsServerTransactionId(SAMPLE_THREE_DS_SERVER_TRANS_ID)
                .threedsServerReferenceNumber("serverReferenceNumber")
                .dsTransactionId(SAMPLE_DS_TRANS_ID)
                .dsUrl("http://dsUrl.com")
                .notificationUrl("http://notificationUrl.com")
                .threeDSRequestorChallengeInd("challengeInd")
                .build();
    }

    public static TransactionPurchaseDetail createSampleTransactionPurchaseDetail() {
        return TransactionPurchaseDetail.builder()
                .purchaseAmount("100.00")
                .purchaseCurrency("USD")
                .purchaseExponent((byte) 2)
                .purchaseTimestamp(new Timestamp(System.currentTimeMillis()))
                .payTokenInd(true)
                .build();
    }

    public static TransactionSdkDetail createSampleTransactionSdkDetail() {
        return TransactionSdkDetail.builder()
                .sdkAppID("sampleAppID")
                .sdkTransId(SAMPLE_SDK_TRANS_ID)
                .sdkReferenceNumber("sampleRefNumber")
                .acsInterface("sampleAcsInterface")
                .acsUiTemplate("sampleAcsUiTemplate")
                .deviceInfo("sampleDeviceInfo")
                .acsSecretKey("sampleSecretKey")
                .encryptionMethod("sampleEncryptionMethod")
                .acsCounterAtoS("sampleCounterAtoS")
                .whitelistingDataEntry("sampleDataEntry")
                .build();
    }

    public static TransactionCardHolderDetail createSampleTransactionCardHolderDetail() {
        return TransactionCardHolderDetail.builder()
                .name("John Doe")
                .emailId("john.doe@example.com")
                .mobileNumber("1234567890")
                .build();
    }
}
