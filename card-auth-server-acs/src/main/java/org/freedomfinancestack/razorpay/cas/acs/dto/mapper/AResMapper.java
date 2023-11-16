package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.contract.enums.TransactionStatusReason;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * The {@code AResMapper} interface is responsible for mapping data from the {@link Transaction}
 * entity and the {@link AREQ} request to the Authentication Response {@link ARES} DTO.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Mapper(
        uses = {HelperMapper.class},
        componentModel = "spring",
        imports = {
            TransactionStatus.class,
            MessageCategory.class,
            Network.class,
            MessageType.class,
            Util.class,
            InternalConstants.class,
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
    @Mapping(
            target = "acsDecConInd",
            expression =
                    "java(transaction.getTransactionStatus().equals(TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED)"
                        + " ? \"Y\" : \"N\")")
    @Mapping(
            target = "acsOperatorID",
            expression = "java(getOperatorId(transaction, this.helperMapper.appConfiguration))")
    @Mapping(
            target = "acsReferenceNumber",
            expression = "java(this.helperMapper.appConfiguration.getAcs().getReferenceNumber())")
    @Mapping(target = "acsTransID", source = "transaction.id")
    @Mapping(target = "eci", source = "transaction.eci")
    @Mapping(
            target = "acsURL",
            expression =
                    "java(org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel())"
                        + " ? null :"
                        + " (Util.getAcsChallengeUrl(this.helperMapper.appConfiguration.getHostname(),"
                        + " transaction.getDeviceChannel())))")
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
    @Mapping(target = "sdkEphemPubKey", expression = "java(null)")
    @Mapping(target = "cardholderInfo", expression = "java(null)")
    @Mapping(
            target = "whiteListStatus",
            expression =
                    "java(!Util.isNullorBlank(areq.getThreeRIInd()) &&"
                        + " areq.getThreeRIInd().equals(InternalConstants.THREE_RI_IND_WHILE_LIST)"
                        + " ?  InternalConstants.NO : null)")
    @Mapping(
            target = "whiteListStatusSource",
            expression =
                    "java(!Util.isNullorBlank(areq.getThreeRIInd()) &&"
                        + " areq.getThreeRIInd().equals(InternalConstants.THREE_RI_IND_WHILE_LIST)"
                        + " ? InternalConstants.THREE_RI_WHILE_LIST_STATUS_SOURCE : null)")
    @Mapping(target = "messageType", expression = "java(MessageType.ARes.toString())")
    @Mapping(
            target = "acsRenderingType",
            expression =
                    "java(org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel())"
                        + " ? new org.freedomfinancestack.razorpay.cas.contract.enums."
                        + "ACSRenderingType(transaction.getTransactionSdkDetail().getAcsInterface(),"
                        + " transaction.getTransactionSdkDetail().getAcsUiType()) : null)")
    @Mapping(
            target = "acsSignedContent",
            expression = "java(transaction.getTransactionSdkDetail().getAcsSignedContent())")
    ARES toAres(AREQ areq, Transaction transaction);

    default String getTransStatusReason(AREQ areq, Transaction transaction) {
        String transStatusReason = "";
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
                            == transaction.getTransactionCardDetail().getNetworkCode()) {
                if (TransactionStatus.SUCCESS.equals(transaction.getTransactionStatus())) {
                    transaction.setTransactionStatusReason(
                            TransactionStatusReason.MEDIUM_CONFIDENCE.getCode());
                    transStatusReason = transaction.getTransactionStatusReason();
                } else {
                    transStatusReason = transaction.getTransactionStatusReason();
                }
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
}
