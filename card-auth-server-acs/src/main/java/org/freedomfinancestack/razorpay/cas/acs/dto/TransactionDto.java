package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

import lombok.Data;

@Data
public class TransactionDto {
    Transaction transaction;
}
