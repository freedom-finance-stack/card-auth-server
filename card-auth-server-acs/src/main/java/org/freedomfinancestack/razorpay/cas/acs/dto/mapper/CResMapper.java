package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.freedomfinancestack.razorpay.cas.acs.service.ChallengeRequestService;
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
    @Mapping(
            target = "sdkTransID",
            expression =
                    "java(DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel()) ?"
                            + " transaction.getTransactionSdkDetail().getSdkTransId() : null)")
    @Mapping(
            target = "acsCounterAtoS",
            expression =
                    "java(DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel()) ?"
                            + " transaction.getTransactionSdkDetail().getAcsCounterAtoS() : null)")
    @Mapping(target = "psImage", expression = "java(null)")
    @Mapping(target = "issuerImage", expression = "java(null)")
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
    @Mapping(
            target = "psImage",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ? institutionUIParams.getPsImage() :"
                            + " null)")
    @Mapping(
            target = "issuerImage",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ? institutionUIParams.getIssuerImage()"
                            + " : null)")
    @Mapping(
            target = "challengeInfoHeader",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ?"
                            + " institutionUIParams.getChallengeInfoHeader() : null)")
    @Mapping(
            target = "challengeInfoLabel",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ?"
                            + " institutionUIParams.getChallengeInfoLabel() : null)")
    @Mapping(
            target = "challengeInfoText",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ?"
                            + " institutionUIParams.getChallengeInfoText() : null)")
    @Mapping(
            target = "expandInfoLabel",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ?"
                            + " institutionUIParams.getExpandInfoLabel() : null)")
    @Mapping(
            target = "expandInfoText",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ?"
                            + " institutionUIParams.getExpandInfoText() : null)")
    @Mapping(
            target = "resendInformationLabel",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ?"
                            + " institutionUIParams.getResendInformationLabel() : null)")
    @Mapping(
            target = "submitAuthenticationLabel",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ?"
                            + " institutionUIParams.getSubmitAuthenticationLabel() : null)")
    @Mapping(
            target = "whyInfoLabel",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ? institutionUIParams.getWhyInfoLabel()"
                            + " : null)")
    @Mapping(
            target = "whyInfoText",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ? institutionUIParams.getWhyInfoText()"
                            + " : null)")
    @Mapping(
            target = "whitelistingInfoText",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ?"
                            + " institutionUIParams.getWhitelistingInfoText() : null)")
    @Mapping(
            target = "oobContinueLabel",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ?"
                            + " institutionUIParams.getOobContinueLabel() : null)")
    @Mapping(
            target = "challengeSelectInfo",
            expression =
                    "java(isAppBasedNativeFlow(transaction) ?"
                            + " institutionUIParams.getChallengeSelectInfo() : null)")
    // messageExtension
    CRES toAppCres(Transaction transaction, InstitutionUIParams institutionUIParams);

    default String getChallengeCompletionInd(Transaction transaction) {
        return ChallengeRequestService.isChallengeCompleted(transaction)
                ? InternalConstants.YES
                : InternalConstants.NO;
    }

    default boolean isAppBasedNativeFlow(Transaction transaction) {
        return transaction.getDeviceChannel().equals(DeviceChannel.APP.getChannel())
                && transaction
                        .getTransactionSdkDetail()
                        .getAcsInterface()
                        .equals(DeviceInterface.NATIVE.getValue());
    }
}
