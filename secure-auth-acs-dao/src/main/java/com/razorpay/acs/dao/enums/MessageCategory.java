/*******************************************************************************
 * Copyright (C)  IZealiant Technologies 2017  - All Rights Reserved 
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential

 Author 				: Ashish Kirpan
 Created Date 			: Jul 29, 2017 
 ******************************************************************************/

package com.razorpay.acs.dao.enums;

public enum MessageCategory {

	PA("01", "PAYMENT_AUTHENTICATION"), 
	NPA("02", "NON_PAYMENT_AUTHENTICATION"),
	
	//New Message Category for Mastercard
	PVPA("85", "PV_PAYMENT_AUTHENTICATION"), //PRODUCTION_VALIDATION_PAYMENT_AUTHENTICATION
	PVNPA("86", "PV_NON_PAYMENT_AUTHENTICATION"), //PRODUCTION_VALIDATION_NON_PAYMENT_AUTHENTICATION
	
	//Added for Rupay
	NW("NW", "Transaction performed at domestic merchant"),
	TW("TW", "Transaction performed at domestic merchant along with Token provisioning"),
	IT("IT", "Transaction performed at International merchant"),
	AT("AT", "Authentication Transaction Only"),
	AW("AW", "Authentication transaction for Provisioning"),
	DI("DI", "Domestic InApp transaction"),
	II("II", "International InApp transaction");
	
	
	private String category;
	private String desc;

	private MessageCategory(String category, String desc) {
		this.category = category;
		this.desc = desc;
	}

	public String getCategory() {
		return category;
	}

	public String getDesc() {
		return desc;
	}

	public static MessageCategory getMessageCategory(String category) {
		for (MessageCategory messageCategory : MessageCategory.values()) {
			if (messageCategory.getCategory().equals(category)) {
				return messageCategory;
			}
		}
		return null;
	}
	
	public static boolean contains(MessageCategory[] categories ,String category) {
		for (MessageCategory messageCategory : categories) {
			if (messageCategory.getCategory().equals(category)) {
				return true;
			}
		}
		return false;
	}
	
}
