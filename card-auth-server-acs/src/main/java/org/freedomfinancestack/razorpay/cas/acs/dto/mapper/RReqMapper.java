package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;


import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.enums.*;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * The {@code RResMapper} interface is responsible for mapping data from the {@link Transaction}
 * entity to the Result Response {@link RREQ}.
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
            WhitelistStatusSource.class,
        })
public interface RReqMapper {

    /**
     * Creates a Result Request (AREQ) from Transaction objects to communication to DS. it uses
     * MapStruct, which provides annotations to specify which
     *
     * @param transaction The {@link Transaction} object representing the transaction details.
     * @return The {@link RREQ} object representing the Authentication Response message.
     */
    @Mapping(target = "acsTransID", source = "transaction.id")
    @Mapping(target = "eci", source = "transaction.eci")
    @Mapping(
            target = "threeDSServerTransID",
            source = "transaction.transactionReferenceDetail.threedsServerTransactionId")
    @Mapping(target = "authenticationValue", source = "transaction.authValue")
    @Mapping(target = "authenticationType", expression = "java(getAuthType(transaction))")
    @Mapping(target = "transStatusReason", source = "transaction.transactionStatusReason")
    @Mapping(target = "transStatus", source = "transaction.transactionStatus.status")
    @Mapping(target = "interactionCounter", expression = "java(getInteractionCounter(transaction))")
    @Mapping(target = "authenticationMethod", expression = "java(getAuthMethod(transaction))")
    @Mapping(target = "challengeCancel", source = "transaction.challengeCancelInd")
    @Mapping(
            target = "dsTransID",
            source = "transaction.transactionReferenceDetail.dsTransactionId")
    @Mapping(target = "messageVersion", source = "transaction.messageVersion")
    @Mapping(
            target = "messageCategory",
            expression = "java(transaction.getMessageCategory().getCategory())")
    @Mapping(target = "messageType", expression = "java(MessageType.RReq.toString())")
    @Mapping(
            target = "sdkTransID",
            expression =
                    "java(DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel()) ?"
                            + " transaction.getTransactionSdkDetail().getSdkTransId() : null)")
    @Mapping(
            target = "acsRenderingType",
            expression =
                    "java((DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel()) &&"
                        + " transaction.getTransactionSdkDetail().getAcsUiTemplate() != null )? new"
                        + " ACSRenderingType(transaction.getTransactionSdkDetail().getAcsInterface(),"
                        + " transaction.getTransactionSdkDetail().getAcsUiTemplate()) : null)")
    @Mapping(target = "whiteListStatus", expression = "java(getWhiteListStatus(transaction))")
    @Mapping(
            target = "whiteListStatusSource",
            expression =
                    "java(getWhiteListStatus(transaction) != null ?"
                            + " WhitelistStatusSource.ACS.getValue() : null)")
    RREQ toRreq(Transaction transaction);

    default String getAuthType(Transaction transaction) {
        if (transaction == null || transaction.getAuthenticationType() == null) {
            return "";
        }
        if (transaction.getAuthenticationType() < 10) {
            return "0" + transaction.getAuthenticationType();
        }
        return String.valueOf(transaction.getAuthenticationType());
    }

    default String getInteractionCounter(Transaction transaction) {
        if (transaction.getInteractionCount() < 10) {
            return "0" + transaction.getInteractionCount();
        }
        return String.valueOf(transaction.getInteractionCount());
    }

    default String getAuthMethod(Transaction transaction) {
        // todo add logic for OOB and other auth types
        return "02";
    }

    default String getWhiteListStatus(Transaction transaction) {
        if (!Util.isNullorBlank(transaction.getTransactionSdkDetail().getWhitelistingDataEntry())
                && transaction
                        .getTransactionSdkDetail()
                        .getWhitelistingDataEntry()
                        .equals(InternalConstants.YES)) {
            return InternalConstants.YES;
        } else if (!Util.isNullorBlank(
                        transaction.getTransactionSdkDetail().getWhitelistingDataEntry())
                && transaction
                        .getTransactionSdkDetail()
                        .getWhitelistingDataEntry()
                        .equals(InternalConstants.NO)) {
            return InternalConstants.PADDED_SYMBOL_R;
        }
        return null;
    }
}
