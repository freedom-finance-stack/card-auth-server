package com.razorpay.threeds.service.impl;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.acs.dao.contract.CREQ;
import com.razorpay.acs.dao.contract.ThreeDSObject;
import com.razorpay.acs.dao.contract.enums.DeviceChannel;
import com.razorpay.acs.dao.contract.enums.MessageCategory;
import com.razorpay.acs.dao.enums.MessageType;
import com.razorpay.acs.dao.enums.Phase;
import com.razorpay.acs.dao.enums.TransactionStatus;
import com.razorpay.acs.dao.model.*;
import com.razorpay.acs.dao.repository.TransactionMessageTypeDetailRepository;
import com.razorpay.acs.dao.repository.TransactionRepository;
import com.razorpay.threeds.constant.InternalConstants;
import com.razorpay.threeds.constant.ThreeDSConstant;
import com.razorpay.threeds.service.TransactionMessageTypeService;
import com.razorpay.threeds.service.TransactionService;
import com.razorpay.threeds.utils.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

import static com.razorpay.acs.dao.contract.constants.EMVCOConstant.appDeviceInfoAndroid;
import static com.razorpay.acs.dao.contract.constants.EMVCOConstant.appDeviceInfoIOS;
import static com.razorpay.acs.dao.contract.utils.Util.DATE_FORMAT_YYYYMMDDHHMMSS;
import static com.razorpay.threeds.utils.Util.getTimeStampFromString;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TransactionMessageTypeServiceImpl implements TransactionMessageTypeService {

    private final TransactionMessageTypeDetailRepository transactionMessageTypeDetailRepository;
    public TransactionMessageTypeDetail create(AREQ areq, String transactionId) {
        TransactionMessageTypeDetail transactionMessageTypeDetail = new TransactionMessageTypeDetail(Util.toJson(areq), MessageType.AReq);
        transactionMessageTypeDetail.setId(Util.generateUUID());
        transactionMessageTypeDetail.setTransactionId(transactionId);
        transactionMessageTypeDetail.setReceivedTimestamp(new Timestamp(System.currentTimeMillis()));
        return transactionMessageTypeDetail;
    }

    public TransactionMessageTypeDetail create(CREQ creq, String transactionId) {
        TransactionMessageTypeDetail transactionMessageTypeDetail = new TransactionMessageTypeDetail(Util.toJson(creq), MessageType.CReq);
        transactionMessageTypeDetail.setId(Util.generateUUID());
        transactionMessageTypeDetail.setTransactionId(transactionId);
        transactionMessageTypeDetail.setReceivedTimestamp(new Timestamp(System.currentTimeMillis()));
        return transactionMessageTypeDetail;
    }

    public void save(TransactionMessageTypeDetail transactionMessageTypeDetail){
            if ( transactionMessageTypeDetail != null ) {
                transactionMessageTypeDetailRepository.save(transactionMessageTypeDetail);
            }
    }

    public TransactionMessageTypeDetail createAndSave(ThreeDSObject threeDSObject, String transactionId) {
        TransactionMessageTypeDetail transactionMessageTypeDetail = null;
        if (threeDSObject instanceof AREQ) {
            transactionMessageTypeDetail =  create((AREQ) threeDSObject, transactionId);
        } else if (threeDSObject instanceof CREQ) {
            transactionMessageTypeDetail = create((CREQ) threeDSObject, transactionId);
        }
        this.save(transactionMessageTypeDetail);
        return transactionMessageTypeDetail;
    }

}
