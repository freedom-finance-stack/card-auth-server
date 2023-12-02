package org.freedomfinancestack.razorpay.cas.acs.validation;

import java.util.Arrays;

import org.freedomfinancestack.extensions.validation.enums.DataLengthType;
import org.freedomfinancestack.extensions.validation.exception.ValidationException;
import org.freedomfinancestack.extensions.validation.validator.Validation;
import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.ThreeDSConstant;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.MessageExtension;
import org.freedomfinancestack.razorpay.cas.contract.constants.EMVCOConstant;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.contract.enums.ThreeRIInd;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.extensions.validation.validator.basic.IsIP.isIP;
import static org.freedomfinancestack.extensions.validation.validator.basic.IsNumeric.isNumeric;
import static org.freedomfinancestack.extensions.validation.validator.basic.IsUUID.isUUID;
import static org.freedomfinancestack.extensions.validation.validator.basic.IsValidObject.isValidObject;
import static org.freedomfinancestack.extensions.validation.validator.basic.NotBlank.notBlank;
import static org.freedomfinancestack.extensions.validation.validator.basic.NotEmpty.notEmpty;
import static org.freedomfinancestack.extensions.validation.validator.enriched.IsDate.isDate;
import static org.freedomfinancestack.extensions.validation.validator.enriched.IsIn.isIn;
import static org.freedomfinancestack.extensions.validation.validator.enriched.LengthValidator.lengthValidator;
import static org.freedomfinancestack.extensions.validation.validator.enriched.NotIn.notIn;
import static org.freedomfinancestack.extensions.validation.validator.enriched.RegexValidator.regexValidator;
import static org.freedomfinancestack.extensions.validation.validator.enriched.isJsonObjectLengthValid.isJsonObjectLengthValid;
import static org.freedomfinancestack.extensions.validation.validator.enriched.isListLengthValid.isListLengthValid;
import static org.freedomfinancestack.extensions.validation.validator.rule.IsListValid.isListValid;
import static org.freedomfinancestack.extensions.validation.validator.rule.When.when;

