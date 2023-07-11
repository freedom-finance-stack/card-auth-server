package com.razorpay.threeds.validator.rules;

import com.razorpay.threeds.exception.ValidationException;

public interface Rule<T> {
  void validate(T value) throws ValidationException;

  default Rule<T> and(Rule<T> other) {
    return value -> {
      validate(value);
      other.validate(value);
    };
  }
}
