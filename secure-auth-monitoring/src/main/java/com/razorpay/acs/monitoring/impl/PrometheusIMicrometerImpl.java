package com.razorpay.acs.monitoring.impl;

import com.razorpay.acs.monitoring.IMicrometer;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

public class PrometheusIMicrometerImpl implements IMicrometer {

    @Override
    public PrometheusMeterRegistry providesMeterRegistry() {

        final PrometheusMeterRegistry prometheusMeterRegistry =
                new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        prometheusMeterRegistry.config().commonTags("service", "secure-auth-acs");
        return prometheusMeterRegistry;
    }
}
