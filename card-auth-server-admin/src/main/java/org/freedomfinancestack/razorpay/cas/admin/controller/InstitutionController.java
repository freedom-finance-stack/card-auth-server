package org.freedomfinancestack.razorpay.cas.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.freedomfinancestack.razorpay.cas.admin.dto.InstitutionRequestDto;
import org.freedomfinancestack.razorpay.cas.admin.dto.InstitutionResponseDto;
import org.freedomfinancestack.razorpay.cas.admin.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link InstitutionController} is a REST controller responsible for handling all {@link
 * org.freedomfinancestack.razorpay.cas.dao.model.Institution} related CRUD Operations
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author ankitchoudhary2209
 */
@Slf4j
@RestController("institutionController")
@RequestMapping("/v1/admin/institution")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstitutionController {

    private final InstitutionService institutionService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public InstitutionResponseDto handleCreateInstitutionOperation(
            @RequestBody @NonNull InstitutionRequestDto institutionRequestDto,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestHeader HttpHeaders headers) {

        return institutionService.processCreateInstitutionOperation(institutionRequestDto);
    }
}
