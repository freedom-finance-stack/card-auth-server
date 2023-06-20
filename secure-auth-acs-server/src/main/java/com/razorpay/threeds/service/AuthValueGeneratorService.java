package com.razorpay.threeds.service;

import com.razorpay.acs.dao.model.Transaction;

public interface AuthValueGeneratorService {
  String generateCAVV(Transaction transaction);
}
