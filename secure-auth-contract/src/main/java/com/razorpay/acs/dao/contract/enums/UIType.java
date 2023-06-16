/*******************************************************************************
 * Copyright (C)  IZealiant Technologies 2017  - All Rights Reserved 
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential

 Author 				: Ashish Kirpan
 Created Date 			: Aug 8, 2017 
 ******************************************************************************/

package com.razorpay.acs.dao.contract.enums;

public enum UIType {

	TEXT("01"), 
	SINGLE_SELECT("02"), 
	MULTI_SELECT("03"), 
	OOB("04"), 
	HTML_OTHER("05");

	private String type;

	private UIType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static UIType getUIType(String type) {
		for (UIType uiType : UIType.values()) {
			if (uiType.getType().equals(type)) {
				return uiType;
			}
		}
		return null;
	}
}