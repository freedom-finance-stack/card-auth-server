/*******************************************************************************
 * Copyright (C)  IZealiant Technologies 2016  - All Rights Reserved 
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 ******************************************************************************/
package com.razorpay.threeds.exception.checked;



// Checked Exception
public class DataNotFoundException extends ACSException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DataNotFoundException(String errorCode, String message, Throwable cause, boolean enableSuppression,
								 boolean writableStackTrace) {
		super(errorCode, message, cause, enableSuppression, writableStackTrace);
	}

	public DataNotFoundException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}

	public DataNotFoundException(String errorCode, String message) {
		super(errorCode, message);
	}

	public DataNotFoundException(String errorCode, Throwable cause) {
		super(errorCode, cause);
	}

}
