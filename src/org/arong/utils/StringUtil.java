package org.arong.utils;

public class StringUtil {
	/**
	 * 首字母变小写
	 * @param str
	 * @return
	 */
	public static String firstCharToLowerCase(String str){
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	/**
	 * 首字母变大写
	 * @param str
	 * @return
	 */
	public static String firstCharToUpperCase(String str){
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	/**
	 * 如果为null或者空字符，则返回true
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		if(str == null || "".equals(str.trim()))
			return true;
		return false;
	}
	/**
	 * 如果不为null并且不为空字符，则返回true
	 * @param str
	 * @return
	 */
	public static boolean notBlank(String str){
		if(str == null || "".equals(str.trim()))
			return false;
		return true;
	}
}
