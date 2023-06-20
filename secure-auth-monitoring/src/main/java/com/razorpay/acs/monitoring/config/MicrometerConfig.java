package com.razorpay.acs.monitoring.config;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;

public enum MicrometerConfig {
  MICROMETER_CONFIG;

  private Configuration config;

  MicrometerConfig() {
    Parameters params = new Parameters();

    String configFile = "micrometer-config.yaml";
    File file = new File(configFile);

    try {
      FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
          new FileBasedConfigurationBuilder<FileBasedConfiguration>(YAMLConfiguration.class)
              .configure(params.fileBased().setFile(file));

      config = builder.getConfiguration();
    } catch (Exception ex) {
      // todo - handle exception
      ex.printStackTrace();
    }
  }

  public boolean isPrometheusMicrometerEnabled() {
    return config.getBoolean("micrometer.supportedSystem.prometheus.enabled");
  }

  public boolean isGraphiteMicrometerEnabled() {
    return config.getBoolean("micrometer.supportedSystem.graphite.enabled");
  }

  public String getGraphiteMicrometerHost() {
    return config.getString("micrometer.supportedSystem.graphite.serverUrl");
  }

  public String getGraphiteMicrometerPort() {
    return config.getString("micrometer.supportedSystem.graphite.serverPort");
  }
}
