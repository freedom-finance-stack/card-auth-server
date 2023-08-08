package org.freedomfinancestack.extensions.hsm.command.module;

import org.freedomfinancestack.extensions.hsm.command.enums.HSMCommandType;
import org.freedomfinancestack.extensions.hsm.command.factory.HSMCommandFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan(value = {"org.freedomfinancestack.extensions"})
public class HSMCommandModule {

    @Value("${hsm.enabled_gateway}")
    private String enabledHSMGateway;

    @Bean("hsmCommandFactory")
    public FactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(HSMCommandFactory.class);
        return factoryBean;
    }

    @Bean("hsmCommandTypeEnabled")
    public HSMCommandType hsmCommandTypeEnabled() {
        HSMCommandType hsmCommandTypeEnabled = HSMCommandType.fromStringType(enabledHSMGateway);
        log.info("hsmCommandTypeEnabled() hsmCommandTypeEnabled: {}", hsmCommandTypeEnabled);
        return hsmCommandTypeEnabled;
    }
}
