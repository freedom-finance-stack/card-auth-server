package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.RouteConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.ThreeDSConstant;
import org.freedomfinancestack.razorpay.cas.acs.dto.AResMapperParams;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.freedomfinancestack.razorpay.cas.contract.enums.*;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * The {@code AResMapper} interface is responsible for mapping data from the {@link Transaction}
 * entity and the {@link AREQ} request to the Authentication Response {@link ARES} DTO.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Mapper(
        uses = {HelperMapper.class},
        componentModel = "spring",
        imports = {
            TransactionStatus.class,
            MessageCategory.class,
            Network.class,
            MessageType.class,
            DeviceChannel.class,
            ACSRenderingType.class,
            Util.class,
            InternalConstants.class,
            ThreeDSConstant.class,
            RouteConstants.class
        })
public interface AResMapper {

    /**
     * Converts the Authentication Request (AREQ) and Transaction objects to an Authentication
     * Response (ARES) object. it uses MapStruct, which provides annotations to specify which
     * attribute to map where, with processing if needed.
     *
     * @param areq The {@link AREQ} object representing the Authentication Request message received
     *     from the 3DS Server.
     * @param transaction The {@link Transaction} object representing the transaction details.
     * @return The {@link ARES} object representing the Authentication Response message.
     */
    @Mapping(target = "acsChallengeMandated", source = "transaction.challengeMandated")
    @Mapping(target = "acsDecConInd", expression = "java(getAcsDecConInd(transaction))")
    @Mapping(
            target = "acsOperatorID",
            expression =
                    "java(getOperatorId(transaction, this.helperMapper.getAppConfiguration()))")
    @Mapping(
            target = "acsReferenceNumber",
            expression =
                    "java(this.helperMapper.getAppConfiguration().getAcs().getReferenceNumber())")
    @Mapping(target = "acsTransID", source = "transaction.id")
    @Mapping(target = "eci", source = "transaction.eci")
    @Mapping(
            target = "acsURL",
            expression =
                    "java(DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel()) ?"
                        + " null :"
                        + " (RouteConstants.getAcsChallengeUrl(this.helperMapper.getAppConfiguration().getHostname(),transaction.getDeviceChannel())))")
    @Mapping(
            target = "transStatus",
            expression = "java(transaction.getTransactionStatus().getStatus())")
    @Mapping(
            target = "transStatusReason",
            expression = "java(getTransStatusReason(areq, transaction))")
    @Mapping(target = "authenticationValue", source = "transaction.authValue")
    @Mapping(target = "authenticationType", expression = "java(getAuthType(transaction))")
    @Mapping(target = "threeDSServerTransID", source = "areq.threeDSServerTransID")
    @Mapping(target = "dsReferenceNumber", source = "areq.dsReferenceNumber")
    @Mapping(target = "dsTransID", source = "areq.dsTransID")
    @Mapping(target = "sdkTransID", source = "areq.sdkTransID")
    @Mapping(target = "messageVersion", source = "areq.messageVersion")
    @Mapping(target = "broadInfo", expression = "java(null)")
    @Mapping(target = "whiteListStatus", expression = "java(getWhiteListStatus(areq))")
    @Mapping(target = "whiteListStatusSource", expression = "java(getWhiteListStatusSource(areq))")
    @Mapping(target = "messageType", expression = "java(MessageType.ARes.toString())")
    @Mapping(target = "acsRenderingType", expression = "java(getAcsRenderingType(transaction))")
    @Mapping(target = "acsSignedContent", source = "aResMapperParams.acsSignedContent")
    @Mapping(target = "cardholderInfo", expression = "java(getCardholderInfo(transaction))")
    ARES toAres(AREQ areq, Transaction transaction, AResMapperParams aResMapperParams);

    default String getWhiteListStatus(AREQ areq) {
        return !Util.isNullorBlank(areq.getThreeRIInd())
                        && areq.getThreeRIInd().equals(InternalConstants.THREE_RI_IND_WHILE_LIST)
                ? InternalConstants.NO
                : null;
    }

