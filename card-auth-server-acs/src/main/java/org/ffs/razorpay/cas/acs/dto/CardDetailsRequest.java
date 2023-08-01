package org.ffs.razorpay.cas.acs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The {@code CardDetailsRequest} class represents a request object for retrieving card details.
 *
 * <p>This class is annotated with Lombok annotations {@code @Data} and {@code @AllArgsConstructor},
 * which automatically generate data methods and constructors for this class.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Data
@AllArgsConstructor
public class CardDetailsRequest {
    String institutionId;
    String cardNumber;
}
