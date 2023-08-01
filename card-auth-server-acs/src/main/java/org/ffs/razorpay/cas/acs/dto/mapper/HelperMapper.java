package org.ffs.razorpay.cas.acs.dto.mapper;

import org.ffs.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The {@code HelperMapper} class is a MapStruct component used as a helper for the {@link
 * AResMapper} interface. It provides utility methods to map specific attributes during the mapping
 * process.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Mapper
@Component
public class HelperMapper {

    /**
     * The {@code AppConfiguration} bean is auto-wired to this mapper to provide access to
     * application configuration properties.
     */
    @Autowired AppConfiguration appConfiguration;

    public static final String YES = "Y";
    public static final String NO = "N";

    /**
     * Converts a Boolean value to a String representation.
     *
     * @param bool The Boolean value to convert.
     * @return "Y" if the Boolean is true, "N" if it is false, or null if the input Boolean is null.
     */
    public String asString(Boolean bool) {
        return null == bool ? null : (bool ? YES : NO);
    }
}
