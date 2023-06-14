package com.razorpay.threeds.service.impl;

import com.razorpay.acs.dao.contract.enums.DeviceChannel;
import com.razorpay.acs.dao.enums.MessageCategory;
import com.razorpay.acs.dao.enums.MessageType;
import com.razorpay.acs.dao.enums.Phase;
import com.razorpay.acs.dao.enums.TransactionStatus;
import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.acs.dao.model.TransactionMerchant;
import com.razorpay.acs.dao.model.TransactionMessageTypeDetail;
import com.razorpay.acs.dao.model.TransactionReferenceDetail;
import com.razorpay.threeds.constant.InternalConstants;
import com.razorpay.threeds.constant.ThreeDSConstant;
import com.razorpay.threeds.context.RequestContextHolder;
import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.acs.dao.contract.ARES;
import com.razorpay.threeds.configuration.AppConfiguration;
import com.razorpay.threeds.service.AuthenticationService;
import com.razorpay.threeds.service.TransactionService;
import com.razorpay.threeds.validation.ValidationService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.razorpay.acs.dao.contract.constants.EMVCOConstant.appDeviceInfoAndroid;
import static com.razorpay.acs.dao.contract.constants.EMVCOConstant.appDeviceInfoIOS;

@Slf4j
@Service("authenticationServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceImpl implements AuthenticationService {

    @Qualifier(value = "aReqValidationServiceImpl")
    private final ValidationService<AREQ> validationService;
    private final TransactionService transactionService;
    @Override
    public ARES processAuthenticationRequest(@NonNull AREQ areq) {

        log.info("Starting processing for Authentication request: {}", RequestContextHolder.get().getRequestId());
        String id = UUID.randomUUID().toString();


        // validation 1

        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setInteractionCount(0);
        transaction.setPhase(Phase.AREQ);
        transaction.setTransactionStatus(TransactionStatus.CREATED);

        //Set default message version
        String acsMessageVersion = ThreeDSConstant.SUPPORTED_MESSAGE_VERSION[0];
        for(String availableVersion: ThreeDSConstant.SUPPORTED_MESSAGE_VERSION) {
            if(availableVersion.equals(areq.getMessageVersion())) {
                acsMessageVersion = areq.getMessageVersion();
            }
        }
        transaction.setMessageVersion(acsMessageVersion);

        String appDeviceInfo = "";
        if (areq.getDeviceChannel().equals(DeviceChannel.APP.getChannel())) {
            try {
                String deviceInfo = areq.getDeviceInfo();
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] decodedByte = decoder.decode(deviceInfo);
                String decodedString = new String(decodedByte);
                JSONObject json = new JSONObject(decodedString);
                JSONObject dd = json.getJSONObject("DD");
                appDeviceInfo = dd.getString("C001");
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }

        if(areq.getDeviceChannel().equals(DeviceChannel.APP.getChannel())) {
            if(appDeviceInfo.equals(appDeviceInfoAndroid)) {
                transaction.setDeviceName(InternalConstants.ANDROID);
            }else if(appDeviceInfo.equals(appDeviceInfoIOS)) {
                transaction.setDeviceName(InternalConstants.IOS);
            }
            transaction.setDeviceChannel(DeviceChannel.APP.getChannel());
        }else if(areq.getDeviceChannel().equals(DeviceChannel.BRW.getChannel())) {
            transaction.setDeviceName(InternalConstants.BROWSER);
            transaction.setDeviceChannel(DeviceChannel.BRW.getChannel());
        }

        transaction.setMessageCategory(MessageCategory.getMessageCategory(areq.getMessageCategory()));

        transaction.setInstitutionId("institutionId");
     //   TransactionReferenceDetail transactionReferenceDetail = new TransactionReferenceDetail("threedsServerTransactionId", "refNumber",  "dsid");
       // transactionReferenceDetail.setId(UUID.randomUUID().toString());
        //transaction.setTransactionReferenceDetail(transactionReferenceDetail);

        TransactionMerchant transactionMerchant = new TransactionMerchant();
        transactionMerchant.setMerchantName("merchantName");
        transactionMerchant.setAcquirerMerchantId("merchantId");
        transaction.setTransactionMerchant(transactionMerchant);


        List<TransactionMessageTypeDetail> transactionMessageTypes = new LinkedList<>();
        TransactionMessageTypeDetail  transactionMessageType = new TransactionMessageTypeDetail();
        transactionMessageType.setId(UUID.randomUUID().toString());
        transactionMessageType.setMessageType(MessageType.AReq);
        Timestamp receivedDateTime = new Timestamp(System.currentTimeMillis());
        transactionMessageType.setReceivedTimestamp(receivedDateTime);
        transactionMessageTypes.add(transactionMessageType);


        TransactionMessageTypeDetail  transactionMessageType2 = new TransactionMessageTypeDetail();
        transactionMessageType2.setId(UUID.randomUUID().toString());
        transactionMessageType2.setMessageType(MessageType.ARes);
        Timestamp receivedDateTime2 = new Timestamp(System.currentTimeMillis());
        transactionMessageType2.setReceivedTimestamp(receivedDateTime2);
        transactionMessageTypes.add(transactionMessageType2);
        
        transaction.setTransactionMessageTypeDetail(transactionMessageTypes);
        transactionService.createOrUpdate(transaction);
        Transaction dto = transactionService.findById(transaction.getId());
        List<TransactionMessageTypeDetail> ts = dto.getTransactionMessageTypeDetail();
      //  dto.getTransactionReferenceDetail().setDsTransactionId("dsTransactionId");
        transactionService.createOrUpdate(dto);
        transactionService.remove(transaction.getId());

        // Create other transaction dtos
        // check populateTransactionDetails
        // Store details that came under Areq

        // institutionInstrumentService.findByPan(pan); getAll institution entities, error if not found
        // Get Auth options for institution
        // Validate institution and range

        // Validate AREQ
       // validationService.validate(areq, TransectionDto, RangeDto);
        // riskBasedEngineService.determineChallenge(reqAReq, transaction);

//eci = eCommIndicatorService.generateECI(transaction, reqAReq);
//
//			//String eci = eCommIndicatorService.generateECI(transaction.getTransactionStatus(), transaction.getBrand().getLable());
//			LOGGER.trace(Utility.prefixTxnId(transaction.getTransactionId(),"Generated ECI : "+eci));
//			transaction.setEci(eci);
//
//
//			if (TransactionStatus.SUCCESS.equals(transaction.getTransactionStatus())
//					|| TransactionStatus.ATTEMPT.equals(transaction.getTransactionStatus())
//					|| TransactionStatus.INFORMATIONAL.equals(transaction.getTransactionStatus())) {
//				try {
//					LOGGER.trace(Utility.prefixTxnId(transaction.getTransactionId(),"Generating CAVV "));
//					HSMConfigPK configPK = new HSMConfigPK();
//					configPK.setInstitutionId(institution.getInstitutionId());
//					configPK.setNetworkId(instrumentDetail.getNetwork().getNetworkId());
//					LOGGER.trace(Utility.prefixTxnId(transaction.getTransactionId(),"HSM Config : "+configPK));
//					HSMConfig hsmConfig = hsmConfigService.findById(configPK);
//
//					String authValue = cavvGeneratorLocator.generateCAVV(reqAReq, hsmConfig, transaction, "nextval");
//
//					LOGGER.info(Utility.prefixTxnId(transaction.getTransactionId(), "Generated Auth Value ", authValue));
//					transaction.setAuthenticationValue(authValue);
//				} catch (ACSException e) {
//					e.printStackTrace();
//					LOGGER.error("Unable to generate Auth Value", e);
//				}
//			}


        // Store transaction details in db and get correct exception with details
        //generateARES
         // check every error and state being stored in db check for checked and unchecked exception... checked should return 200 with Ares
        // check transaction status handle

        return null;
    }
}
