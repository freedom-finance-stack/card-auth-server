package org.freedomfinancestack.razorpay.cas.dao.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum FeatureEntityType {
    CARD_RANGE(1),
    CARD_RANGE_GROUP(2),
    INSTITUTION(3);

    private final int preference;

    FeatureEntityType(int preference) {
        this.preference = preference;
    }

    public int getPreference() {
        return preference;
    }

    public static List<FeatureEntityType> getOrderedByPreference() {
        return Arrays.stream(values())
                .sorted(Comparator.comparingInt(FeatureEntityType::getPreference))
                .collect(Collectors.toList());
    }
}