    default String getWhiteListStatusSource(AREQ areq) {
        return !Util.isNullorBlank(areq.getThreeRIInd())
                        && areq.getThreeRIInd().equals(InternalConstants.THREE_RI_IND_WHILE_LIST)
                ? InternalConstants.THREE_RI_WHILE_LIST_STATUS_SOURCE
                : null;
    }

    default ACSRenderingType getAcsRenderingType(Transaction transaction) {
        if (DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel())
                && transaction.getTransactionSdkDetail().getAcsUiTemplate() != null) {
            return new ACSRenderingType(
                    transaction.getTransactionSdkDetail().getAcsInterface(),
                    transaction.getTransactionSdkDetail().getAcsUiTemplate());
        }
        return null;
    }

    default String getCardholderInfo(Transaction transaction) {
        return transaction.getMessageVersion().equals(ThreeDSConstant.MESSAGE_VERSION_2_2_0)
                        && transaction
                                .getTransactionStatus()
                                .equals(TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED)
                ? "Additional authentication is needed for this transaction, please  contact"
                        + " (Issuer Name) at xxx-xxx-xxxx."
                : null;
    }

    default String getTransStatusReason(AREQ areq, Transaction transaction) {
        String transStatusReason;
        if (MessageCategory.PA.getCategory().equals(areq.getMessageCategory())
                && (TransactionStatus.FAILED.equals(transaction.getTransactionStatus())
                        || TransactionStatus.UNABLE_TO_AUTHENTICATE.equals(
                                transaction.getTransactionStatus())
                        || TransactionStatus.REJECTED.equals(transaction.getTransactionStatus()))) {
            transStatusReason = transaction.getTransactionStatusReason();
        } else {
            // For 02-NPA, Conditional as defined by the DS.
            if (transaction.getTransactionCardDetail() != null
                    && transaction.getTransactionCardDetail().getNetworkCode() != null
                    && Network.AMEX.getValue()
                            == transaction.getTransactionCardDetail().getNetworkCode()
                    && TransactionStatus.SUCCESS.equals(transaction.getTransactionStatus())) {
                transStatusReason = TransactionStatusReason.MEDIUM_CONFIDENCE.getCode();
            } else {
                transStatusReason = transaction.getTransactionStatusReason();
            }
        }
        return transStatusReason;
    }

    default String getOperatorId(Transaction transaction, AppConfiguration appConfiguration) {
        String operatorId = "";
        if (transaction.getTransactionCardDetail() == null
                || transaction.getTransactionCardDetail().getNetworkCode() == null) {
            operatorId = "DEFAULT";
        } else if (Network.VISA.getValue()
                == transaction.getTransactionCardDetail().getNetworkCode().intValue()) {
            operatorId = appConfiguration.getAcs().getOperatorId().getVisa();
        } else if (Network.MASTERCARD.getValue()
                == transaction.getTransactionCardDetail().getNetworkCode().intValue()) {
            operatorId = appConfiguration.getAcs().getOperatorId().getMastercard();
        } else if (Network.AMEX.getValue()
                == transaction.getTransactionCardDetail().getNetworkCode().intValue()) {
            operatorId = appConfiguration.getAcs().getOperatorId().getAmex();
        }
        return operatorId;
    }

    default String getAuthType(Transaction transaction) {
        if (transaction == null || transaction.getAuthenticationType() == null) {
            return "02"; // default Auth type to 2, this is needed as required by test portal
        }
        if (transaction.getAuthenticationType() < 10) {
            return "0" + transaction.getAuthenticationType();
        }
        return String.valueOf(transaction.getAuthenticationType());
    }

    default String getAcsDecConInd(Transaction transaction) {
        if (transaction.getMessageVersion().equals(ThreeDSConstant.MESSAGE_VERSION_2_2_0)) {
            return transaction
                            .getTransactionStatus()
                            .equals(TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED)
                    ? "Y"
                    : "N";
        }
        return null;
    }
}
