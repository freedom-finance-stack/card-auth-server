package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
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
            DeviceChannel.class,
            TransactionStatus.class,
            MessageCategory.class,
            Network.class,
            MessageType.class,
            Util.class,
            DeviceInterface.class,
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
    CRES toCres(Transaction transaction);

    @Mapping(target = "acsTransID", source = "transaction.id")
    @Mapping(
            target = "threeDSServerTransID",
            source = "transaction.transactionReferenceDetail.threedsServerTransactionId")
    @Mapping(
            target = "challengeCompletionInd",
            expression = "java(getChallengeCompletionInd(transaction))")
    @Mapping(target = "messageVersion", source = "transaction.messageVersion")
    @Mapping(target = "messageType", expression = "java(MessageType.CRes.toString())")
    @Mapping(target = "acsCounterAtoS", source = "transaction.transactionSdkDetail.acsCounterAtoS")
    @Mapping(
            target = "acsUiType",
            expression = "java(transaction.getTransactionSdkDetail().getAcsUiType())")
    @Mapping(target = "sdkTransID", source = "transaction.transactionSdkDetail.sdkTransId")
    @Mapping(target = "acsHTML", source = "institutionUIParams.displayPage")
    @Mapping(target = "psImage", source = "institutionUIParams.psImage")
    @Mapping(target = "issuerImage", source = "institutionUIParams.issuerImage")
    @Mapping(target = "challengeInfoHeader", source = "institutionUIParams.challengeInfoHeader")
    @Mapping(target = "challengeInfoLabel", source = "institutionUIParams.challengeInfoLabel")
    @Mapping(target = "challengeInfoText", source = "institutionUIParams.challengeInfoText")
    @Mapping(target = "expandInfoLabel", source = "institutionUIParams.expandInfoLabel")
    @Mapping(target = "expandInfoText", source = "institutionUIParams.expandInfoText")
    @Mapping(
            target = "resendInformationLabel",
            source = "institutionUIParams.resendInformationLabel")
    @Mapping(
            target = "submitAuthenticationLabel",
            source = "institutionUIParams.submitAuthenticationLabel")
    @Mapping(target = "whyInfoLabel", source = "institutionUIParams.whyInfoLabel")
    @Mapping(target = "whyInfoText", source = "institutionUIParams.whyInfoText")
    @Mapping(target = "whitelistingInfoText", source = "institutionUIParams.whitelistingInfoText")
    // TODO
    // challengeSelectInfo
    // messageExtension
    // oobAppURL
    // oobContinueLabel
    // oobAppLabel

    CRES toAppCres(Transaction transaction, InstitutionUIParams institutionUIParams);

    default String getChallengeCompletionInd(Transaction transaction) {
        return Util.isChallengeCompleted(transaction)
                ? InternalConstants.YES
                : InternalConstants.NO;
    }
}
