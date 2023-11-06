package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.List;
import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.service.RenderingTypeService;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.model.RenderingTypeConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.RenderingTypeConfigPK;
import org.freedomfinancestack.razorpay.cas.dao.repository.RenderingTypeConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RenderingTypeServiceImpl implements RenderingTypeService {
    private final RenderingTypeConfigRepository renderingTypeConfigRepository;

    @Override
    public RenderingTypeConfig findById(RenderingTypeConfigPK renderingTypeConfigPK)
            throws ACSDataAccessException, DataNotFoundException {
        try {
            Optional<RenderingTypeConfig> renderingTypeConfig =
                    renderingTypeConfigRepository.findById(renderingTypeConfigPK);

            if (renderingTypeConfig.isPresent()) {
                return renderingTypeConfig.get();
            }
        } catch (DataAccessException e) {
            log.error(
                    "Error while fetching Rendering Type for Institution ID : "
                            + renderingTypeConfigPK.getInstitutionId()
                            + " and Card Range ID : "
                            + renderingTypeConfigPK.getCardRangeId());
            throw new ACSDataAccessException(InternalErrorCode.INSTITUTION_FETCH_EXCEPTION, e);
        }

        log.error(
                "Rendering Type not found for Institution ID : "
                        + renderingTypeConfigPK.getInstitutionId()
                        + " and Card Range ID : "
                        + renderingTypeConfigPK.getCardRangeId());
        throw new DataNotFoundException(
                ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                InternalErrorCode.RENDERING_TYPE_NOT_FOUND);
    }

    @Override
    public RenderingTypeConfig findRenderingType(
            String institutionId, String cardRangeId, String acsInterface)
            throws ACSDataAccessException, DataNotFoundException {
        try {
            List<RenderingTypeConfig> renderingTypeConfigList =
                    renderingTypeConfigRepository.findByInstitutionIdCardRangeIdAcsInterface(
                            institutionId, cardRangeId, acsInterface);
            if (!renderingTypeConfigList.isEmpty()) {
                return renderingTypeConfigList.get(0);
            }
        } catch (DataAccessException e) {
            log.error(
                    "Error while fetching Rendering Type for Institution ID : "
                            + institutionId
                            + " and Card Range ID : "
                            + cardRangeId);
            throw new ACSDataAccessException(InternalErrorCode.INSTITUTION_FETCH_EXCEPTION, e);
        }

        log.error(
                "Rendering Type not found for Institution ID : "
                        + institutionId
                        + " and Card Range ID : "
                        + cardRangeId);
        throw new DataNotFoundException(
                ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                InternalErrorCode.RENDERING_TYPE_NOT_FOUND);
    }

    @Override
    public RenderingTypeConfig findDefaultRenderingType(
            String institutionId, String cardRangeId, String acsInterface)
            throws ACSDataAccessException, DataNotFoundException {
        try {
            Optional<RenderingTypeConfig> renderingTypeConfigList =
                    renderingTypeConfigRepository
                            .findByInstitutionIdCardRangeIdAcsInterfaceDefaultRenderOption(
                                    institutionId, cardRangeId, acsInterface, "1");
            if (renderingTypeConfigList.isPresent()) {
                return renderingTypeConfigList.get();
            }
        } catch (DataAccessException e) {
            log.error(
                    "Error while fetching Rendering Type for Institution ID : "
                            + institutionId
                            + " and Card Range ID : "
                            + cardRangeId);
            throw new ACSDataAccessException(InternalErrorCode.INSTITUTION_FETCH_EXCEPTION, e);
        }

        log.error(
                "Rendering Type not found for Institution ID : "
                        + institutionId
                        + " and Card Range ID : "
                        + cardRangeId);
        throw new DataNotFoundException(
                ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                InternalErrorCode.RENDERING_TYPE_NOT_FOUND);
    }
}
