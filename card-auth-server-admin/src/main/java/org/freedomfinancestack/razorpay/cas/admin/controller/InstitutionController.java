package org.freedomfinancestack.razorpay.cas.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.freedomfinancestack.razorpay.cas.admin.dto.*;
import org.freedomfinancestack.razorpay.cas.admin.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public DefaultSuccessResponse handleCreateInstitutionOperation(
            @RequestBody @NonNull InstitutionRequestDto institutionRequestDto,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestHeader HttpHeaders headers) {
        return institutionService.processCreateInstitutionOperation(institutionRequestDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public GetInstitutionResponseDto handleGetInstitutionOperation(
            @RequestParam(name = "institution_id") String institutionId,
            @RequestParam(name = "fetch_meta", required = false) Boolean fetchMeta,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestHeader HttpHeaders headers) {
        GetInstitutionRequestDto getInstitutionRequestDto = new GetInstitutionRequestDto();
        getInstitutionRequestDto.setInstitution_id(institutionId);
        if (fetchMeta == null) {
            fetchMeta = false;
        }
        getInstitutionRequestDto.setFetch_meta(fetchMeta);
        return institutionService.processGetInstitutionOperation(getInstitutionRequestDto);
    }

    @PatchMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultSuccessResponse handlePatchInstitutionOperation(
            @RequestBody @NonNull InstitutionRequestDto institutionRequestDto,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestHeader HttpHeaders headers) {
        return institutionService.processPatchInstitutionOperation(institutionRequestDto);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultSuccessResponse handleDeleteInstitutionOperation(
            @RequestParam(name = "institution_id") String institutionId,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestHeader HttpHeaders headers) {
        return institutionService.processDeleteInstitutionOperation(institutionId);
    }
}
