package com.razorpay.threeds.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class AppConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "app")
  public AppProperties App() {
    return new AppProperties();
  }
}
