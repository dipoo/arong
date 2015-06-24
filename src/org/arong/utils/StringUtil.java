package org.arong.utils;

import java.util.StringTokenizer;

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
	
	/**
	 * 字符串替换
	 * @param srcString
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static String replace(String srcString, String oldString, String newString) {
		if(srcString == null) {
			return null;
		}
		char[] lineChars = srcString.toCharArray();
		char[] newStringChars = newString.toCharArray();
		int oldLength = oldString.length();
		StringBuffer buf = new StringBuffer(lineChars.length); //用于存储替换后的字符串
		
		int i = 0;
		int j = i;
		while((i = srcString.indexOf(oldString,i)) >= 0){
			buf.append(lineChars, j, i-j).append(newStringChars);
			i += oldLength;
			j = i;
		}
		buf.append(lineChars, j, lineChars.length - j); //拼接源字符串后面剩下的字符串
		return buf.toString();
	}
	
	public static void beforeTuning(){
		 long start = System.currentTimeMillis();
		 int[] array = new int[9999999];
		 for(int i=0;i<9999999;i++){
		 array[i] = i;
		 }
		 System.out.println(System.currentTimeMillis() - start);
		}

		public static void afterTuning(){
		 long start = System.currentTimeMillis();
		 int[] array = new int[9999999];
		 for(int i=0;i<9999999;i+=4){
		 array[i] = i;
		 array[i+1] = i+1;
		 array[i+2] = i+2;
		 }
		 System.out.println(System.currentTimeMillis() - start);
		}

		public static void main(String[] args){
		beforeTuning();
		afterTuning();
		}
}
