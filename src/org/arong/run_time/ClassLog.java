package org.arong.run_time;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassLog {
	/**
	 * 打印出一个对象中所有无参的get()方法的返回值
	 * 
	 * @param obj
	 */
	public static void printObjectMethods(Object obj) {
		if (obj == null) {
			System.out.println("警告：printObjectMethods(Object obj):obj为空！");
			return;
		}
		String methodName = null;
		Method[] methods = obj.getClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			methodName = methods[i].getName();
			methods[i].setAccessible(true);
			if (methodName.startsWith("get")) {
				int len = methods[i].getParameterTypes().length;
				// 参数个数为0并且返回值不为void
				if (len == 0
						&& !"void".equals(methods[i].getReturnType().getName()))
					try {
						System.out.println(methodName + "() : "
								+ methods[i].invoke(obj));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
			}
		}
	}
}
