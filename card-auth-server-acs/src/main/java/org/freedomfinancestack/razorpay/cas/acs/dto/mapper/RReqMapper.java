package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
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
        imports = {TransactionStatus.class, MessageCategory.class, Network.class})
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
    @Mapping(target = "transStatusReason", source = "transaction.transactionStatusReason")
    @Mapping(target = "transStatus", source = "transaction.transactionStatus.status")
    @Mapping(target = "authenticationType", expression = "java(getAuthType(transaction))")
    @Mapping(target = "interactionCounter", expression = "java(getInteractionCounter(transaction))")
    @Mapping(target = "authenticationMethod", expression = "java(getAuthMethod(transaction))")
    @Mapping(target = "challengeCancel", source = "transaction.challengeCancelInd")
    @Mapping(
            target = "dsTransID",
            source = "transaction.transactionReferenceDetail.dsTransactionId")
    @Mapping(target = "messageVersion", source = "transaction.messageVersion")
    @Mapping(target = "messageCategory", source = "transaction.messageCategory")
    // todo add acsRenderingType, messageExtension, sdkTransactionId and WhiteListStatus for App
    // Based flow
    RREQ toRreq(Transaction transaction);

    default String getAuthType(Transaction transaction) {
        if (TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED.equals(
                transaction.getTransactionStatus())) {
            return "04";
        }
        return "02";
    }

    default String getInteractionCounter(Transaction transaction) {
        if (transaction.getInteractionCount() == null) {
            return "00";
        }
        return "0" + transaction.getInteractionCount();
    }

    default String getAuthMethod(Transaction transaction) {
        // todo add logic for OOB and other auth types
        return "02";
    }
}
