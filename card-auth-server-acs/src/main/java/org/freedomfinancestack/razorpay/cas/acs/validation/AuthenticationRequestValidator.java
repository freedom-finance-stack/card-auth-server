package org.freedomfinancestack.razorpay.cas.acs.validation;

import java.util.Arrays;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.Validation;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched.LengthValidator;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.constants.EMVCOConstant;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic.IsIP.isIP;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic.IsNumeric.isNumeric;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic.IsUUID.isUUID;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic.IsValidObject.isValidObject;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic.NotNull.notNull;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched.IsDate.isDate;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched.IsIn.isIn;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched.LengthValidator.lengthValidator;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched.NotIn.notIn;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched.RegexValidator.regexValidator;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.rule.IsListValid.isListValid;
import static org.freedomfinancestack.razorpay.cas.acs.validation.validator.rule.When.when;

/**
 * The {@code AuthenticationRequestValidator} class is responsible for validating the Authentication
 * Request (AREQ) received from the 3DS Server.
 *
 * <p>This class is annotated with Lombok annotation {@code @Slf4j} to enable logging, and it is
 * also annotated with Spring annotation {@code @Component} to mark it as a Spring component with
 * the name "authenticationRequestValidator".
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Slf4j
@Component(value = "authenticationRequestValidator")
public class AuthenticationRequestValidator implements ThreeDSValidator<AREQ> {

    /**
     * Validates the authentication request (AREQ).
     *
     * @param request The authentication request (AREQ) to be validated.
     * @throws ValidationException If the request fails validation.
     */
    @Override
    public void validateRequest(AREQ request) throws ValidationException {
        validateAuthenticationRequest(request);
    }

    /**
     * Validates the authentication request (AREQ) by performing various validation checks.
     *
     * @param request The authentication request (AREQ) to be validated.
     * @throws ValidationException If the request fails validation.
     */
    private void validateAuthenticationRequest(AREQ request) throws ValidationException {
        validateMandatoryFields(request);
        validateConditionalFields(request);
        validateOptionalFields(request);
    }

    private void validateMandatoryFields(AREQ request) throws ValidationException {

        Validation.validate(
                ThreeDSDataElement.DEVICE_CHANNEL.getFieldName(),
                request.getDeviceChannel(),
                notNull(),
                isIn(ThreeDSDataElement.DEVICE_CHANNEL.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_CATEGORY.getFieldName(),
                request.getMessageCategory(),
                notNull(),
                isIn(ThreeDSDataElement.MESSAGE_CATEGORY.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_TYPE.getFieldName(),
                request.getMessageType(),
                notNull(),
                isIn(ThreeDSDataElement.MESSAGE_TYPE.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_VERSION.getFieldName(),
                request.getMessageVersion(),
                notNull(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 8),
                isIn(
                        ThreeDSDataElement.MESSAGE_VERSION
                                .getAcceptedValues())); // todo handle exception for message version

        Validation.validate(
                ThreeDSDataElement.THREEDS_COMPIND.getFieldName(),
                request.getThreeDSCompInd(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_COMPIND, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.FIXED, 1),
                isIn(ThreeDSDataElement.THREEDS_COMPIND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_URL.getFieldName(),
                request.getThreeDSRequestorURL(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_REQUESTOR_URL, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 2048));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_ID.getFieldName(),
                request.getThreeDSRequestorID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_REQUESTOR_ID, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 35));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND.getFieldName(),
                request.getThreeDSRequestorAuthenticationInd(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND, request),
                        notNull()),
                isIn(ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_NAME.getFieldName(),
                request.getThreeDSRequestorName(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_REQUESTOR_NAME, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 40));
        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_REF_NUMBER.getFieldName(),
                request.getThreeDSServerRefNumber(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_SERVER_REF_NUMBER, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 32));
        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID.getFieldName(),
                request.getThreeDSServerTransID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 36));
        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_URL.getFieldName(),
                request.getThreeDSServerURL(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_SERVER_URL, request),
                        notNull()),
                lengthValidator(
                        LengthValidator.DataLengthType.VARIABLE,
                        2048)); // todo is message version condition needed ?
        Validation.validate(
                ThreeDSDataElement.THREEDS_RI_IND.getFieldName(),
                request.getThreeRIInd(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.THREEDS_RI_IND, request),
                        notNull()),
                isIn(ThreeDSDataElement.THREEDS_RI_IND.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.ACQUIRER_BIN.getFieldName(),
                request.getAcquirerBIN(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.ACQUIRER_BIN, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 11));
        Validation.validate(
                ThreeDSDataElement.ACQUIRER_MERCHANT_ID.getFieldName(),
                request.getAcquirerMerchantID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.ACQUIRER_MERCHANT_ID, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 15));

        Validation.validate(
                ThreeDSDataElement.BROWSER_JAVA_ENABLED.getFieldName(),
                request.getBrowserJavaEnabled(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_JAVA_ENABLED, request),
                        notNull()),
                isIn(ThreeDSDataElement.BROWSER_JAVA_ENABLED.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.BROWSER_ACCEPT_HEADER.getFieldName(),
                request.getBrowserAcceptHeader(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_ACCEPT_HEADER, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 2048));
        Validation.validate(
                ThreeDSDataElement.BROWSER_JAVA_SCRIPT_ENABLED.getFieldName(),
                request.getBrowserJavascriptEnabled(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_JAVA_SCRIPT_ENABLED, request),
                        notNull()),
                isIn(ThreeDSDataElement.BROWSER_JAVA_SCRIPT_ENABLED.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.BROWSER_LANGUAGE.getFieldName(),
                request.getBrowserLanguage(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_LANGUAGE, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 8));
        Validation.validate(
                ThreeDSDataElement.BROWSER_COLOR_DEPTH.getFieldName(),
                request.getBrowserColorDepth(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_COLOR_DEPTH, request),
                        notNull()),
                isIn(ThreeDSDataElement.BROWSER_COLOR_DEPTH.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.BROWSER_SCREEN_HEIGHT.getFieldName(),
                request.getBrowserScreenHeight(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_SCREEN_HEIGHT, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 6),
                isNumeric());
        Validation.validate(
                ThreeDSDataElement.BROWSER_SCREEN_WIDTH.getFieldName(),
                request.getBrowserScreenWidth(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_SCREEN_WIDTH, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 6),
                isNumeric());
        Validation.validate(
                ThreeDSDataElement.BROWSER_TZ.getFieldName(),
                request.getBrowserTZ(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_TZ, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 5),
                regexValidator("[0-9-.]{1,5}"));
        Validation.validate(
                ThreeDSDataElement.BROWSER_USER_AGENT.getFieldName(),
                request.getBrowserUserAgent(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.BROWSER_USER_AGENT, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 2048));
        Validation.validate(
                ThreeDSDataElement.ACCT_NUMBER.getFieldName(),
                request.getAcctNumber(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.ACCT_NUMBER, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 19));
        Validation.validate(
                ThreeDSDataElement.DEVICE_RENDER_OPTIONS.getFieldName(),
                request.getDeviceRenderOptions(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.DEVICE_RENDER_OPTIONS, request),
                        notNull()),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.NOTIFICATION_URL.getFieldName(),
                request.getNotificationURL(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.NOTIFICATION_URL, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 2048));

        Validation.validate(
                ThreeDSDataElement.SDK_APP_ID.getFieldName(),
                request.getSdkAppID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_APP_ID, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 36));
        Validation.validate(
                ThreeDSDataElement.SDK_MAX_TIMEOUT.getFieldName(),
                request.getSdkMaxTimeout(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_MAX_TIMEOUT, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.FIXED, 2));
        Validation.validate(
                ThreeDSDataElement.SDK_REFERENCE_NUMBER.getFieldName(),
                request.getSdkReferenceNumber(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_REFERENCE_NUMBER, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 32));
        Validation.validate(
                ThreeDSDataElement.SDK_EPHEM_PUB_KEY.getFieldName(),
                request.getSdkEphemPubKey(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_EPHEM_PUB_KEY, request),
                        notNull()),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.SDK_TRANS_ID.getFieldName(),
                request.getSdkTransID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.SDK_TRANS_ID, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 36),
                isUUID());
        Validation.validate(
                ThreeDSDataElement.DEVICE_INFO.getFieldName(),
                request.getDeviceInfo(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.DEVICE_INFO, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 64000));

        Validation.validate(
                ThreeDSDataElement.DS_REFERENCE_NUMBER.getFieldName(),
                request.getDsReferenceNumber(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.DS_REFERENCE_NUMBER, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 32));
        Validation.validate(
                ThreeDSDataElement.DS_TRANS_ID.getFieldName(),
                request.getDsTransID(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.DS_TRANS_ID, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.FIXED, 36));
        Validation.validate(
                ThreeDSDataElement.DS_URL.getFieldName(),
                request.getDsURL(),
                when(
                        validateDeviceChannelAndMessageCategory(ThreeDSDataElement.DS_URL, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 2048));
    }

    private void validateConditionalFields(AREQ request) throws ValidationException {

        // Conditional Fields

        Validation.validate(
                ThreeDSDataElement.MCC.getFieldName(),
                request.getMcc(),
                when(
                        validateDeviceChannelAndMessageCategory(ThreeDSDataElement.MCC, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.FIXED, 4),
                isNumeric());
        Validation.validate(
                ThreeDSDataElement.MERCHANT_COUNTRY_CODE.getFieldName(),
                request.getMerchantCountryCode(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.MERCHANT_COUNTRY_CODE, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.FIXED, 3),
                isNumeric(),
                notIn(EMVCOConstant.excludedCountry));
        Validation.validate(
                ThreeDSDataElement.MERCHANT_NAME.getFieldName(),
                request.getMerchantName(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                ThreeDSDataElement.MERCHANT_NAME, request),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 40));
        Validation.validate(
                ThreeDSDataElement.PAY_TOKEN_SOURCE.getFieldName(),
                request.getPayTokenSource(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                        ThreeDSDataElement.PAY_TOKEN_SOURCE, request)
                                && !Util.isNullorBlank(request.getPayTokenInd()),
                        notNull()),
                isIn(ThreeDSDataElement.PAY_TOKEN_SOURCE.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQ_AUTH_METHOD_IND.getFieldName(),
                request.getThreeDSReqAuthMethodInd(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                        ThreeDSDataElement.THREEDS_REQ_AUTH_METHOD_IND, request)
                                && !Util.isNullorBlank(request.getThreeDSReqAuthMethodInd()),
                        notNull()),
                isIn(ThreeDSDataElement.THREEDS_REQ_AUTH_METHOD_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.WHITE_LIST_STATUS_SOURCE.getFieldName(),
                request.getWhiteListStatusSource(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                        ThreeDSDataElement.WHITE_LIST_STATUS_SOURCE, request)
                                && !Util.isNullorBlank(request.getWhiteListStatus()),
                        notNull()),
                isIn(ThreeDSDataElement.WHITE_LIST_STATUS_SOURCE.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_DEC_MAX_TIME.getFieldName(),
                request.getThreeDSRequestorDecMaxTime(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                        ThreeDSDataElement.THREEDS_REQUESTOR_DEC_MAX_TIME, request)
                                && !Util.isNullorBlank(request.getThreeDSRequestorDecReqInd())
                                && InternalConstants.YES.equals(
                                        request.getThreeDSRequestorDecReqInd()),
                        notNull()));

        Validation.validate(
                ThreeDSDataElement.MESSAGE_EXTENSION.getFieldName(),
                request.getMessageExtension(),
                isListValid(isValidObject()));

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
                when(purchaseElementsWhenRule, notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 48),
                isNumeric());

        Validation.validate(
                ThreeDSDataElement.PURCHASE_CURRENCY.getFieldName(),
                request.getPurchaseCurrency(),
                when(purchaseElementsWhenRule, notNull()),
                lengthValidator(LengthValidator.DataLengthType.FIXED, 3),
                notIn(EMVCOConstant.excludedCountry));
        Validation.validate(
                ThreeDSDataElement.PURCHASE_EXPONENT.getFieldName(),
                request.getPurchaseExponent(),
                when(purchaseElementsWhenRule, notNull()),
                isIn(ThreeDSDataElement.PURCHASE_EXPONENT.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.PURCHASE_DATE.getFieldName(),
                request.getPurchaseDate(),
                when(purchaseElementsWhenRule, notNull()),
                lengthValidator(LengthValidator.DataLengthType.FIXED, 14),
                isDate(ThreeDSDataElement.PURCHASE_DATE.getAcceptedFormat()));
        Validation.validate(
                ThreeDSDataElement.PURCHASE_INSTAL_DATA.getFieldName(),
                request.getPurchaseInstalData(),
                when(
                        validateDeviceChannelAndMessageCategory(
                                        ThreeDSDataElement.PURCHASE_INSTAL_DATA, request)
                                && Util.isNullorBlank(
                                        request.getThreeDSRequestorAuthenticationInd())
                                && "03".equals(request.getThreeDSRequestorAuthenticationInd()),
                        notNull()),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 3));
    }

    protected void validateOptionalFields(AREQ request) throws ValidationException {

        // Optional Fields
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_INFO.getFieldName(),
                request.getThreeDSRequestorAuthenticationInfo(),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_CHALLENGE_IND.getFieldName(),
                request.getThreeDSRequestorChallengeInd(),
                isIn(ThreeDSDataElement.THREEDS_REQUESTOR_CHALLENGE_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_PRIOR_AUTHENTICATION_INFO.getFieldName(),
                request.getThreeDSRequestorPriorAuthenticationInfo(),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.ADDRESS_MATCH.getFieldName(),
                request.getAddrMatch(),
                isIn(ThreeDSDataElement.ADDRESS_MATCH.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.ACCT_ID.getFieldName(),
                request.getAcctID(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 64));
        Validation.validate(
                ThreeDSDataElement.ACCT_TYPE.getFieldName(),
                request.getAcctType(),
                isIn(ThreeDSDataElement.ACCT_TYPE.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MERCHANT_RISK_INDICATOR.getFieldName(),
                request.getMerchantRiskIndicator(),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.WHITE_LIST_STATUS.getFieldName(),
                request.getWhiteListStatus(),
                isIn(ThreeDSDataElement.WHITE_LIST_STATUS.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_DEC_REQ_IND.getFieldName(),
                request.getThreeDSRequestorDecReqInd(),
                isIn(ThreeDSDataElement.THREEDS_REQUESTOR_DEC_REQ_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.TRANS_TYPE.getFieldName(),
                request.getTransType(),
                lengthValidator(LengthValidator.DataLengthType.FIXED, 2),
                isNumeric());
        Validation.validate(
                ThreeDSDataElement.PAY_TOKEN_IND.getFieldName(),
                request.getPayTokenInd(),
                isIn(ThreeDSDataElement.PAY_TOKEN_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.CARD_EXPIRY_DATE.getFieldName(),
                request.getCardExpiryDate(),
                lengthValidator(LengthValidator.DataLengthType.FIXED, 4),
                isDate("YYMM"));

        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_CITY.getFieldName(),
                request.getBillAddrCity(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_COUNTRY.getFieldName(),
                request.getBillAddrCountry(),
                lengthValidator(LengthValidator.DataLengthType.FIXED, 3),
                isNumeric(),
                notIn(EMVCOConstant.excludedCountry));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_LINE_1.getFieldName(),
                request.getBillAddrLine1(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_LINE_2.getFieldName(),
                request.getBillAddrLine2(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_LINE_3.getFieldName(),
                request.getBillAddrLine3(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_POST_CODE.getFieldName(),
                request.getBillAddrPostCode(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 16));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_STATE.getFieldName(),
                request.getBillAddrState(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 3));
        Validation.validate(
                ThreeDSDataElement.EMAIL.getFieldName(),
                request.getEmail(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 254));
        Validation.validate(
                ThreeDSDataElement.HOME_PHONE.getFieldName(),
                request.getHomePhone(),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.MOBILE_PHONE.getFieldName(),
                request.getMobilePhone(),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.CARDHOLDER_NAME.getFieldName(),
                request.getCardholderName(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 45));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_CITY.getFieldName(),
                request.getShipAddrCity(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_COUNTRY.getFieldName(),
                request.getShipAddrCountry(),
                lengthValidator(LengthValidator.DataLengthType.FIXED, 3),
                isNumeric(),
                notIn(EMVCOConstant.excludedCountry));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_LINE_1.getFieldName(),
                request.getShipAddrLine1(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_LINE_2.getFieldName(),
                request.getShipAddrLine2(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_LINE_3.getFieldName(),
                request.getShipAddrLine3(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_POST_CODE.getFieldName(),
                request.getShipAddrPostCode(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 16));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_STATE.getFieldName(),
                request.getShipAddrState(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 3));
        Validation.validate(
                ThreeDSDataElement.WORK_PHONE.getFieldName(),
                request.getWorkPhone(),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.BROWSER_IP.getFieldName(),
                request.getBrowserIP(),
                lengthValidator(LengthValidator.DataLengthType.VARIABLE, 45),
                isIP());
        Validation.validate(
                ThreeDSDataElement.ACCT_INFO.getFieldName(),
                request.getAcctInfo(),
                isValidObject());
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
