package org.freedomfinancestack.razorpay.cas.admin.controller;

import org.freedomfinancestack.razorpay.cas.admin.service.MetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("metaDataController")
@RequestMapping("/v1/admin/metadata")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MetaDataController {

    private final MetaDataService metaDataService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleGetMetaDataOperation(@RequestHeader HttpHeaders headers) {
        return metaDataService.processGetMetaDataOperation();
    }
}
