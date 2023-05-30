package com.razorpay.threeds.module.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ConfigProvider {

    private Configuration config;

    public ConfigProvider() {
        Parameters params = new Parameters();

        // todo - handle environment specific properties
        String configFile = "config.properties";

        File propertiesFile = new File(configFile);
        try {
            FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                    new FileBasedConfigurationBuilder<FileBasedConfiguration>(
                                    PropertiesConfiguration.class)
                            .configure(params.fileBased().setFile(propertiesFile));

            config = builder.getConfiguration();
        } catch (Exception ex) {
            // todo - handle exception
            ex.printStackTrace();
        }
    }

    public int getServerPort() {
        return config.getInt("server.port");
    }
}
