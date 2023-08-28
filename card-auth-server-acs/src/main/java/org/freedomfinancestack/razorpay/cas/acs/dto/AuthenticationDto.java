package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) class that encapsulates the information required for authentication.
 * Contains details about the transaction, authentication configuration, and authentication value.
 *
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Getter
@Setter
@Builder
public class AuthenticationDto {
    /**
     * The transaction for which authentication is being performed.
     *
     * @param transaction The transaction details.
     * @since 1.0.0
     */
    private Transaction transaction;

    /**
     * The authentication configuration settings specific to this authentication process.
     *
     * @param authConfigDto The authentication configuration.
     * @since 1.0.0
     */
    private AuthConfigDto authConfigDto;

    /**
     * The value used for authentication, such as OTP, password, etc.
     *
     * @param authValue The authentication value.
     * @since 1.0.0
     */
    private String authValue;
}
