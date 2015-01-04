package org.arong.utils;

public class ExceptionUtil {
	/**
	 * 方法参数为空异常
	 * @param meodth 方法名
	 * @param paramName 参数名
	 * @param obj 对象
	 */
	public static void nullParamException(String meodth, String paramName, Object obj){
		if(obj == null){
			talk(meodth + ":" +paramName + "对象不能为空！");
		}
	}
	private static void talk(String message){
		throw new RuntimeException(message);
	}

}
