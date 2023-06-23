package com.razorpay.threeds.module.configuration;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class ApplicationHealthIndicator implements HealthIndicator {

  @Override
  public Health health() {
    // perform some specific health check, otherwise this class is not needed
    //        int errorCode = check();
    //        if (errorCode != 0) {
    //            return Health.down().withDetail("Error Code", errorCode).build();
    //        }
    return Health.up().build();
  }
}
