package org.freedomfinancestack.razorpay.cas.acs.exception;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.extensions.validation.exception.ValidationErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.validation.ThreeDSDataElement;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class ValidationErrorCodeMapper {
    private static final Map<ValidationErrorCode, ThreeDSecureErrorCode>
            VALIDATION_TO_THREEDSECURE_ERROR_MAP = new EnumMap<>(ValidationErrorCode.class);

    private static final Map<String, HashMap<ValidationErrorCode, ThreeDSecureErrorCode>>
            FIELD_TO_THREEDSECURE_ERROR_MAP = new HashMap<>();

    static {
        VALIDATION_TO_THREEDSECURE_ERROR_MAP.put(
                ValidationErrorCode.INVALID_FORMAT, ThreeDSecureErrorCode.INVALID_FORMAT);
        VALIDATION_TO_THREEDSECURE_ERROR_MAP.put(
                ValidationErrorCode.INVALID_FORMAT_LENGTH,
                ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH);
        VALIDATION_TO_THREEDSECURE_ERROR_MAP.put(
                ValidationErrorCode.INVALID_FORMAT_VALUE,
                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE);
        VALIDATION_TO_THREEDSECURE_ERROR_MAP.put(
                ValidationErrorCode.NOT_EQUAL, ThreeDSecureErrorCode.INVALID_FORMAT_VALUE);
        VALIDATION_TO_THREEDSECURE_ERROR_MAP.put(
                ValidationErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING);
    }

    static {

        // todo refactor this logic
        HashMap<ValidationErrorCode, ThreeDSecureErrorCode> messageVersionRules = new HashMap<>();
        messageVersionRules.put(
                ValidationErrorCode.INVALID_FORMAT_VALUE,
                ThreeDSecureErrorCode.MESSAGE_VERSION_NUMBER_NOT_SUPPORTED);
        FIELD_TO_THREEDSECURE_ERROR_MAP.put(
                ThreeDSDataElement.MESSAGE_VERSION.getFieldName(), messageVersionRules);

        HashMap<ValidationErrorCode, ThreeDSecureErrorCode> messageExtensionRules = new HashMap<>();
        messageExtensionRules.put(
                ValidationErrorCode.INVALID_FORMAT_VALUE,
                ThreeDSecureErrorCode.CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED);
        FIELD_TO_THREEDSECURE_ERROR_MAP.put(
                ThreeDSDataElement.MESSAGE_EXTENSION_CRITICAL_INDICATOR.getFieldName(),
                messageExtensionRules);

        HashMap<ValidationErrorCode, ThreeDSecureErrorCode> dsTransIdRules = new HashMap<>();
        messageExtensionRules.put(
                ValidationErrorCode.NOT_EQUAL, ThreeDSecureErrorCode.TRANSACTION_ID_NOT_RECOGNISED);
        FIELD_TO_THREEDSECURE_ERROR_MAP.put(
                ThreeDSDataElement.DS_TRANS_ID.getFieldName(), dsTransIdRules);

        HashMap<ValidationErrorCode, ThreeDSecureErrorCode> threedsServerTransIdRules =
                new HashMap<>();
        messageExtensionRules.put(
                ValidationErrorCode.NOT_EQUAL, ThreeDSecureErrorCode.TRANSACTION_ID_NOT_RECOGNISED);
        FIELD_TO_THREEDSECURE_ERROR_MAP.put(
                ThreeDSDataElement.THREEDS_SERVER_TRANSACTION_ID.getFieldName(),
                threedsServerTransIdRules);

        HashMap<ValidationErrorCode, ThreeDSecureErrorCode> acsTransIdRules = new HashMap<>();
        messageExtensionRules.put(
                ValidationErrorCode.NOT_EQUAL, ThreeDSecureErrorCode.TRANSACTION_ID_NOT_RECOGNISED);
        FIELD_TO_THREEDSECURE_ERROR_MAP.put(
                ThreeDSDataElement.ACS_TRANS_ID.getFieldName(), acsTransIdRules);
    }

    public static ThreeDSecureErrorCode mapValidationToThreeDSecure(
            ValidationErrorCode validationErrorCode, String fieldName) {
        if (FIELD_TO_THREEDSECURE_ERROR_MAP.containsKey(fieldName)
                && FIELD_TO_THREEDSECURE_ERROR_MAP
                        .get(fieldName)
                        .containsKey(validationErrorCode)) {
            return FIELD_TO_THREEDSECURE_ERROR_MAP.get(fieldName).get(validationErrorCode);
        }
        return VALIDATION_TO_THREEDSECURE_ERROR_MAP.getOrDefault(
                validationErrorCode, ThreeDSecureErrorCode.INVALID_FORMAT);
    }
}
