package com.razorpay.ffs.cas.acs.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.razorpay.ffs.cas.acs.module.configuration.AppConfiguration;

@Mapper
@Component
public class HelperMapper {

    @Autowired AppConfiguration appConfiguration;
    public static final String YES = "Y";
    public static final String NO = "N";

    public String asString(Boolean bool) {
        return null == bool ? null : (bool ? YES : NO);
    }
}
