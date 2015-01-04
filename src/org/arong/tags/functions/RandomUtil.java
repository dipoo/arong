package org.arong.tags.functions;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {
	/**
	 * 获取一个随机的字符串数字，从指定的大小中
	 * @param start
	 * @param end
	 * @return
	 */
	public static String randomString(Integer start, Integer end){
		Random random = new Random();
		//得到1000-9999的数字
		int num = random.nextInt(end - start + 1) + start;
		return num + "";
	}
	/**
	 * 随机返回一个uuid字符串
	 * @return
	 */
	public static String UUIDString(){
		return UUID.randomUUID().toString();
	}
}
