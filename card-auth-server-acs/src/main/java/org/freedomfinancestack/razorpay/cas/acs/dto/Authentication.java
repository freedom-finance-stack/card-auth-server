package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

import lombok.Data;

@Data
public class Authentication {
    Transaction transaction;
}
