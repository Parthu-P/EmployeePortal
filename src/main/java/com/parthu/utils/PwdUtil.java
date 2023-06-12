package com.parthu.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class PwdUtil {
	
	private PwdUtil() {
		
	}
	public static String generatedRamdomPwd() {
		String characters="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		return RandomStringUtils.random(6, characters);


	}
}
