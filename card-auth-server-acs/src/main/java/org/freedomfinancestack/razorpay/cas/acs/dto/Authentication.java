package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

import lombok.Data;

/**
 * The {@code Authentication} class is a data transfer object that contains all data needed to
 * perform authentication
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Data
public class Authentication {
    private Transaction transaction;
}
