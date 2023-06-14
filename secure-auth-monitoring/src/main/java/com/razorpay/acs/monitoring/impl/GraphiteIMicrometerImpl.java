package com.razorpay.acs.monitoring.impl;

import com.razorpay.acs.monitoring.IMicrometer;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;
import io.micrometer.graphite.GraphiteConfig;
import io.micrometer.graphite.GraphiteMeterRegistry;

public class GraphiteIMicrometerImpl implements IMicrometer {

    @Override
    public GraphiteMeterRegistry providesMeterRegistry() {

        GraphiteMeterRegistry graphiteMeterRegistry =
                new GraphiteMeterRegistry(
                        GraphiteConfig.DEFAULT,
                        Clock.SYSTEM,
                        (id, convention) ->
                                "prefix."
                                        + HierarchicalNameMapper.DEFAULT.toHierarchicalName(
                                                id, convention));
        return graphiteMeterRegistry;
    }
}
