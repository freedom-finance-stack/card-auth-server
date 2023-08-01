package org.ffs.razorpay.cas.acs.service;

import org.ffs.razorpay.cas.acs.dto.GenerateECIRequest;

/**
 * The {@code ECommIndicatorService} interface represents a service responsible for generating the
 * E-commerce Indicator (ECI) value. The ECI value indicates the outcome of an authentication
 * request and is used in 3D Secure transactions to convey the result of the authentication.
 *
 * @version 1.0.0
 * @since ACS 1.0.0
 * @author jaydeepRadadiya
 */
public interface ECommIndicatorService {

    /**
     * Generates the E-commerce Indicator (ECI) value based on the provided {@link
     * GenerateECIRequest}.
     *
     * @param generateECIRequest The {@link GenerateECIRequest} object containing the details
     *     required to generate the ECI value.
     * @return The E-commerce Indicator (ECI) value as a String.
     */
    String generateECI(GenerateECIRequest generateECIRequest);
}
