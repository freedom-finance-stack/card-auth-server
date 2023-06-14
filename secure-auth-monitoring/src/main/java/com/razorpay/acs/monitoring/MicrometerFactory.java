package com.razorpay.acs.monitoring;

import com.razorpay.acs.monitoring.config.MicrometerConfig;
import com.razorpay.acs.monitoring.impl.GraphiteIMicrometerImpl;
import com.razorpay.acs.monitoring.impl.PrometheusIMicrometerImpl;

import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerFactory {
    public static MeterRegistry getMeterRegistry() {
        IMicrometer iMicrometer = null;

        if (MicrometerConfig.MICROMETER_CONFIG.isPrometheusMicrometerEnabled()) {
            iMicrometer = new PrometheusIMicrometerImpl();
        }
        if (MicrometerConfig.MICROMETER_CONFIG.isGraphiteMicrometerEnabled()) {
            iMicrometer = new GraphiteIMicrometerImpl();
        }
        return iMicrometer == null ? null : iMicrometer.providesMeterRegistry();
    }
}
