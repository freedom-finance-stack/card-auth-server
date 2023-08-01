package org.ffs.razorpay.cas.acs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code CardDetailResponse} class represents a response object containing card details and
 * other information related to the card retrieval operation.
 *
 * <p>This class is annotated with Lombok annotations {@code @Data}, {@code @NoArgsConstructor},
 * {@code @AllArgsConstructor}, and {@code @Builder}, which automatically generate data methods,
 * constructors, and builder methods for this class.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDetailResponse {

    private CardDetailDto cardDetailDto;
    private boolean isSuccess;
    // these will be used in case of API fetch
    private String statusCode;
    private String statusDescription;
}
