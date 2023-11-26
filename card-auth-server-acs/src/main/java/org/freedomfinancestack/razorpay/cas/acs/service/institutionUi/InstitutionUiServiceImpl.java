package org.freedomfinancestack.razorpay.cas.acs.service.institutionUi;

import java.util.Objects;
import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.acs.dto.AppChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.service.InstitutionUiService;
import org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.impl.HtmlDeviceInterfaceServiceImpl;
import org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.impl.NativeDeviceInterfaceServiceImpl;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceInterface;
import org.freedomfinancestack.razorpay.cas.contract.enums.UIType;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfigPK;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.repository.InstitutionUiConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "institutionUiServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstitutionUiServiceImpl implements InstitutionUiService {

    private final ApplicationContext applicationContext;

    private final InstitutionUiConfigRepository institutionUiConfigRepository;

    @Override
    public void populateInstitutionUiConfig(
            AppChallengeFlowDto challengeFlowDto,
            Transaction transaction,
            AuthConfigDto authConfigDto)
            throws ACSDataAccessException {

        DeviceInterface deviceInterface =
                DeviceInterface.getDeviceInterface(
                        transaction.getTransactionSdkDetail().getAcsInterface());
        AuthType authType = AuthType.getAuthType(transaction.getAuthenticationType());
        UIType uiType = UIType.getUIType(transaction.getTransactionSdkDetail().getAcsUiType());
        Optional<InstitutionUiConfig> institutionUiConfig =
                institutionUiConfigRepository.findById(
                        new InstitutionUiConfigPK(
                                transaction.getInstitutionId(), authType, uiType));
        if (institutionUiConfig.isPresent()) {
            DeviceInterfaceService deviceInterfaceService =
                    getDeviceInterfaceService(Objects.requireNonNull(deviceInterface));
            deviceInterfaceService.populateInstitutionUiConfig(
                    transaction, challengeFlowDto, institutionUiConfig.get(), authConfigDto);
            return;
        }

        log.error(
                "Institution Ui Config not found for Institution ID : "
                        + transaction.getInstitutionId());
        throw new ACSDataAccessException(
                InternalErrorCode.INSTITUTION_UI_CONFIG_NOT_FOUND,
                "Institution Ui Config not found");
    }

    private DeviceInterfaceService getDeviceInterfaceService(
            @NonNull final DeviceInterface deviceInterface) {
        switch (deviceInterface) {
            case NATIVE:
                return applicationContext.getBean(NativeDeviceInterfaceServiceImpl.class);
            case HTML:
                return applicationContext.getBean(HtmlDeviceInterfaceServiceImpl.class);
        }
        return null;
    }
}
