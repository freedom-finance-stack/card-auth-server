package org.freedomfinancestack.razorpay.cas.acs.data;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ChallengeSelectInfo;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceInterface;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.contract.enums.UIType;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public class UiParamsTestData {

    public static final String SAMPLE_DISPLAY_PAGE =
            "<!DOCTYPE html>\n"
                    + "<html lang=\"en\">\n"
                    + "<head>\n"
                    + "    <meta charset=\"UTF-8\">\n"
                    + "    <title>Test</title>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "</body>\n"
                    + "</html>";

    public static final String SAMPLE_ENCODED_DISPLAY_PAGE =
            "PCFET0NUWVBFIGh0bWw-CjxodG1sIGxhbmc9ImVuIj4KPGhlYWQ-CiAgICA8bWV0YSBjaGFyc2V0PSJVVEYtOCI-CiAgICA8dGl0bGU-VGVzdDwvdGl0bGU-CjwvaGVhZD4KPGJvZHk-CjwvYm9keT4KPC9odG1sPg";

    public static InstitutionUiConfig createInstitutionUiConfig() {
        return InstitutionUiConfig.builder()
                .whitelistingInfoText("sampleWhitelistingInfoText")
                .challengeInfoText(
                        "An OTP has been sent to mobile number ending with"
                                + " LAST_FOUR_DIGIT_MOBILE_NUMBER successfully!")
                .build();
    }

    public static InstitutionUIParams createInstitutionUiParams(
            Transaction transaction, AuthConfigDto authConfigDto) {

        InstitutionUIParams.InstitutionUIParamsBuilder institutionUIParams =
                InstitutionUIParams.builder();
        institutionUIParams
                .acsTransID(transaction.getId())
                .messageVersion(transaction.getMessageVersion())
                .threeDSServerTransID(
                        transaction.getTransactionReferenceDetail().getThreedsServerTransactionId())
                .otpLength(4)
                .timeout(180)
                .isTest(true)
                .deviceChannel(transaction.getDeviceChannel())
                .amount(
                        transaction.getMessageCategory().equals(MessageCategory.PA)
                                ? "100.00"
                                : null)
                .currency(
                        transaction.getMessageCategory().equals(MessageCategory.PA) ? "USD" : null)
                .merchantName(transaction.getTransactionMerchant().getMerchantName())
                .cardNumber(
                        Util.maskedCardNumber(
                                transaction
                                        .getTransactionCardDetail()
                                        .getCardNumber()
                                        .getDecrypted()))
                .otpAttemptLeft("2")
                .resendAttemptLeft("2")
                .psImage(null)
                .issuerImage(null)
                .challengeInfoHeader("sampleChallengeInfoHeader")
                .challengeInfoLabel("sampleChallengeInfoLabel")
                .challengeInfoText("sampleChallengeInfoText")
                .submitAuthenticationLabel("sampleSubmitAuthenticationLabel")
                .resendInformationLabel("sampleResendInformationLabel")
                .whyInfoLabel("sampleWhyInfoLabel")
                .whyInfoText("sampleWhyInfoText")
                .expandInfoLabel("sampleExpandInfoLabel")
                .expandInfoText("sampleExpandInfoText")
                .validationUrl("http://validationUrl.com/")
                .resendBlocked("false");

        if (transaction.getDeviceChannel().equals(DeviceChannel.APP.getChannel())) {
            if (authConfigDto.getChallengeAttemptConfig().isWhitelistingAllowed()) {
                institutionUIParams.whitelistingInfoText("sampleWhitelistingInfoText");
            }
            if (transaction
                    .getTransactionSdkDetail()
                    .getAcsInterface()
                    .equals(DeviceInterface.HTML.getValue())) {
                institutionUIParams.displayPage(SAMPLE_ENCODED_DISPLAY_PAGE);
            }
            if (transaction
                            .getTransactionSdkDetail()
                            .getAcsUiTemplate()
                            .equals(UIType.SINGLE_SELECT.getType())
                    || transaction
                            .getTransactionSdkDetail()
                            .getAcsUiTemplate()
                            .equals(UIType.MULTI_SELECT.getType())) {
                institutionUIParams.challengeSelectInfo(
                        new ChallengeSelectInfo[] {
                            ChallengeSelectInfo.builder().yes("yes").build(),
                            ChallengeSelectInfo.builder().yes("no").build()
                        });
            } else if (transaction
                    .getTransactionSdkDetail()
                    .getAcsUiTemplate()
                    .equals(UIType.OOB.getType())) {
                institutionUIParams.oobContinueLabel(InternalConstants.OOB_CONTINUE_LABEL);
            }
        }
        return institutionUIParams.build();
    }
}
