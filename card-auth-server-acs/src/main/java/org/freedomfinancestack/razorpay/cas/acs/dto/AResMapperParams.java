package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.AResMapper;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.freedomfinancestack.razorpay.cas.contract.enums.ACSRenderingType;

import lombok.Builder;
import lombok.Data;

/**
 * The {@code AResMapperParams} class is a data class representing parameters used by the {@link
 * AResMapper} to map data from {@link AREQ} to {@link ARES} objects.
 *
 * <p>This class is annotated with {@code @Builder} and {@code @Data} from the Lombok library to
 * automatically generate builder methods and data methods, such as getters, setters, equals,
 * hashCode, and toString methods.
 *
 * <p>Usage example:
 *
 * <pre>{@code
 * AResMapperParams params = AResMapperParams.builder()
 *                                    .acsUrl("https://example.com/acs")
 *                                    .build();
 * }</pre>
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Builder
@Data
public class AResMapperParams {
    String acsUrl;

    ACSRenderingType acsRenderingType;
}
