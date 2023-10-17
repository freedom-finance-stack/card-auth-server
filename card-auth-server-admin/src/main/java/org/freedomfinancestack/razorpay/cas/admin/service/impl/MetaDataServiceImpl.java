package org.freedomfinancestack.razorpay.cas.admin.service.impl;

import org.freedomfinancestack.razorpay.cas.admin.dto.MetaDataResponse;
import org.freedomfinancestack.razorpay.cas.admin.exception.GlobalExceptionHandler;
import org.freedomfinancestack.razorpay.cas.admin.module.configuration.MetaDataConfiguration;
import org.freedomfinancestack.razorpay.cas.admin.service.MetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("MetaDataServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MetaDataServiceImpl implements MetaDataService {
    @Autowired MetaDataConfiguration metaDataConfiguration;

    private final GlobalExceptionHandler globalExceptionHandler;

    @Override
    public ResponseEntity<?> processGetMetaDataOperation() {
        try {
            MetaDataResponse metaDataResponse =
                    new MetaDataResponse(
                            metaDataConfiguration.getSupportedMessageVersions(),
                            metaDataConfiguration.getIsoCountryCode(),
                            metaDataConfiguration.getSupportedTimezone());
            return new ResponseEntity<>(metaDataResponse, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(" Message {}, Error string {}", ex.getMessage(), ex.toString());
            return globalExceptionHandler.handleException(ex);
        }
    }
}
