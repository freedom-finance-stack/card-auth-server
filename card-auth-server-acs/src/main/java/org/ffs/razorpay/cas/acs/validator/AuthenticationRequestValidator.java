package org.ffs.razorpay.cas.acs.validator;

import java.util.Arrays;

import org.ffs.razorpay.cas.acs.constant.InternalConstants;
import org.ffs.razorpay.cas.acs.exception.ValidationException;
import org.ffs.razorpay.cas.acs.utils.Util;
import org.ffs.razorpay.cas.acs.validator.enums.DataLengthType;
import org.ffs.razorpay.cas.acs.validator.enums.ThreeDSDataElement;
import org.ffs.razorpay.cas.acs.validator.rules.*;
import org.ffs.razorpay.cas.acs.validator.rules.IsIPRule;
import org.ffs.razorpay.cas.acs.validator.rules.IsInRule;
import org.ffs.razorpay.cas.acs.validator.rules.IsNumericRule;
import org.ffs.razorpay.cas.acs.validator.rules.IsUUIDRule;
import org.ffs.razorpay.cas.acs.validator.rules.IsValidDate;
import org.ffs.razorpay.cas.acs.validator.rules.IsValidRule;
import org.ffs.razorpay.cas.acs.validator.rules.LengthRule;
import org.ffs.razorpay.cas.acs.validator.rules.NotInRule;
import org.ffs.razorpay.cas.acs.validator.rules.NotNullRule;
import org.ffs.razorpay.cas.acs.validator.rules.RegexRule;
import org.ffs.razorpay.cas.acs.validator.rules.ValidListRule;
import org.ffs.razorpay.cas.acs.validator.rules.Validation;
import org.ffs.razorpay.cas.contract.AREQ;
import org.ffs.razorpay.cas.contract.constants.EMVCOConstant;
import org.ffs.razorpay.cas.contract.enums.MessageCategory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import static org.ffs.razorpay.cas.acs.validator.rules.WhenRule.when;

@Slf4j
@Component(value = "authenticationRequestValidator")
public class AuthenticationRequestValidator implements ThreeDSValidator<AREQ> {

    @Override
    public void validateRequest(AREQ request) throws ValidationException {
        validateAuthenticationRequest(request);
    }

    private void validateAuthenticationRequest(AREQ request) throws ValidationException {
        validateMandatoryFields(request);
        validateConditionalFields(request);
        validateOptionalFields(request);
    }

