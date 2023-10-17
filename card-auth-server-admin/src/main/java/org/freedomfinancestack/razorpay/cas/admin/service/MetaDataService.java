package org.freedomfinancestack.razorpay.cas.admin.service;

import org.springframework.http.ResponseEntity;

public interface MetaDataService {

    ResponseEntity<?> processGetMetaDataOperation();
}
