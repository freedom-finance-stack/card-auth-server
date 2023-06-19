package com.razorpay.threeds.dto.mapper;

import com.razorpay.threeds.configuration.AppConfiguration;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper
@Component
public class HelperMapper {

    @Autowired
    AppConfiguration appConfiguration;
    public static final String YES = "Y";
    public static final String NO = "N";

    public String asString(Boolean bool) {
        return null == bool ?
                null : (bool ?
                YES : NO
        );
    }
}
