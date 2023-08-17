/*******************************************************************************
 * Copyright (C)  IZealiant Technologies 2017  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Author 				: Ashish Kirpan
 * Created Date 			: Aug 1, 2017
 ******************************************************************************/

package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.Authentication;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;

public interface AuthenticationService<T extends Authentication> {

    public void preAuthenticate(T t) throws ACSException;

    public AuthResponse authenticate(T t) throws ACSException;
}
