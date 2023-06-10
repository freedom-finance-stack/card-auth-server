package com.razorpay.acs.dao.annotation;


import org.hibernate.annotations.Where;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Where(clause = "deleted_at IS NULL")
@Inherited
public @interface SoftDeleteRead {
}
