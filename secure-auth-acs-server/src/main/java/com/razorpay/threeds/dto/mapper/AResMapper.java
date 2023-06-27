package com.razorpay.threeds.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.acs.dao.contract.ARES;
import com.razorpay.acs.dao.contract.enums.MessageCategory;
import com.razorpay.acs.dao.enums.Network;
import com.razorpay.acs.dao.enums.TransactionStatus;
import com.razorpay.acs.dao.enums.TransactionStatusReason;
import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.dto.AResMapperParams;
import com.razorpay.threeds.module.configuration.AppConfiguration;

@Mapper(
    uses = {HelperMapper.class, AppConfiguration.class},
    componentModel = "spring",
    imports = {TransactionStatus.class, MessageCategory.class, Network.class})
public interface AResMapper {

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
      expression =
          "java(this.helperMapper.appConfiguration.getApp().getAcs().getReferenceNumber())")
  @Mapping(target = "acsTransID", source = "transaction.id")
  @Mapping(target = "eci", source = "transaction.eci")
  @Mapping(target = "acsURL", source = "aResMapperParams.acsUrl")
  @Mapping(
      target = "transStatus",
      expression = "java(transaction.getTransactionStatus().getStatus())")
  @Mapping(
      target = "transStatusReason",
      expression = "java(getTransStatusReason(areq, transaction))")

  // todo   @Mapping(target = "authenticationMethod", expression =
  // "java(getAuthType(transaction))")
  @Mapping(target = "authenticationValue", source = "transaction.authValue")
  @Mapping(target = "threeDSServerTransID", source = "areq.threeDSServerTransID")
  @Mapping(target = "dsReferenceNumber", source = "areq.dsReferenceNumber")
  @Mapping(target = "dsTransID", source = "areq.dsTransID")
  @Mapping(target = "sdkTransID", source = "areq.sdkTransID")
  @Mapping(target = "messageVersion", source = "areq.messageVersion")
  @Mapping(target = "broadInfo", expression = "java(null)")
  @Mapping(target = "sdkEphemPubKey", expression = "java(null)")
  @Mapping(target = "cardholderInfo", expression = "java(null)")

  // todo    @Mapping acsRenderingType, AcsSignedContent  for appbased
  ARES toAres(AREQ areq, Transaction transaction, AResMapperParams aResMapperParams);

  default String getTransStatusReason(AREQ areq, Transaction transaction) {
    String transStatusReason = "";
    if (MessageCategory.PA.getCategory().equals(areq.getMessageCategory())
        && (TransactionStatus.FAILED.equals(transaction.getTransactionStatus())
            || TransactionStatus.UNABLE_TO_AUTHENTICATE.equals(transaction.getTransactionStatus())
            || TransactionStatus.REJECTED.equals(transaction.getTransactionStatus()))) {
      transStatusReason = transaction.getTransactionStatusReason();
    } else {
      // For 02-NPA, Conditional as defined by the DS.
      if (Network.AMEX.getValue() == transaction.getTransactionCardDetail().getNetworkCode()) {
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

  default String getAuthType(Transaction transaction) {
    if (TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED.equals(transaction.getTransactionStatus())
        || TransactionStatus.CHALLENGE_REQUIRED.equals(transaction.getTransactionStatus())) {
      return "02"; // hard coded to Dynamic
    }
    return "";
  }

  default String getOperatorId(Transaction transaction, AppConfiguration appConfiguration) {

    String operatorId = "";
    if (Network.VISA.getValue()
        == transaction.getTransactionCardDetail().getNetworkCode().intValue()) {
      operatorId = appConfiguration.getApp().getAcs().getOperatorId().getVisa();
    } else if (Network.MASTERCARD.getValue()
        == transaction.getTransactionCardDetail().getNetworkCode().intValue()) {
      operatorId = appConfiguration.getApp().getAcs().getOperatorId().getMastercard();
    } else if (Network.AMEX.getValue()
        == transaction.getTransactionCardDetail().getNetworkCode().intValue()) {
      operatorId = appConfiguration.getApp().getAcs().getOperatorId().getAmex();
    }
    return operatorId;
  }
}