/**
 * The {@code AuthenticationRequestValidator} class is responsible for validating the Authentication
 * Request (AREQ) received from the 3DS Server.
 *
 * <p>This class is annotated with Lombok annotation {@code @Slf4j} to enable logging, and it is
 * also annotated with Spring annotation {@code @Component} to mark it as a Spring component with
 * the name "authenticationRequestValidator".
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Component(value = "authenticationRequestValidator")
public class AuthenticationRequestValidator implements ThreeDSValidator<AREQ> {

    /**
     * Validates the authentication request {@link AREQ}.
     *
     * @param request The authentication request {@link AREQ} to be validated.
     * @throws ACSValidationException If the request fails validation.
     */
    @Override
    public void validateRequest(AREQ request) throws ACSValidationException {
        validateAuthenticationRequest(request);
    }

    /**
     * Validates the authentication request {@link AREQ} by performing various validation checks.
     *
     * @param request The authentication request {@link AREQ} to be validated.
     * @throws ACSValidationException If the request fails validation.
     */
    private void validateAuthenticationRequest(AREQ request) throws ACSValidationException {
        try {
            validateMandatoryFields(request);
            validateConditionalFields(request);
            validateOptionalFields(request);
        } catch (ValidationException vex) {
            throw new ACSValidationException(vex);
        }
    }

    private void validateMandatoryFields(AREQ request) throws ValidationException {

        Validation.validate(
                ThreeDSDataElement.DEVICE_CHANNEL.getFieldName(),
                request.getDeviceChannel(),
                notBlank(),
                isIn(ThreeDSDataElement.DEVICE_CHANNEL.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_CATEGORY.getFieldName(),
                request.getMessageCategory(),
                notBlank(),
                isIn(ThreeDSDataElement.MESSAGE_CATEGORY.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_TYPE.getFieldName(),
                request.getMessageType(),
                notBlank(),
                isIn(ThreeDSDataElement.MESSAGE_TYPE.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MESSAGE_VERSION.getFieldName(),
                request.getMessageVersion(),
                notBlank(),
                lengthValidator(DataLengthType.VARIABLE, 8),
                isIn(ThreeDSDataElement.MESSAGE_VERSION.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.THREEDS_COMPIND.getFieldName(),
                request.getThreeDSCompInd(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_COMPIND, request),
                        notBlank()),
                lengthValidator(DataLengthType.FIXED, 1),
                isIn(ThreeDSDataElement.THREEDS_COMPIND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_URL.getFieldName(),
                request.getThreeDSRequestorURL(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_REQUESTOR_URL, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 2048));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_ID.getFieldName(),
                request.getThreeDSRequestorID(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_REQUESTOR_ID, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 35));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND.getFieldName(),
                request.getThreeDSRequestorAuthenticationInd(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND, request),
                        notBlank()),
                isIn(ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_NAME.getFieldName(),
                request.getThreeDSRequestorName(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_REQUESTOR_NAME, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 40));
        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_REF_NUMBER.getFieldName(),
                request.getThreeDSServerRefNumber(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_SERVER_REF_NUMBER, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 32));
        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID.getFieldName(),
                request.getThreeDSServerTransID(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 36));
        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_URL_2_2_0.getFieldName(),
                request.getThreeDSServerURL(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_SERVER_URL_2_2_0, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 2048));
        Validation.validate(
                ThreeDSDataElement.THREEDS_SERVER_URL_2_1_0.getFieldName(),
                request.getThreeDSServerURL(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_SERVER_URL_2_1_0, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 2048));
        Validation.validate(
                ThreeDSDataElement.THREEDS_RI_IND.getFieldName(),
                request.getThreeRIInd(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.THREEDS_RI_IND, request),
                        notBlank()),
                isIn(ThreeDSDataElement.THREEDS_RI_IND.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.ACQUIRER_BIN.getFieldName(),
                request.getAcquirerBIN(),
                when(
                        shouldValidateThreeDSDataElement(ThreeDSDataElement.ACQUIRER_BIN, request),
                        notBlank(),
                        notEmpty()),
                lengthValidator(DataLengthType.VARIABLE, 11));

        Validation.validate(
                ThreeDSDataElement.ACQUIRER_MERCHANT_ID.getFieldName(),
                request.getAcquirerMerchantID(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.ACQUIRER_MERCHANT_ID, request),
                        notBlank(),
                        notEmpty()),
                lengthValidator(DataLengthType.VARIABLE, 15));

        Validation.validate(
                ThreeDSDataElement.BROWSER_JAVA_ENABLED.getFieldName(),
                request.getBrowserJavaEnabled(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.BROWSER_JAVA_ENABLED, request),
                        notBlank()),
                isIn(ThreeDSDataElement.BROWSER_JAVA_ENABLED.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.BROWSER_ACCEPT_HEADER.getFieldName(),
                request.getBrowserAcceptHeader(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.BROWSER_ACCEPT_HEADER, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 2048));
        Validation.validate(
                ThreeDSDataElement.BROWSER_JAVA_SCRIPT_ENABLED.getFieldName(),
                request.getBrowserJavascriptEnabled(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.BROWSER_JAVA_SCRIPT_ENABLED, request),
                        notBlank()),
                isIn(ThreeDSDataElement.BROWSER_JAVA_SCRIPT_ENABLED.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.BROWSER_LANGUAGE.getFieldName(),
                request.getBrowserLanguage(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.BROWSER_LANGUAGE, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 8));
        Validation.validate(
                ThreeDSDataElement.BROWSER_COLOR_DEPTH.getFieldName(),
                request.getBrowserColorDepth(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.BROWSER_COLOR_DEPTH, request),
                        notBlank()),
                isIn(ThreeDSDataElement.BROWSER_COLOR_DEPTH.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.BROWSER_SCREEN_HEIGHT.getFieldName(),
                request.getBrowserScreenHeight(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.BROWSER_SCREEN_HEIGHT, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 6),
                isNumeric());
        Validation.validate(
                ThreeDSDataElement.BROWSER_SCREEN_WIDTH.getFieldName(),
                request.getBrowserScreenWidth(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.BROWSER_SCREEN_WIDTH, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 6),
                isNumeric());
        Validation.validate(
                ThreeDSDataElement.BROWSER_TZ.getFieldName(),
                request.getBrowserTZ(),
                when(
                        shouldValidateThreeDSDataElement(ThreeDSDataElement.BROWSER_TZ, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 5),
                regexValidator("[0-9-.+]{1,5}"));
        Validation.validate(
                ThreeDSDataElement.BROWSER_USER_AGENT.getFieldName(),
                request.getBrowserUserAgent(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.BROWSER_USER_AGENT, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 2048));
        Validation.validate(
                ThreeDSDataElement.ACCT_NUMBER.getFieldName(),
                request.getAcctNumber(),
                when(
                        shouldValidateThreeDSDataElement(ThreeDSDataElement.ACCT_NUMBER, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 19));
        Validation.validate(
                ThreeDSDataElement.DEVICE_RENDER_OPTIONS.getFieldName(),
                request.getDeviceRenderOptions(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.DEVICE_RENDER_OPTIONS, request),
                        notBlank()));
        if (request.getDeviceRenderOptions() != null) {
            Validation.validate(
                    ThreeDSDataElement.DEVICE_RENDER_OPTIONS_SDK_INTERFACE.getFieldName(),
                    request.getDeviceRenderOptions().getSdkInterface(),
                    when(
                            shouldValidateThreeDSDataElement(
                                    ThreeDSDataElement.DEVICE_RENDER_OPTIONS, request),
                            notBlank()),
                    lengthValidator(DataLengthType.FIXED, 2),
                    isIn(
                            ThreeDSDataElement.DEVICE_RENDER_OPTIONS_SDK_INTERFACE
                                    .getAcceptedValues()));
            Validation.validate(
                    ThreeDSDataElement.DEVICE_RENDER_OPTIONS.getFieldName(),
                    Arrays.stream(request.getDeviceRenderOptions().getSdkUiType()).toList(),
                    when(
                            shouldValidateThreeDSDataElement(
                                    ThreeDSDataElement.DEVICE_RENDER_OPTIONS, request),
                            notBlank()),
                    isListLengthValid(DataLengthType.VARIABLE, 5),
                    isListValid(lengthValidator(DataLengthType.FIXED, 2)),
                    isListValid(
                            isIn(
                                    ThreeDSDataElement.DEVICE_RENDER_OPTIONS_SDK_UI_TYPE
                                            .getAcceptedValues())));
        }
        Validation.validate(
                ThreeDSDataElement.NOTIFICATION_URL.getFieldName(),
                request.getNotificationURL(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.NOTIFICATION_URL, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 2048));

        Validation.validate(
                ThreeDSDataElement.SDK_APP_ID.getFieldName(),
                request.getSdkAppID(),
                when(
                        shouldValidateThreeDSDataElement(ThreeDSDataElement.SDK_APP_ID, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 36));
        Validation.validate(
                ThreeDSDataElement.SDK_MAX_TIMEOUT.getFieldName(),
                request.getSdkMaxTimeout(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.SDK_MAX_TIMEOUT, request),
                        notBlank()),
                lengthValidator(DataLengthType.FIXED, 2),
                notIn(EMVCOConstant.sdkMaxTimeoutList));
        Validation.validate(
                ThreeDSDataElement.SDK_REFERENCE_NUMBER.getFieldName(),
                request.getSdkReferenceNumber(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.SDK_REFERENCE_NUMBER, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 32));
        Validation.validate(
                ThreeDSDataElement.SDK_EPHEM_PUB_KEY.getFieldName(),
                request.getSdkEphemPubKey(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.SDK_EPHEM_PUB_KEY, request),
                        notBlank()),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.SDK_EPHEM_PUB_KEY.getFieldName(),
                request.getSdkEphemPubKey(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.SDK_EPHEM_PUB_KEY, request),
                        notBlank()),
                isJsonObjectLengthValid(256));
        Validation.validate(
                ThreeDSDataElement.SDK_TRANS_ID.getFieldName(),
                request.getSdkTransID(),
                when(
                        shouldValidateThreeDSDataElement(ThreeDSDataElement.SDK_TRANS_ID, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 36),
                isUUID());
        Validation.validate(
                ThreeDSDataElement.DEVICE_INFO.getFieldName(),
                request.getDeviceInfo(),
                when(
                        shouldValidateThreeDSDataElement(ThreeDSDataElement.DEVICE_INFO, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 64000),
                regexValidator("^[A-Za-z0-9_-]*$"));

        Validation.validate(
                ThreeDSDataElement.DS_REFERENCE_NUMBER.getFieldName(),
                request.getDsReferenceNumber(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.DS_REFERENCE_NUMBER, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 32));
        Validation.validate(
                ThreeDSDataElement.DS_TRANS_ID.getFieldName(),
                request.getDsTransID(),
                when(
                        shouldValidateThreeDSDataElement(ThreeDSDataElement.DS_TRANS_ID, request),
                        notBlank()),
                lengthValidator(DataLengthType.FIXED, 36));
        Validation.validate(
                ThreeDSDataElement.DS_URL.getFieldName(),
                request.getDsURL(),
                when(
                        shouldValidateThreeDSDataElement(ThreeDSDataElement.DS_URL, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 2048));
    }

    private void validateConditionalFields(AREQ request) throws ValidationException {

        // Conditional Fields
        Validation.validate(
                ThreeDSDataElement.MCC.getFieldName(),
                request.getMcc(),
                when(
                        shouldValidateThreeDSDataElement(ThreeDSDataElement.MCC, request),
                        notBlank(),
                        notEmpty()),
                lengthValidator(DataLengthType.FIXED, 4),
                isNumeric());
        Validation.validate(
                ThreeDSDataElement.MERCHANT_COUNTRY_CODE.getFieldName(),
                request.getMerchantCountryCode(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.MERCHANT_COUNTRY_CODE, request),
                        notBlank(),
                        notEmpty()),
                lengthValidator(DataLengthType.FIXED, 3),
                isNumeric(),
                notIn(EMVCOConstant.excludedCountry));
        Validation.validate(
                ThreeDSDataElement.MERCHANT_NAME.getFieldName(),
                request.getMerchantName(),
                when(
                        shouldValidateThreeDSDataElement(ThreeDSDataElement.MERCHANT_NAME, request),
                        notBlank(),
                        notEmpty()),
                lengthValidator(DataLengthType.VARIABLE, 40));
        Validation.validate(
                ThreeDSDataElement.PAY_TOKEN_SOURCE.getFieldName(),
                request.getPayTokenSource(),
                when(
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.PAY_TOKEN_SOURCE, request)
                                && !Util.isNullorBlank(request.getPayTokenInd()),
                        notBlank()),
                isIn(ThreeDSDataElement.PAY_TOKEN_SOURCE.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQ_AUTH_METHOD_IND.getFieldName(),
                request.getThreeDSReqAuthMethodInd(),
                when(
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.THREEDS_REQ_AUTH_METHOD_IND, request)
                                && !Util.isNullorBlank(request.getThreeDSReqAuthMethodInd()),
                        notBlank()),
                isIn(ThreeDSDataElement.THREEDS_REQ_AUTH_METHOD_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.WHITE_LIST_STATUS_SOURCE.getFieldName(),
                request.getWhiteListStatusSource(),
                when(
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.WHITE_LIST_STATUS_SOURCE, request)
                                && !Util.isNullorBlank(request.getWhiteListStatus()),
                        notBlank(),
                        notEmpty()),
                isIn(ThreeDSDataElement.WHITE_LIST_STATUS_SOURCE.getAcceptedValues()));

        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_DEC_MAX_TIME.getFieldName(),
                request.getThreeDSRequestorDecMaxTime(),
                when(
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.THREEDS_REQUESTOR_DEC_MAX_TIME, request)
                                && !Util.isNullorBlank(request.getThreeDSRequestorDecReqInd())
                                && InternalConstants.YES.equals(
                                        request.getThreeDSRequestorDecReqInd()),
                        notBlank()));

        Validation.validate(
                ThreeDSDataElement.MESSAGE_EXTENSION.getFieldName(),
                request.getMessageExtension(),
                isListValid(isValidObject()),
                isListLengthValid(DataLengthType.VARIABLE, 10));

        if (request.getMessageExtension() != null) {
            for (MessageExtension messageExtension : request.getMessageExtension()) {
                Validation.validate(
                        ThreeDSDataElement.MESSAGE_EXTENSION_CRITICAL_INDICATOR.getFieldName(),
                        Boolean.toString(messageExtension.isCriticalityIndicator()),
                        isIn(
                                ThreeDSDataElement.MESSAGE_EXTENSION_CRITICAL_INDICATOR
                                        .getAcceptedValues()));
            }
        }

        Validation.validate(
                ThreeDSDataElement.MESSAGE_EXTENSION.getFieldName(),
                request.getMessageExtension(),
                isJsonObjectLengthValid(81920));

        boolean purchaseNPARule =
                (!Util.isNullorBlank(request.getThreeDSRequestorAuthenticationInd())
                                && Arrays.asList(
                                                InternalConstants
                                                        .THREE_DS_REQUESTOR_AUTHENTICATION_IND_02,
                                                InternalConstants
                                                        .THREE_DS_REQUESTOR_AUTHENTICATION_IND_03)
                                        .contains(request.getThreeDSRequestorAuthenticationInd()))
                        || (!Util.isNullorBlank(request.getThreeRIInd())
                                && Arrays.asList(
                                                ThreeRIInd.RECURRING_TRANSACTION,
                                                ThreeRIInd.INSTALMENT_TRANSACTION,
                                                ThreeRIInd.SPLIT_DELAYED_SHIPMENT,
                                                ThreeRIInd.TOP_UP,
                                                ThreeRIInd.MAIL_ORDER,
                                                ThreeRIInd.TELEPHONE_ORDER,
                                                ThreeRIInd.OTHER_PAYMENT)
                                        .contains(request.getThreeRIInd())
                                && ThreeDSConstant.MESSAGE_VERSION_2_2_0.equals(
                                        request.getMessageVersion()));
        boolean purchaseElementsWhenRule =
                validateDeviceChannel(ThreeDSDataElement.PURCHASE_AMOUNT, request)
                        && (request.getMessageCategory().equals(MessageCategory.PA.getCategory())
                                || (request.getMessageCategory()
                                                .equals(MessageCategory.NPA.getCategory())
                                        && purchaseNPARule));
        Validation.validate(
                ThreeDSDataElement.PURCHASE_AMOUNT.getFieldName(),
                request.getPurchaseAmount(),
                when(purchaseElementsWhenRule, notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 48),
                isNumeric());

        Validation.validate(
                ThreeDSDataElement.PURCHASE_CURRENCY.getFieldName(),
                request.getPurchaseCurrency(),
                when(purchaseElementsWhenRule, notBlank()),
                lengthValidator(DataLengthType.FIXED, 3),
                notIn(EMVCOConstant.excludedCountry));
        Validation.validate(
                ThreeDSDataElement.PURCHASE_EXPONENT.getFieldName(),
                request.getPurchaseExponent(),
                when(purchaseElementsWhenRule, notBlank()),
                isIn(ThreeDSDataElement.PURCHASE_EXPONENT.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.PURCHASE_DATE.getFieldName(),
                request.getPurchaseDate(),
                when(purchaseElementsWhenRule, notBlank()),
                lengthValidator(DataLengthType.FIXED, 14),
                isDate(ThreeDSDataElement.PURCHASE_DATE.getAcceptedFormat()));

        String authenticationInd = request.getThreeDSRequestorAuthenticationInd();
        String threeRIInd = request.getThreeRIInd();
        Validation.validate(
                ThreeDSDataElement.PURCHASE_INSTAL_DATA_2_2_0.getFieldName(),
                request.getPurchaseInstalData(),
                when(
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.PURCHASE_INSTAL_DATA_2_2_0, request)
                                && ((!Util.isNullorBlank(authenticationInd)
                                                && InternalConstants
                                                        .THREE_DS_REQUESTOR_AUTHENTICATION_IND_03
                                                        .equals(authenticationInd))
                                        || (DeviceChannel.TRI
                                                        .getChannel()
                                                        .equals(request.getDeviceChannel())
                                                && threeRIInd.equals(
                                                        ThreeRIInd.INSTALMENT_TRANSACTION
                                                                .getValue()))),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 3));
        Validation.validate(
                ThreeDSDataElement.PURCHASE_INSTAL_DATA_2_1_0.getFieldName(),
                request.getPurchaseInstalData(),
                when(
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.PURCHASE_INSTAL_DATA_2_1_0, request)
                                && (!Util.isNullorBlank(authenticationInd)
                                        && InternalConstants
                                                .THREE_DS_REQUESTOR_AUTHENTICATION_IND_03
                                                .equals(authenticationInd)),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 3));

        boolean isAuthenticationIndValid =
                !Util.isNullorBlank(authenticationInd)
                        && (InternalConstants.THREE_DS_REQUESTOR_AUTHENTICATION_IND_02.equals(
                                        authenticationInd)
                                || InternalConstants.THREE_DS_REQUESTOR_AUTHENTICATION_IND_03
                                        .equals(authenticationInd));
        boolean isThreeRIIndValid =
                !Util.isNullorBlank(threeRIInd)
                        && (ThreeRIInd.RECURRING_TRANSACTION.getValue().equals(threeRIInd)
                                || ThreeRIInd.INSTALMENT_TRANSACTION.getValue().equals(threeRIInd));

        Validation.validate(
                ThreeDSDataElement.RECURRING_EXPIRY_2_2_0.getFieldName(),
                request.getRecurringFrequency(),
                when(
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.RECURRING_EXPIRY_2_2_0, request)
                                && (isAuthenticationIndValid || isThreeRIIndValid),
                        notBlank()));

        Validation.validate(
                ThreeDSDataElement.RECURRING_FREQUENCY_2_1_0.getFieldName(),
                request.getThreeDSServerURL(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.RECURRING_FREQUENCY_2_1_0, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 2048));

        Validation.validate(
                ThreeDSDataElement.RECURRING_EXPIRY_2_2_0.getFieldName(),
                request.getRecurringExpiry(),
                when(
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.RECURRING_EXPIRY_2_2_0, request)
                                && (isAuthenticationIndValid || isThreeRIIndValid),
                        notBlank()));

        Validation.validate(
                ThreeDSDataElement.RECURRING_EXPIRY_2_1_0.getFieldName(),
                request.getThreeDSServerURL(),
                when(
                        shouldValidateThreeDSDataElement(
                                ThreeDSDataElement.RECURRING_EXPIRY_2_1_0, request),
                        notBlank()),
                lengthValidator(DataLengthType.VARIABLE, 2048));
    }

    protected void validateOptionalFields(AREQ request) throws ValidationException {

        // Optional Fields
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_INFO.getFieldName(),
                request.getThreeDSRequestorAuthenticationInfo(),
                notEmpty(),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_CHALLENGE_IND.getFieldName(),
                request.getThreeDSRequestorChallengeInd(),
                notEmpty(),
                isIn(ThreeDSDataElement.THREEDS_REQUESTOR_CHALLENGE_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_PRIOR_AUTHENTICATION_INFO.getFieldName(),
                request.getThreeDSRequestorPriorAuthenticationInfo(),
                notEmpty(),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.ADDRESS_MATCH.getFieldName(),
                request.getAddrMatch(),
                notEmpty(),
                isIn(ThreeDSDataElement.ADDRESS_MATCH.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.ACCT_ID.getFieldName(),
                request.getAcctID(),
                when(
                        shouldValidateThreeDSDataElement(ThreeDSDataElement.ACCT_ID, request),
                        notEmpty()),
                lengthValidator(DataLengthType.VARIABLE, 64));
        Validation.validate(
                ThreeDSDataElement.ACCT_TYPE.getFieldName(),
                request.getAcctType(),
                isIn(ThreeDSDataElement.ACCT_TYPE.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.MERCHANT_RISK_INDICATOR.getFieldName(),
                request.getMerchantRiskIndicator(),
                notEmpty(),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.WHITE_LIST_STATUS.getFieldName(),
                request.getWhiteListStatus(),
                isIn(ThreeDSDataElement.WHITE_LIST_STATUS.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.THREEDS_REQUESTOR_DEC_REQ_IND.getFieldName(),
                request.getThreeDSRequestorDecReqInd(),
                notEmpty(),
                isIn(ThreeDSDataElement.THREEDS_REQUESTOR_DEC_REQ_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.TRANS_TYPE.getFieldName(),
                request.getTransType(),
                lengthValidator(DataLengthType.FIXED, 2),
                isNumeric());
        Validation.validate(
                ThreeDSDataElement.PAY_TOKEN_IND.getFieldName(),
                request.getPayTokenInd(),
                isIn(ThreeDSDataElement.PAY_TOKEN_IND.getAcceptedValues()));
        Validation.validate(
                ThreeDSDataElement.CARD_EXPIRY_DATE.getFieldName(),
                request.getCardExpiryDate(),
                lengthValidator(DataLengthType.FIXED, 4),
                isDate("yyMM"));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_CITY.getFieldName(),
                request.getBillAddrCity(),
                lengthValidator(DataLengthType.VARIABLE, 50));

        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_COUNTRY.getFieldName(),
                request.getBillAddrCountry(),
                when(
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.BILL_ADDR_COUNTRY, request)
                                && !Util.isNullorBlank(request.getBillAddrState()),
                        notBlank()),
                lengthValidator(DataLengthType.FIXED, 3),
                isNumeric(),
                notIn(EMVCOConstant.excludedCountry));

        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_LINE_1.getFieldName(),
                request.getBillAddrLine1(),
                lengthValidator(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_LINE_2.getFieldName(),
                request.getBillAddrLine2(),
                lengthValidator(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_LINE_3.getFieldName(),
                request.getBillAddrLine3(),
                lengthValidator(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_POST_CODE.getFieldName(),
                request.getBillAddrPostCode(),
                lengthValidator(DataLengthType.VARIABLE, 16));
        Validation.validate(
                ThreeDSDataElement.BILL_ADDR_STATE.getFieldName(),
                request.getBillAddrState(),
                lengthValidator(DataLengthType.VARIABLE, 3));
        Validation.validate(
                ThreeDSDataElement.EMAIL.getFieldName(),
                request.getEmail(),
                lengthValidator(DataLengthType.VARIABLE, 254));
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
                lengthValidator(DataLengthType.VARIABLE, 45));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_CITY.getFieldName(),
                request.getShipAddrCity(),
                lengthValidator(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_COUNTRY.getFieldName(),
                request.getShipAddrCountry(),
                when(
                        shouldValidateThreeDSDataElement(
                                        ThreeDSDataElement.SHIP_ADDR_COUNTRY, request)
                                && !Util.isNullorBlank(request.getShipAddrState()),
                        notBlank()),
                lengthValidator(DataLengthType.FIXED, 3),
                isNumeric(),
                notIn(EMVCOConstant.excludedCountry));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_LINE_1.getFieldName(),
                request.getShipAddrLine1(),
                lengthValidator(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_LINE_2.getFieldName(),
                request.getShipAddrLine2(),
                lengthValidator(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_LINE_3.getFieldName(),
                request.getShipAddrLine3(),
                lengthValidator(DataLengthType.VARIABLE, 50));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_POST_CODE.getFieldName(),
                request.getShipAddrPostCode(),
                lengthValidator(DataLengthType.VARIABLE, 16));
        Validation.validate(
                ThreeDSDataElement.SHIP_ADDR_STATE.getFieldName(),
                request.getShipAddrState(),
                lengthValidator(DataLengthType.VARIABLE, 3));
        Validation.validate(
                ThreeDSDataElement.WORK_PHONE.getFieldName(),
                request.getWorkPhone(),
                isValidObject());
        Validation.validate(
                ThreeDSDataElement.BROWSER_IP.getFieldName(),
                request.getBrowserIP(),
                lengthValidator(DataLengthType.VARIABLE, 45),
                isIP());
        Validation.validate(
                ThreeDSDataElement.ACCT_INFO.getFieldName(),
                request.getAcctInfo(),
                notEmpty(),
                isValidObject());
    }

    private static boolean shouldValidateThreeDSDataElement(
            @NonNull final ThreeDSDataElement element, @NonNull final AREQ areq) {
        return validateDeviceChannel(element, areq)
                && validateMessageCategory(element, areq)
                && validateMessageVersion(element, areq);
    }

    private static boolean validateDeviceChannel(
            @NonNull final ThreeDSDataElement element, @NonNull final AREQ areq) {
        return Arrays.stream(element.getSupportedChannel())
                .anyMatch(sc -> sc.getChannel().equals(areq.getDeviceChannel()));
    }

    private static boolean validateMessageCategory(
            @NonNull final ThreeDSDataElement element, @NonNull final AREQ areq) {
        return Arrays.stream(element.getSupportedCategory())
                .anyMatch(sc -> sc.getCategory().equals(areq.getMessageCategory()));
    }

    private static boolean validateMessageVersion(
            @NonNull final ThreeDSDataElement element, @NonNull final AREQ areq) {
        return Arrays.stream(element.getSupportedMessageVersion())
                .anyMatch(version -> version.equals(areq.getMessageVersion()));
    }
}
