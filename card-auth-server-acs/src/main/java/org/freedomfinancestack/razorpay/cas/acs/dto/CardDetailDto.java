package org.freedomfinancestack.razorpay.cas.acs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code CardDetailDto} class represents a Data Transfer Object (DTO) used for transferring
 * card details between different layers or components of the application.
 *
 * <p>This class is annotated with Lombok annotations {@code @Data}, {@code @NoArgsConstructor},
 * {@code @AllArgsConstructor}, and {@code @Builder}, which automatically generate data methods,
 * constructors, and builder methods for this class.
 *
 * <p>Usage example:
 *
 * <pre>{@code
 * CardDetailDto cardDetailDto = CardDetailDto.builder()
 *                                           .cardNumber("1234 5678 9876 5432")
 *                                           .cardExpiry("12/25")
 *                                           .name("John Doe")
 *                                           .mobileNumber("+1 123-456-7890")
 *                                           .emailId("johndoe@example.com")
 *                                           .blocked(false)
 *                                           .dob("1990-01-01")
 *                                           .institutionId("INST1234")
 *                                           .build();
 * }</pre>
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDetailDto {
    private String cardNumber;
    private String cardExpiry;
    private String name;
    private String mobileNumber;
    private String emailId;
    private boolean blocked;
    private String dob;
    private String institutionId;
}
