package client.service;

import md5.MD5Crypt;

public class EncryptionService {

	public static String ecrypt(String inputString) {
		
		if(inputString == null) return null;
		
		if(inputString.isEmpty()) return inputString;
		
		return MD5Crypt.apacheCrypt(inputString);
	}
}