    // todo improve readability of validations
    private void validateMandatoryFields(AREQ request) throws ValidationException {

        Validation.validate(
                ThreeDSDataElement.DEVICE_CHANNEL.getFieldName(),
                request.getDeviceChannel(),
                new NotNullRule<>(),
                new IsInRule(ThreeDSDataElement.DEVICE_CHANNEL.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_CATEGORY.getFieldName(),
                request.getMessageCategory(),
                new NotNullRule<>(),
                new IsInRule(ThreeDSDataElement.MESSAGE_CATEGORY.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_TYPE.getFieldName(),
                request.getMessageType(),
                new NotNullRule<>(),
                new IsInRule(ThreeDSDataElement.MESSAGE_TYPE.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_VERSION.getFieldName(),
                request.getMessageVersion(),
                new NotNullRule<>(),
                new LengthRule(DataLengthType.VARIABLE, 8),
                new IsInRule(
                        ThreeDSDataElement.MESSAGE_VERSION
                                .getAcceptedValues())); // todo handle exception for message version

        Validation.validate(
                ThreeDSDataElement.THREEDS_COMPIND.getFieldName(),
                request.getThreeDSCompInd(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_COMPIND, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.FIXED, 1),
                new IsInRule(ThreeDSDataElement.THREEDS_COMPIND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_URL.getFieldName(),
                request.getThreeDSRequestorURL(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_REQUESTOR_URL, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 2048));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_ID.getFieldName(),
                request.getThreeDSRequestorID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_REQUESTOR_ID, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 35));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND.getFieldName(),
                request.getThreeDSRequestorAuthenticationInd(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND, request),
                        new NotNullRule<>()),
                new IsInRule(
                        ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND
                                .getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_NAME.getFieldName(),
                request.getThreeDSRequestorName(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_REQUESTOR_NAME, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 40));
        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_REF_NUMBER.getFieldName(),
                request.getThreeDSServerRefNumber(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_SERVER_REF_NUMBER, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 32));
        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID.getFieldName(),
                request.getThreeDSServerTransID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 36));
        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_URL.getFieldName(),
                request.getThreeDSServerURL(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_SERVER_URL, request),
                        new NotNullRule<>()),
                new LengthRule(
                        DataLengthType.VARIABLE,
                        2048)); // todo is message version condition needed ?
        Validation.validate(
                ThreeDSDataElement.THREEDS_RI_IND.getFieldName(),
                request.getThreeRIInd(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_RI_IND, request),
                        new NotNullRule<>()),
                new IsInRule(ThreeDSDataElement.THREEDS_RI_IND.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.ACQUIRER_BIN.getFieldName(),
                request.getAcquirerBIN(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.ACQUIRER_BIN, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 11));
        Validation.validate(
                ThreeDSDataElement.ACQUIRER_MERCHANT_ID.getFieldName(),
                request.getAcquirerMerchantID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.ACQUIRER_MERCHANT_ID, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 15));

        Validation.validate(
                ThreeDSDataElement.BROWSER_JAVA_ENABLED.getFieldName(),
                request.getBrowserJavaEnabled(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_JAVA_ENABLED, request),
                        new NotNullRule<>()),
                new IsInRule(ThreeDSDataElement.BROWSER_JAVA_ENABLED.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.BROWSER_ACCEPT_HEADER.getFieldName(),
                request.getBrowserAcceptHeader(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_ACCEPT_HEADER, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 2048));
        Validation.validate(
                ThreeDSDataElement.BROWSER_JAVA_SCRIPT_ENABLED.getFieldName(),
                request.getBrowserJavascriptEnabled(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_JAVA_SCRIPT_ENABLED, request),
                        new NotNullRule<>()),
                new IsInRule(ThreeDSDataElement.BROWSER_JAVA_SCRIPT_ENABLED.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.BROWSER_LANGUAGE.getFieldName(),
                request.getBrowserLanguage(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_LANGUAGE, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 8));
        Validation.validate(
                ThreeDSDataElement.BROWSER_COLOR_DEPTH.getFieldName(),
                request.getBrowserColorDepth(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_COLOR_DEPTH, request),
                        new NotNullRule<>()),
                new IsInRule(ThreeDSDataElement.BROWSER_COLOR_DEPTH.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.BROWSER_SCREEN_HEIGHT.getFieldName(),
                request.getBrowserScreenHeight(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_SCREEN_HEIGHT, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 6),
                new IsNumericRule());
        Validation.validate(
                ThreeDSDataElement.BROWSER_SCREEN_WIDTH.getFieldName(),
                request.getBrowserScreenWidth(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_SCREEN_WIDTH, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 6),
                new IsNumericRule());
        Validation.validate(
                ThreeDSDataElement.BROWSER_TZ.getFieldName(),
                request.getBrowserTZ(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_TZ, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 5),
                new RegexRule("[0-9-.]{1,5}"));
        Validation.validate(
                ThreeDSDataElement.BROWSER_USER_AGENT.getFieldName(),
                request.getBrowserUserAgent(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_USER_AGENT, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 2048));
        Validation.validate(
                ThreeDSDataElement.ACCT_NUMBER.getFieldName(),
                request.getAcctNumber(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.ACCT_NUMBER, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 19));
        Validation.validate(
                ThreeDSDataElement.DEVICE_RENDER_OPTIONS.getFieldName(),
                request.getDeviceRenderOptions(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.DEVICE_RENDER_OPTIONS, request),
                        new NotNullRule<>()),
                new IsValidRule<>());
        Validation.validate(
                ThreeDSDataElement.NOTIFICATION_URL.getFieldName(),
                request.getNotificationURL(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.NOTIFICATION_URL, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 2048));

        Validation.validate(
                ThreeDSDataElement.SDK_APP_ID.getFieldName(),
                request.getSdkAppID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_APP_ID, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 36));
        Validation.validate(
                ThreeDSDataElement.SDK_MAX_TIMEOUT.getFieldName(),
                request.getSdkMaxTimeout(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_MAX_TIMEOUT, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.FIXED, 2));
        Validation.validate(
                ThreeDSDataElement.SDK_REFERENCE_NUMBER.getFieldName(),
                request.getSdkReferenceNumber(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_REFERENCE_NUMBER, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 32));
        Validation.validate(
                ThreeDSDataElement.SDK_EPHEM_PUB_KEY.getFieldName(),
                request.getSdkEphemPubKey(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_EPHEM_PUB_KEY, request),
                        new NotNullRule<>()),
                new IsValidRule<>());
        Validation.validate(
                ThreeDSDataElement.SDK_TRANS_ID.getFieldName(),
                request.getSdkTransID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_TRANS_ID, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 36),
                new IsUUIDRule());
        Validation.validate(
                ThreeDSDataElement.DEVICE_INFO.getFieldName(),
                request.getDeviceInfo(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.DEVICE_INFO, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 64000));

        Validation.validate(
                ThreeDSDataElement.DS_REFERENCE_NUMBER.getFieldName(),
                request.getDsReferenceNumber(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.DS_REFERENCE_NUMBER, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 32));
        Validation.validate(
                ThreeDSDataElement.DS_TRANS_ID.getFieldName(),
                request.getDsTransID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.DS_TRANS_ID, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.FIXED, 36));
        Validation.validate(
                ThreeDSDataElement.DS_URL.getFieldName(),
                request.getDsURL(),
                when(
                        validateDeviceChannelAndMessageCategory(ThreeDSDataElement.DS_URL, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 2048));
    }

    private void validateConditionalFields(AREQ request) throws ValidationException {

        // Conditional Fields

        Validation.validate(
                ThreeDSDataElement.MCC.getFieldName(),
                request.getMcc(),
                when(
                        validateDeviceChannelAndMessageCategory(ThreeDSDataElement.MCC, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.FIXED, 4),
                new IsNumericRule());
        Validation.validate(
                ThreeDSDataElement.MERCHANT_COUNTRY_CODE.getFieldName(),
                request.getMerchantCountryCode(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.MERCHANT_COUNTRY_CODE, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.FIXED, 3),
                new IsNumericRule(),
                new NotInRule(EMVCOConstant.excludedCountry));
        Validation.validate(
                ThreeDSDataElement.MERCHANT_NAME.getFieldName(),
                request.getMerchantName(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.MERCHANT_NAME, request),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 40));
        Validation.validate(
                ThreeDSDataElement.PAY_TOKEN_SOURCE.getFieldName(),
                request.getPayTokenSource(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                        ThreeDSDataElement.PAY_TOKEN_SOURCE, request)
                                && !Util.isNullorBlank(request.getPayTokenInd()),
                        new NotNullRule<>()),
                new IsInRule(ThreeDSDataElement.PAY_TOKEN_SOURCE.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQ_AUTH_METHOD_IND.getFieldName(),
                request.getThreeDSReqAuthMethodInd(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                        ThreeDSDataElement.THREEDS_REQ_AUTH_METHOD_IND, request)
                                && !Util.isNullorBlank(request.getThreeDSReqAuthMethodInd()),
                        new NotNullRule<>()),
                new IsInRule(ThreeDSDataElement.THREEDS_REQ_AUTH_METHOD_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.WHITE_LIST_STATUS_SOURCE.getFieldName(),
                request.getWhiteListStatusSource(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                        ThreeDSDataElement.WHITE_LIST_STATUS_SOURCE, request)
                                && !Util.isNullorBlank(request.getWhiteListStatus()),
                        new NotNullRule<>()),
                new IsInRule(ThreeDSDataElement.WHITE_LIST_STATUS_SOURCE.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_DEC_MAX_TIME.getFieldName(),
                request.getThreeDSRequestorDecMaxTime(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                        ThreeDSDataElement.THREEDS_REQUESTOR_DEC_MAX_TIME, request)
                                && !Util.isNullorBlank(request.getThreeDSRequestorDecReqInd())
                                && InternalConstants.YES.equals(
                                        request.getThreeDSRequestorDecReqInd()),
                        new NotNullRule<>()));

        Validation.validate(
                ThreeDSDataElement.MESSAGE_EXTENSION.getFieldName(),
                request.getMessageExtension(),
                new ValidListRule<>(new IsValidRule<>()));

        boolean purchaseNPARule =
                (!Util.isNullorBlank(request.getThreeDSRequestorAuthenticationInd())
                                && Arrays.asList("02", "03")
                                        .contains(request.getThreeDSRequestorAuthenticationInd()))
                        || (!Util.isNullorBlank(request.getThreeRIInd())
                                && Arrays.asList("01", "02", "06", "07", "08", "09", "11")
                                        .contains(request.getThreeRIInd())
                                && "2.2.0".equals(request.getMessageVersion()));
        boolean purchaseElementsWhenRule =
                validateDeviceChannel(ThreeDSDataElement.PURCHASE_AMOUNT, request)
                        && (request.getMessageCategory().equals(MessageCategory.PA.getCategory())
                                || (request.getMessageCategory()
                                                .equals(MessageCategory.NPA.getCategory())
                                        && purchaseNPARule));
        Validation.validate(
                ThreeDSDataElement.PURCHASE_AMOUNT.getFieldName(),
                request.getPurchaseAmount(),
                when(purchaseElementsWhenRule, new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 48),
                new IsNumericRule());

        Validation.validate(
                ThreeDSDataElement.PURCHASE_CURRENCY.getFieldName(),
                request.getPurchaseCurrency(),
                when(purchaseElementsWhenRule, new NotNullRule<>()),
                new LengthRule(DataLengthType.FIXED, 3),
                new NotInRule(EMVCOConstant.excludedCountry));
        Validation.validate(
                ThreeDSDataElement.PURCHASE_EXPONENT.getFieldName(),
                request.getPurchaseExponent(),
                when(purchaseElementsWhenRule, new NotNullRule<>()),
                new IsInRule(ThreeDSDataElement.PURCHASE_EXPONENT.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.PURCHASE_DATE.getFieldName(),
                request.getPurchaseDate(),
                when(purchaseElementsWhenRule, new NotNullRule<>()),
                new LengthRule(DataLengthType.FIXED, 14),
                new IsValidDate(ThreeDSDataElement.PURCHASE_DATE.getAcceptedFormat()));
        Validation.validate(
                ThreeDSDataElement.PURCHASE_INSTAL_DATA.getFieldName(),
                request.getPurchaseInstalData(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                        ThreeDSDataElement.PURCHASE_INSTAL_DATA, request)
                                && Util.isNullorBlank(
                                        request.getThreeDSRequestorAuthenticationInd())
                                && "03".equals(request.getThreeDSRequestorAuthenticationInd()),
                        new NotNullRule<>()),
                new LengthRule(DataLengthType.VARIABLE, 3));
    }

    protected void validateOptionalFields(AREQ request) throws ValidationException {

        // Optional Fields
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_INFO.getFieldName(),
                request.getThreeDSRequestorAuthenticationInfo(),
                new IsValidRule<>());
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_CHALLENGE_IND.getFieldName(),
                request.getThreeDSRequestorChallengeInd(),
                new IsInRule(
                        ThreeDSDataElement.THREEDS_REQUESTOR_CHALLENGE_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_PRIOR_AUTHENTICATION_INFO.getFieldName(),
                request.getThreeDSRequestorPriorAuthenticationInfo(),
                new IsValidRule<>());
        Validation.validate(
                ThreeDSDataElement.ADDRESS_MATCH.getFieldName(),
                request.getAddrMatch(),
                new IsInRule(ThreeDSDataElement.ADDRESS_MATCH.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.ACCT_ID.getFieldName(),
                request.getAcctID(),
                new LengthRule(DataLengthType.VARIABLE, 64));
        Validation.validate(
                ThreeDSDataElement.ACCT_TYPE.getFieldName(),
                request.getAcctType(),
                new IsInRule(ThreeDSDataElement.ACCT_TYPE.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MERCHANT_RISK_INDICATOR.getFieldName(),
                request.getMerchantRiskIndicator(),
                new IsValidRule<>());
        Validation.validate(
                ThreeDSDataElement.WHITE_LIST_STATUS.getFieldName(),
                request.getWhiteListStatus(),
                new IsInRule(ThreeDSDataElement.WHITE_LIST_STATUS.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_DEC_REQ_IND.getFieldName(),
                request.getThreeDSRequestorDecReqInd(),
                new IsInRule(ThreeDSDataElement.THREEDS_REQUESTOR_DEC_REQ_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.TRANS_TYPE.getFieldName(),
                request.getTransType(),
                new LengthRule(DataLengthType.FIXED, 2),
                new IsNumericRule());
        Validation.validate(
                ThreeDSDataElement.PAY_TOKEN_IND.getFieldName(),
                request.getPayTokenInd(),
                new IsInRule(ThreeDSDataElement.PAY_TOKEN_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.CARD_EXPIRY_DATE.getFieldName(),
                request.getCardExpiryDate(),
                new LengthRule(DataLengthType.FIXED, 4),
                new IsValidDate("YYMM"));

        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_CITY.getFieldName(),
                request.getBillAddrCity(),
                new LengthRule(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_COUNTRY.getFieldName(),
                request.getBillAddrCountry(),
                new LengthRule(DataLengthType.FIXED, 3),
                new IsNumericRule(),
                new NotInRule(EMVCOConstant.excludedCountry));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_LINE_1.getFieldName(),
                request.getBillAddrLine1(),
                new LengthRule(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_LINE_2.getFieldName(),
                request.getBillAddrLine2(),
                new LengthRule(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_LINE_3.getFieldName(),
                request.getBillAddrLine3(),
                new LengthRule(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_POST_CODE.getFieldName(),
                request.getBillAddrPostCode(),
                new LengthRule(DataLengthType.VARIABLE, 16));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_STATE.getFieldName(),
                request.getBillAddrState(),
                new LengthRule(DataLengthType.VARIABLE, 3));
        Validation.validate(
                ThreeDSDataElement.EMAIL.getFieldName(),
                request.getEmail(),
                new LengthRule(DataLengthType.VARIABLE, 254));
        Validation.validate(
                ThreeDSDataElement.HOME_PHONE.getFieldName(),
                request.getHomePhone(),
                new IsValidRule<>());
        Validation.validate(
                ThreeDSDataElement.MOBILE_PHONE.getFieldName(),
                request.getMobilePhone(),
                new IsValidRule<>());
        Validation.validate(
                ThreeDSDataElement.CARDHOLDER_NAME.getFieldName(),
                request.getCardholderName(),
                new LengthRule(DataLengthType.VARIABLE, 45));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_CITY.getFieldName(),
                request.getShipAddrCity(),
                new LengthRule(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_COUNTRY.getFieldName(),
                request.getShipAddrCountry(),
                new LengthRule(DataLengthType.FIXED, 3),
                new IsNumericRule(),
                new NotInRule(EMVCOConstant.excludedCountry));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_LINE_1.getFieldName(),
                request.getShipAddrLine1(),
                new LengthRule(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_LINE_2.getFieldName(),
                request.getShipAddrLine2(),
                new LengthRule(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_LINE_3.getFieldName(),
                request.getShipAddrLine3(),
                new LengthRule(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_POST_CODE.getFieldName(),
                request.getShipAddrPostCode(),
                new LengthRule(DataLengthType.VARIABLE, 16));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_STATE.getFieldName(),
                request.getShipAddrState(),
                new LengthRule(DataLengthType.VARIABLE, 3));
        Validation.validate(
                ThreeDSDataElement.WORK_PHONE.getFieldName(),
                request.getWorkPhone(),
                new IsValidRule<>());
        Validation.validate(
                ThreeDSDataElement.BROWSER_IP.getFieldName(),
                request.getBrowserIP(),
                new LengthRule(DataLengthType.VARIABLE, 45),
                new IsIPRule());
        Validation.validate(
                ThreeDSDataElement.ACCT_INFO.getFieldName(),
                request.getAcctInfo(),
                new IsValidRule<>());
    }

    public static boolean validateDeviceChannelAndMessageCategory(
            ThreeDSDataElement element, AREQ areq) {
        return validateDeviceChannel(element, areq) && validateMessageCategory(element, areq);
    }

    public static boolean validateDeviceChannel(ThreeDSDataElement element, AREQ areq) {
        return Arrays.stream(element.getSupportedChannel())
                .anyMatch(sc -> sc.getChannel().equals(areq.getDeviceChannel()));
    }

    public static boolean validateMessageCategory(ThreeDSDataElement element, AREQ areq) {
        return Arrays.stream(element.getSupportedCategory())
                .anyMatch(sc -> sc.getCategory().equals(areq.getMessageCategory()));
    }
}
