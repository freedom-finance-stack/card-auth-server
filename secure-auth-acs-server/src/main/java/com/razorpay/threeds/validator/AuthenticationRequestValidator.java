package com.razorpay.threeds.validator;

import com.razorpay.acs.contract.AREQ;
import com.razorpay.acs.contract.enums.MessageCategory;
import com.razorpay.threeds.exception.ValidationException;
import com.razorpay.threeds.validator.enums.DataLengthType;
import com.razorpay.threeds.validator.enums.ThreeDSDataElement;
import com.razorpay.threeds.validator.rules.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "authenticationRequestValidator")
public class AuthenticationRequestValidator implements ThreeDSValidator<AREQ> {

    @Override
    public void validateRequest(AREQ request) throws ValidationException {
        validateAuthenticationRequest(request);
    }

    protected void validateAuthenticationRequest(AREQ request) throws ValidationException {
        validateMandatoryFields(request);
        validateConditionalFields(request);
        //        validateOptionalFields(request);
    }

    protected void validateMandatoryFields(AREQ request) throws ValidationException {

        Validation.validate(ThreeDSDataElement.DEVICE_CHANNEL.getFieldName(), request.getDeviceChannel(), new NotNullRule<>(), new IsInRule(ThreeDSDataElement.DEVICE_CHANNEL.getAcceptedValues()));


        Validation.validate(ThreeDSDataElement.MESSAGE_CATEGORY.getFieldName(), request.getMessageCategory(), new NotNullRule<>(), new IsInRule(ThreeDSDataElement.MESSAGE_CATEGORY.getAcceptedValues()));
        Validation.validate(ThreeDSDataElement.MESSAGE_TYPE.getFieldName(), request.getMessageType(), new NotNullRule<>(), new IsInRule(ThreeDSDataElement.MESSAGE_TYPE.getAcceptedValues()));
        Validation.validate(ThreeDSDataElement.MESSAGE_VERSION.getFieldName(), request.getMessageVersion(), new NotNullRule<>(), new LengthRule(DataLengthType.VARIABLE, 8), new IsInRule(ThreeDSDataElement.MESSAGE_VERSION.getAcceptedValues()));  // todo handle exception for message version

        Validation.validate(ThreeDSDataElement.THREEDS_COMPIND.getFieldName(), request.getThreeDSCompInd(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.THREEDS_COMPIND, request), new NotNullRule<>(), new LengthRule(DataLengthType.FIXED, 1), new IsInRule(ThreeDSDataElement.THREEDS_COMPIND.getAcceptedValues())));
        Validation.validate(ThreeDSDataElement.THREEDS_REQUESTOR_URL.getFieldName(), request.getThreeDSRequestorURL(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.THREEDS_REQUESTOR_URL, request), new NotNullRule<>(), new LengthRule(DataLengthType.VARIABLE, 2048)));
        Validation.validate(ThreeDSDataElement.THREEDS_REQUESTOR_ID.getFieldName(), request.getThreeDSRequestorID(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.THREEDS_REQUESTOR_ID, request), new NotNullRule<>(), new LengthRule(DataLengthType.VARIABLE, 35)));
        Validation.validate(ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND.getFieldName(), request.getThreeDSRequestorAuthenticationInd(), new WhenRule(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND, request), new NotNullRule(), new IsInRule(ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND.getAcceptedValues())));
        Validation.validate(ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_INFO.getFieldName(), request.getThreeDSRequestorAuthenticationInfo(), new WhenRule(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_INFO, request), new IsInRule(ThreeDSDataElement.THREEDS_REQUESTOR_AUTHENTICATION_IND.getAcceptedValues())));
        Validation.validate(ThreeDSDataElement.THREEDS_REQUESTOR_CHALLENGE_IND.getFieldName(), request.getThreeDSRequestorChallengeInd(), new LengthRule(DataLengthType.FIXED, 2), new IsInRule(ThreeDSDataElement.THREEDS_REQUESTOR_CHALLENGE_IND.getAcceptedValues()));
        Validation.validate(ThreeDSDataElement.THREEDS_REQUESTOR_NAME.getFieldName(), request.getThreeDSRequestorName(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.THREEDS_REQUESTOR_NAME, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 40));
        Validation.validate(ThreeDSDataElement.THREEDS_SERVER_REF_NUMBER.getFieldName(), request.getThreeDSServerRefNumber(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.THREEDS_SERVER_REF_NUMBER, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 32));
        Validation.validate(ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID.getFieldName(), request.getThreeDSServerTransID(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 36));
        Validation.validate(ThreeDSDataElement.THREEDS_SERVER_URL.getFieldName(), request.getThreeDSServerURL(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.THREEDS_SERVER_URL, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 2048));  // todo is message version condition needed ?
        Validation.validate(ThreeDSDataElement.THREEDS_RI_IND.getFieldName(), request.getThreeRIInd(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.THREEDS_RI_IND, request), new NotNullRule<>()), new IsInRule(ThreeDSDataElement.THREEDS_RI_IND.getAcceptedValues()));

        Validation.validate(ThreeDSDataElement.ACQUIRER_BIN.getFieldName(), request.getAcquirerBIN(), new WhenRule<>(MessageCategory.PA.getCategory().equals(request.getMessageCategory()), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 11));
        Validation.validate(ThreeDSDataElement.ACQUIRER_MERCHANT_ID.getFieldName(), request.getAcquirerMerchantID(), new WhenRule<>(MessageCategory.PA.getCategory().equals(request.getMessageCategory()), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 15));
        Validation.validate(ThreeDSDataElement.BROWSER_JAVA_ENABLED.getFieldName(), request.getBrowserJavaEnabled(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.BROWSER_JAVA_ENABLED, request), new NotNullRule<>()), new IsInRule(ThreeDSDataElement.BROWSER_JAVA_ENABLED.getAcceptedValues()));
        Validation.validate(ThreeDSDataElement.BROWSER_ACCEPT_HEADER.getFieldName(), request.getBrowserAcceptHeader(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.BROWSER_ACCEPT_HEADER, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 2048));
        Validation.validate(ThreeDSDataElement.BROWSER_JAVA_SCRIPT_ENABLED.getFieldName(), request.getBrowserJavascriptEnabled(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.BROWSER_JAVA_SCRIPT_ENABLED, request), new NotNullRule<>()), new IsInRule(ThreeDSDataElement.BROWSER_JAVA_SCRIPT_ENABLED.getAcceptedValues()));

        Validation.validate(ThreeDSDataElement.BROWSER_LANGUAGE.getFieldName(), request.getBrowserLanguage(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.BROWSER_LANGUAGE, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 8));
        Validation.validate(ThreeDSDataElement.BROWSER_COLOR_DEPTH.getFieldName(), request.getBrowserColorDepth(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.BROWSER_COLOR_DEPTH, request), new NotNullRule<>()), new IsInRule(ThreeDSDataElement.BROWSER_COLOR_DEPTH.getAcceptedValues()));
        Validation.validate(ThreeDSDataElement.BROWSER_SCREEN_HEIGHT.getFieldName(), request.getBrowserScreenHeight(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.BROWSER_SCREEN_HEIGHT, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 6), new isNumericRule());
        Validation.validate(ThreeDSDataElement.BROWSER_SCREEN_WIDTH.getFieldName(), request.getBrowserScreenWidth(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.BROWSER_SCREEN_WIDTH, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 6), new isNumericRule());
        Validation.validate(ThreeDSDataElement.BROWSER_TZ.getFieldName(), request.getBrowserTZ(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.BROWSER_TZ, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 5), new RegexRule("[0-9-.]{1,5}"));
        Validation.validate(ThreeDSDataElement.BROWSER_USER_AGENT.getFieldName(), request.getBrowserUserAgent(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.BROWSER_USER_AGENT, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 2048));
        Validation.validate(ThreeDSDataElement.ACCT_NUMBER.getFieldName(), request.getAcctNumber(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.ACCT_NUMBER, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 19));
        Validation.validate(ThreeDSDataElement.DEVICE_RENDER_OPTIONS.getFieldName(), request.getDeviceRenderOptions(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.DEVICE_RENDER_OPTIONS, request), new NotNullRule<>())); // valid Value rule
        Validation.validate(ThreeDSDataElement.NOTIFICATION_URL.getFieldName(), request.getNotificationURL(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.NOTIFICATION_URL, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 2048));

        Validation.validate(ThreeDSDataElement.SDK_APP_ID.getFieldName(), request.getSdkAppID(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.SDK_APP_ID, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 36));
        Validation.validate(ThreeDSDataElement.SDK_MAX_TIMEOUT.getFieldName(), request.getSdkMaxTimeout(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.SDK_MAX_TIMEOUT, request), new NotNullRule<>()), new LengthRule(DataLengthType.FIXED, 2));
        Validation.validate(ThreeDSDataElement.SDK_REFERENCE_NUMBER.getFieldName(), request.getSdkReferenceNumber(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.SDK_REFERENCE_NUMBER, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 32)); //todo
        Validation.validate(ThreeDSDataElement.SDK_EPHEM_PUB_KEY.getFieldName(), request.getSdkEphemPubKey(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.SDK_EPHEM_PUB_KEY, request), new NotNullRule<>())); // valid value
        Validation.validate(ThreeDSDataElement.SDK_TRANS_ID.getFieldName(), request.getSdkTransID(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.SDK_TRANS_ID, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 36), new isUUIDRule());

    }

    protected void validateConditionalFields(AREQ request) throws ValidationException {
        //Conditional Fields
        Validation.validate(ThreeDSDataElement.MCC.getFieldName(), request.getMcc(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.MCC, request), new NotNullRule<>()), new LengthRule(DataLengthType.FIXED, 4), new isNumericRule());
        Validation.validate(ThreeDSDataElement.MERCHANT_COUNTRY_CODE.getFieldName(), request.getMerchantCountryCode(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.MERCHANT_COUNTRY_CODE, request), new NotNullRule<>()), new LengthRule(DataLengthType.FIXED, 3), new isNumericRule());
        Validation.validate(ThreeDSDataElement.MERCHANT_NAME.getFieldName(), request.getMerchantName(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.MERCHANT_NAME, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 40));
        Validation.validate(ThreeDSDataElement.PURCHASE_AMOUNT.getFieldName(), request.getPurchaseAmount(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.PURCHASE_AMOUNT, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 12), new isNumericRule());
        Validation.validate(ThreeDSDataElement.PURCHASE_CURRENCY.getFieldName(), request.getPurchaseCurrency(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.PURCHASE_CURRENCY, request), new NotNullRule<>()), new LengthRule(DataLengthType.FIXED, 3), new isNumericRule());
        Validation.validate(ThreeDSDataElement.PURCHASE_EXPONENT.getFieldName(), request.getPurchaseExponent(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.PURCHASE_EXPONENT, request), new NotNullRule<>()), new LengthRule(DataLengthType.FIXED, 1), new isNumericRule());
        Validation.validate(ThreeDSDataElement.PURCHASE_DATE.getFieldName(), request.getPurchaseDate(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.PURCHASE_DATE, request), new NotNullRule<>()), new LengthRule(DataLengthType.FIXED, 8), new isNumericRule());
        Validation.validate(ThreeDSDataElement.THREEDS_SERVER_OPERATOR_ID.getFieldName(), request.getThreeDSServerOperatorID(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.THREEDS_SERVER_OPERATOR_ID, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 32));
        Validation.validate(ThreeDSDataElement.ACCT_TYPE.getFieldName(), request.getAcctType(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.ACCT_TYPE, request), new NotNullRule<>()), new LengthRule(DataLengthType.FIXED, 2), new isNumericRule());
        Validation.validate(ThreeDSDataElement.BROAD_INFO.getFieldName(), request.getBroadInfo(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.BROAD_INFO, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 2048));
        Validation.validate(ThreeDSDataElement.BROWSER_IP.getFieldName(), request.getBrowserIP(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.BROWSER_IP, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 45), new isIPRule());
        Validation.validate(ThreeDSDataElement.CARD_EXPIRY_DATE.getFieldName(), request.getCardExpiryDate(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.CARD_EXPIRY_DATE, request), new NotNullRule<>()), new LengthRule(DataLengthType.FIXED, 4), new isNumericRule());
        Validation.validate(ThreeDSDataElement.BILL_ADDR_CITY.getFieldName(), request.getBillAddrCity(), new WhenRule<>(Validation.validateDeviceChannelAndMessageType(ThreeDSDataElement.BILL_ADDR_CITY, request), new NotNullRule<>()), new LengthRule(DataLengthType.VARIABLE, 50));

        //        strategies.add(ThreeDSDataElementValidationStrategy.MCC);
        //        strategies.add(ThreeDSDataElementValidationStrategy.MERCHANT_COUNTRY_CODE);
        //        strategies.add(ThreeDSDataElementValidationStrategy.MERCHANT_NAME);
        //
        //        strategies.add(ThreeDSDataElementValidationStrategy.PURCHASE_AMOUNT);
        //        strategies.add(ThreeDSDataElementValidationStrategy.PURCHASE_CURRENCY);
        //        strategies.add(ThreeDSDataElementValidationStrategy.PURCHASE_EXPONENT);
        //        strategies.add(ThreeDSDataElementValidationStrategy.PURHASE_DATE);


        //        strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_SERVER_OPERATOR_ID);
        //        strategies.add(ThreeDSDataElementValidationStrategy.ACCT_TYPE);
        //        strategies.add(ThreeDSDataElementValidationStrategy.BROAD_INFO);
        //        strategies.add(ThreeDSDataElementValidationStrategy.BROWSER_IP);
        //        strategies.add(ThreeDSDataElementValidationStrategy.CARD_EXPIRY_DATE);
        //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_CITY);
        //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_COUNTRY);
        //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_LINE_1);
        //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_LINE_2);
        //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_LINE_3);
        //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_POST_CODE);
        //        strategies.add(ThreeDSDataElementValidationStrategy.BILL_ADDR_STATE);
        //        strategies.add(ThreeDSDataElementValidationStrategy.EMAIL);
        //        strategies.add(ThreeDSDataElementValidationStrategy.HOME_PHONE);
        //        strategies.add(ThreeDSDataElementValidationStrategy.MOBILE_PHONE);
        //        strategies.add(ThreeDSDataElementValidationStrategy.CARDHOLDER_NAME);
        //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_CITY);
        //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_COUNTRY);
        //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_LINE_1);
        //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_LINE_2);
        //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_LINE_3);
        //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_POST_CODE);
        //        strategies.add(ThreeDSDataElementValidationStrategy.SHIP_ADDR_STATE);
        //        strategies.add(ThreeDSDataElementValidationStrategy.WORK_PHONE);
        //        strategies.add(ThreeDSDataElementValidationStrategy.DEVICE_INFO);
        //        strategies.add(ThreeDSDataElementValidationStrategy.DS_REFERENCE_NUMBER);
        //        strategies.add(ThreeDSDataElementValidationStrategy.DS_TRANS_ID);
        //        strategies.add(ThreeDSDataElementValidationStrategy.DS_URL);
        //        strategies.add(ThreeDSDataElementValidationStrategy.PAY_TOKEN_IND);
        //        strategies.add(ThreeDSDataElementValidationStrategy.PURCHASE_INSTAL_DATA);
        //        strategies.add(ThreeDSDataElementValidationStrategy.MESSAGE_EXTENSION);
        //        strategies.add(ThreeDSDataElementValidationStrategy.RECURRING_EXPIRY);
        //        strategies.add(ThreeDSDataElementValidationStrategy.RECURRING_FREQUENCY);
        //        strategies.add(ThreeDSDataElementValidationStrategy.SDK_ENC_DATA);
        //        strategies.add(ThreeDSDataElementValidationStrategy.TRANS_TYPE);
        //        strategies.add(ThreeDSDataElementValidationStrategy.ACCT_INFO);
        //        strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_REQ_AUTH_METHOD_IND);
        //        strategies.add(ThreeDSDataElementValidationStrategy.PAY_TOKEN_SOURCE);
        //        strategies.add(ThreeDSDataElementValidationStrategy.WHITE_LIST_STATUS_SOURCE);
        //
        //        List<ValidationResponse> arrResultActual = validateFields(strategies, objAReq);

        //        }
    }
    //
    //    protected void validateOptionalFields(AREQ request) throws ValidationException {
    //        AREQ objAReq = (AREQ) request;
    //        Set<ValidationStrategy> strategies = new LinkedHashSet<ValidationStrategy>();
    //
    //        //Optional Fields
    //
    // strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_REQUESTOR_AUTHENTICATION_INFO);
    //        strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_REQUESTOR_CHALLENGE_IND);
    //
    // strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_REQUESTOR_PRIOR_AUTHENTICATION_INFO);
    //        strategies.add(ThreeDSDataElementValidationStrategy.ADDRESS_MATCH);
    //        strategies.add(ThreeDSDataElementValidationStrategy.ACCT_ID);
    //        strategies.add(ThreeDSDataElementValidationStrategy.MERCHANT_RISK_INDICATOR);
    //        strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_REQUESTOR_DEC_MAX_TIME);
    //        strategies.add(ThreeDSDataElementValidationStrategy.WHITE_LIST_STATUS);
    //        strategies.add(ThreeDSDataElementValidationStrategy.THREEDS_REQUESTOR_DEC_REQ_IND);
    //
    //        List<ValidationResponse> arrResultActual = validateFields(strategies, objAReq);
    //        if(arrResultActual.size() > 0){
    //            ValidationResponse v = arrResultActual.get(0);
    //            throw new ValidationException(v.getThreeDSecureError(),
    // v.getDataElement().getFieldName());
    //        }
    //
    //    }
    //

}
