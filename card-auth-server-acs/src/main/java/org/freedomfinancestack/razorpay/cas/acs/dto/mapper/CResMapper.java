package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
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
                DeviceChannel.class,
            TransactionStatus.class,
            MessageCategory.class,
            Network.class,
            MessageType.class,
            Util.class
        })
public interface CResMapper {

    /**
     * Creates a Challenge Response (CRes) from Transaction objects. it uses MapStruct, which
     * provides annotations to specify which
     *
     * @param transaction The {@link Transaction} object representing the transaction details.
     * @return The {@link CRES} object representing the Authentication Response message.
     */
    @Mapping(target = "acsTransID", source = "transaction.id")
    @Mapping(
            target = "threeDSServerTransID",
            source = "transaction.transactionReferenceDetail.threedsServerTransactionId")
    @Mapping(
            target = "challengeCompletionInd",
            expression = "java(getChallengeCompletionInd(transaction))")
    @Mapping(target = "transStatus", source = "transaction.transactionStatus.status")
    @Mapping(target = "messageVersion", source = "transaction.messageVersion")
    @Mapping(target = "messageType", expression = "java(MessageType.CRes.toString())")
    // todo add acsRenderingType, messageExtension, sdkTransactionId and WhiteListStatus for App
    // Based flow
    CRES toCres(Transaction transaction);

    @Mapping(target = "acsTransID", source = "transaction.id")
    @Mapping(
            target = "threeDSServerTransID",
            source = "transaction.transactionReferenceDetail.threedsServerTransactionId")
    @Mapping(
            target = "challengeCompletionInd",
            expression = "java(getChallengeCompletionInd(transaction))")
    @Mapping(target = "transStatus", source = "transaction.transactionStatus.status")
    @Mapping(target = "messageVersion", source = "transaction.messageVersion")
    @Mapping(target = "messageType", expression = "java(MessageType.CRes.toString())")
    @Mapping(target = "acsCounterAtoS", source = "transaction.transactionSdkDetail.acsCounterAtoS")
    @Mapping(target = "acsUiType", source = "transaction.transactionSdkDetail.acsUiType")
    @Mapping(target = "sdkTransID", source = "transaction.transactionSdkDetail.sdkTransId")
    // acsHTML ...
    // psImage
    // resendInformationLabel
    // submitAuthenticationLabel
    // whitelistingInfoText
    // whyInfoLabel whyInfoText

    // messageExtension
    // oobAppURL
    // oobContinueLabel
    // oobAppLabel

    CRES toAppCres(Transaction transaction);

    default String getChallengeCompletionInd(Transaction transaction) {
        return Util.isChallengeCompleted(transaction)
                ? InternalConstants.YES
                : InternalConstants.NO;
    }
}
