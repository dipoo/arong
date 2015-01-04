package org.arong.tags.functions;

import java.util.Random;

public class Coder {
	// 定义分隔符常量
	public static final String DIVIDE_CHAR = "%";
	private static final int DIS1 = 18500;
	private static final int DIS2 = -222;
	private static final int RANDOM_NUM = 12345;

	/**
	 * 返回中文(所有字符)字符串的ASCII码
	 * 
	 * @param s
	 * @return
	 */
	public static String encode(String s) {
		String str = null;
		// 如果待转化的字符串不为空
		if (s != null && s.trim().length() > 0) {
			// 创建字符串缓冲区
			StringBuffer sb = new StringBuffer();
			// 将待转化的字符串转成字符数组
			// 创建一个随机数对象
			Random random = new Random();
			int start = random.nextInt(RANDOM_NUM);
			int end = random.nextInt(RANDOM_NUM);
			sb.append(start + DIVIDE_CHAR);
			char[] chars = s.toCharArray();
			// 遍历此字符数组
			for (int i = 0; i < chars.length; i++) {
				char ch = chars[i];
				sb.append((int) ch + DIS1 + i + DIS2 + start - end
						+ DIVIDE_CHAR);
			}
			sb.append(end);
			// 转化为字符串
			str = sb.toString();
		}
		return str;
	}

	/**
	 * 解码
	 * 
	 * @param codeStr
	 * @return
	 */
	public static String decode(String codeStr) {
		String str = null;
		// 待反编码的字符串不为空
		if (codeStr != null && codeStr.trim().length() > 0) {
			// 将字符串以逗号分隔成字符串数组
			String[] codes = codeStr.split("" + DIVIDE_CHAR + "");
			int start = Integer.parseInt(codes[0]);
			int end = Integer.parseInt(codes[codes.length - 1]);
			// 建立字符串缓冲区
			StringBuffer sb = new StringBuffer();
			// 定义一个初始ASCII码变量
			int code = 0;
			// 遍历字符串数组
			for (int i = 1; i < codes.length - 1; i++) {
				try {
					code = Integer.parseInt(codes[i]) - DIS1 - i + 1 - DIS2
							- start + end;
				} catch (NumberFormatException e) {
					e.printStackTrace();
					throw new RuntimeException("这个字符串不符合规格，请传入形如：“1"
							+ DIVIDE_CHAR + "2" + DIVIDE_CHAR + "3"
							+ DIVIDE_CHAR + "4”之类的字符串");
				}
				sb.append((char) code);
			}
			str = sb.toString();
		}
		return str;
	}
}