package org.arong.tags.functions;

import java.security.MessageDigest;

public class MD5Encoder {
	private static String[] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	private static String byteArrayToHexString(byte[] byteArray) {
		StringBuffer sb = new StringBuffer();
		for(byte b : byteArray){
			//将每一个byte转换十六进制数
			sb.append(byteToHexChar(b));
		}
		return sb.toString();
	}
	//核心
	private static Object byteToHexChar(byte b) {//-128到+127之间
		int n = b;//n=110
		//如果n为负数
		if( n < 0 ){
			//转正
			n = 256 + n;
		}
		//第一位是商
		int d1 = n / 16;//d1 = 6
		//第二位是余数
		int d2 = n % 16;//d2 = 14
		//拼接字符串
		return hex[d1] + hex[d2];
	}
	public static String encode(String password) throws Exception {
		if(password == null){
			throw new Exception();
		}
		//创建密码生成器(MD5或SHA)
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		//将明文转成byte[]
		byte[] byteArray = md5.digest(password.getBytes());
		//将byte[]转成十六进制的字符串
		String passwordMD5 = byteArrayToHexString(byteArray);
		return passwordMD5;
	}
}
