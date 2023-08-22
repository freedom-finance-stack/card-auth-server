package org.freedomfinancestack.razorpay.cas.dao.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.contract.utils.Util;
import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureEntityType;
import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureName;
import org.freedomfinancestack.razorpay.cas.dao.model.*;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends BaseRepository<Feature, String> {

    List<FeatureEntityType> preference =
            new ArrayList<>(
                    Arrays.asList(
                            FeatureEntityType.CARD_RANGE,
                            FeatureEntityType.CARD_RANGE_GROUP,
                            FeatureEntityType.INSTITUTION));

    Feature findFeatureByNameAndEntityTypeAndEntityIdAndActive(
            FeatureName name, FeatureEntityType entityType, String entityID, Boolean active);

    default IFeature findFeatureByIds(
            FeatureName name, Map<FeatureEntityType, String> entityIdsByType) {
        for (FeatureEntityType entityType : preference) {
            if (entityIdsByType.containsKey(entityType)) {
                Feature feature =
                        findFeatureByNameAndEntityTypeAndEntityIdAndActive(
                                name, entityType, entityIdsByType.get(entityType), true);
                if (feature != null) {
                    switch (feature.getName()) {
                        case CHALLENGE_AUTH_TYPE:
                            return Util.gson.fromJson(
                                    feature.getProperties(), ChallengeAuthTypeConfig.class);
                        case OTP:
                            return Util.gson.fromJson(feature.getProperties(), OtpConfig.class);
                        case CHALLENGE_ATTEMPT:
                            return Util.gson.fromJson(
                                    feature.getProperties(), ChallengeAttemptConfig.class);
                        case PASSWORD:
                            return Util.gson.fromJson(
                                    feature.getProperties(), PasswordConfig.class);
                        default:
                            return null;
                    }
                }
            }
        }
        return null;
    }
}
