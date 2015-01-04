package org.arong.utils;

/**
 *
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public final class WebUtil {
	/**
	 * tomcat example里的一个用于过滤html特殊字符的方法
	 * 
	 * @param message
	 * @return
	 */
	public static String filterHTML(String message) {
		if (message == null)
			return (null);
		char content[] = new char[message.length()];
		message.getChars(0, message.length(), content, 0);
		StringBuffer result = new StringBuffer(content.length + 50);
		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '"':
				result.append("&quot;");
				break;
			default:
				result.append(content[i]);
			}
		}
		return (result.toString());
	}

	/**
	 * 按字节长度截取字符串(支持截取带HTML代码样式的字符串)
	 * 
	 * @param param
	 *            将要截取的字符串参数
	 * @param length
	 *            截取的字节长度
	 * @param end
	 *            字符串末尾补上的字符串
	 * @return 返回截取后的字符串
	 */
	public static String subStringHTML(String param, int length, String end) {
		StringBuffer result = new StringBuffer();
		int n = 0;
		char temp;
		boolean isCode = false; // 是不是HTML代码
		boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
		for (int i = 0; i < param.length(); i++) {
			temp = param.charAt(i);
			if (temp == '<') {
				isCode = true;
			} else if (temp == '&') {
				isHTML = true;
			} else if (temp == '>' && isCode) {
				n = n - 1;
				isCode = false;
			} else if (temp == ';' && isHTML) {
				isHTML = false;
			}
			if (!isCode && !isHTML) {
				n = n + 1;
				// UNICODE码字符占两个字节
				if ((temp + "").getBytes().length > 1) {
					n = n + 1;
				}
			}
			result.append(temp);
			if (n >= length) {
				break;
			}
		}
		// 取出截取字符串中的HTML标记
		String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)",
				"$1$2");
		// 去掉不需要结素标记的HTML标记
		temp_result = temp_result
				.replaceAll(
						"</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
						"");
		// 去掉成对的HTML标记
		temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>",
				"$2");
		// 用正则表达式取出标记
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
		Matcher m = p.matcher(temp_result);
		List<String> endHTML = new ArrayList<String>();
		while (m.find()) {
			endHTML.add(m.group(1));
		}
		// 补全不成对的HTML标记
		for (int i = endHTML.size() - 1; i >= 0; i--) {
			result.append("</");
			result.append(endHTML.get(i));
			result.append(">");
		}
		result.append(end);
		return result.toString();
	}

	/**
	 * 截取不带html标签的字符串
	 * 
	 * @param str
	 *            被截取的字符串
	 * @param length
	 *            指定截取的长度
	 * @return
	 */
	public static String subStringNoHTML(String str, int length) {
		String reg = "<([a-zA-Z]+)[^<>]*>|</([a-zA-Z]+)[^<>]*>";
		// String reg = "/< (.*)>.*|< (.*) />/";
		Pattern pat = Pattern.compile(reg);
		Matcher mat = pat.matcher(str);
		String s = mat.replaceAll("").trim();
		if (s.length() > length) {
			s = s.substring(0, length);
		}
		return s;
	}

	/**
	 * 随机生成一个字符串以及此字符串的BufferedImage
	 * 
	 * @return List<Object> 返回一个长度为2的List对象，此对象第一个元素为字符串形式的验证码<br />
	 *         第二个元素为BufferedImage形式的验证码
	 */
	public static List<Object> getCheckCode() {
		List<Object> codeList = new ArrayList<Object>();
		BufferedImage img = new BufferedImage(68, 22,
				BufferedImage.TYPE_INT_RGB);
		// 得到该图片的绘图对象
		Graphics g = img.getGraphics();
		Random r = new Random();
		Color c = new Color(200, 150, 255);
		g.setColor(c);
		// 填充整个图片的颜色
		g.fillRect(0, 0, 68, 22);
		// 向图片中输出数字和字母
		StringBuffer sb = new StringBuffer();
		char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		int index, len = ch.length;
		for (int i = 0; i < 4; i++) {
			index = r.nextInt(len);
			g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
			g.setFont(new Font("Microsoft Yahei", Font.BOLD | Font.ITALIC, 22));// 输出的
																				// 字体和大小
			g.drawString("" + ch[index], (i * 15) + 3, 18);// 写什么数字，在图片的什么位置画
			sb.append(ch[index]);
		}
		codeList.add(sb.toString());
		codeList.add(img);
		return codeList;
	}

	/**
	 * 将请求中的参数封装到指定的JavaBean对象中去<br >
	 * 此方法会对不同的method请求进行编码<br >
	 * 此方法不依赖与BeanUtils框架,而且能处理包括int,long,double,float,Date,java.sql.Date等类型数据
	 * 
	 * @param request
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object myFormToBean(HttpServletRequest request, Class<?> clazz) {
		Object obj = null;
		try {
			// 创建对象
			obj = clazz.newInstance();
			// 一次性收集表单中所有的参数的名字，而不是对应的值
			Enumeration<String> enums = request.getParameterNames();
			String method = request.getMethod();
			if (method.toLowerCase().equals("post")) {
				request.setCharacterEncoding("UTF-8");
				// 迭代
				while (enums.hasMoreElements()) {
					// 取出该元素
					String name = enums.nextElement();
					// 取得对应的值
					String[] values = request.getParameterValues(name);
					setProperty(obj, name, values);
				}
			} else if (method.toLowerCase().equals("get")) {
				int len = 0;
				int i;
				// 迭代
				while (enums.hasMoreElements()) {
					// 取出该元素
					String name = enums.nextElement();
					// 取得对应的值
					String[] values = request.getParameterValues(name);
					// 将数组中的值进行强制转码
					if (values != null && (len = values.length) > 0) {
						for (i = 0; i < len; i++) {
							values[i] = new String(
									values[i].getBytes("ISO-8859-1"), "UTF-8");
						}
					}
					setProperty(obj, name, values);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("参数封装出错");
		}
		return obj;
	}

	/**
	 * 实现与BeanUtils.setProperty()类似功能
	 * 
	 * @param obj
	 * @param name
	 * @param values
	 */
	static void setProperty(Object obj, String name, String[] values) {
		Class<?> clazz = obj.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		String methodName = null;
		String propertyName = null;
		String propertyType = null;
		for (int i = 0; i < methods.length; i++) {
			methodName = methods[i].getName();
			if (methodName.startsWith("set")) {
				propertyName = Introspector.decapitalize(methodName.substring(
						3, methodName.length()));
				if (propertyName.equals(Introspector.decapitalize(name))) {
					propertyType = methods[i].getParameterTypes()[0]
							.getCanonicalName();
					try {
						methods[i].invoke(obj, converter(values, propertyType));
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

	/**
	 * 将一个String数组进行类型转换<br>
	 * 当数组长度为1时，返回值为非数组
	 * 
	 * @param values
	 *            字符串数组
	 * @param typeName
	 *            类型全限定名
	 * @return
	 */
	private static Object converter(String[] values, String typeName) {
		if (values == null) {
			return null;
		} else if (values.length == 1) {
			String value = values[0];
			if (typeName == null)
				return null;
			else if (typeName.trim().equals("java.lang.String")) {
				return value;
			} else if (typeName.trim().equals("java.lang.Integer")) {
				return NumberUtil.can2Int(value) ? Integer.parseInt(value) : 0;
			} else if (typeName.trim().equals("int")) {
				return NumberUtil.can2Int(value) ? Integer.parseInt(value) : 0;
			} else if (typeName.trim().equals("java.lang.Long")) {
				return Long.parseLong(value);
			} else if (typeName.trim().equals("long")) {
				return Long.parseLong(value);
			} else if (typeName.trim().equals("java.lang.Double")) {
				return Double.parseDouble(value);
			} else if (typeName.trim().equals("double")) {
				return Double.parseDouble(value);
			} else if (typeName.trim().equals("java.lang.Float")) {
				return Float.parseFloat(value);
			} else if (typeName.trim().equals("float")) {
				return Float.parseFloat(value);
			} else if (typeName.trim().equals("java.util.Date")) {
				return String2Date(value);
			} else if (typeName.trim().equals("java.sql.Date")) {
				return new java.sql.Date(String2Date(value).getTime());
			} else
				return null;
		} else {
			if (typeName == null)
				return null;
			else if (typeName.trim().equals("java.lang.String[]")) {
				return values;
			} else if (typeName.trim().equals("java.lang.Integer[]")) {
				return NumberUtil.parseInt(values);
			} else if (typeName.trim().equals("int[]")) {
				return NumberUtil.parseInt(values);
			} else if (typeName.trim().equals("java.lang.Long[]")) {
				Long[] longs = new Long[values.length];
				for (int i = 0; i < values.length; i++) {
					longs[i] = Long.parseLong(values[i]);
				}
				return longs;
			} else if (typeName.trim().equals("long[]")) {
				Long[] longs = new Long[values.length];
				for (int i = 0; i < values.length; i++) {
					longs[i] = Long.parseLong(values[i]);
				}
				return longs;
			} else if (typeName.trim().equals("java.lang.Double[]")) {
				Double[] doubles = new Double[values.length];
				for (int i = 0; i < values.length; i++) {
					doubles[i] = Double.parseDouble(values[i]);
				}
				return doubles;
			} else if (typeName.trim().equals("double[]")) {
				Double[] doubles = new Double[values.length];
				for (int i = 0; i < values.length; i++) {
					doubles[i] = Double.parseDouble(values[i]);
				}
				return doubles;
			} else if (typeName.trim().equals("java.lang.Float[]")) {
				Float[] floats = new Float[values.length];
				for (int i = 0; i < values.length; i++) {
					floats[i] = Float.parseFloat(values[i]);
				}
				return floats;
			} else if (typeName.trim().equals("float[]")) {
				Float[] floats = new Float[values.length];
				for (int i = 0; i < values.length; i++) {
					floats[i] = Float.parseFloat(values[i]);
				}
				return floats;
			} else if (typeName.trim().equals("java.util.Date[]")) {
				Date[] dates = new Date[values.length];
				for (int i = 0; i < values.length; i++) {
					dates[i] = String2Date(values[i]);
				}
				return dates;
			} else if (typeName.trim().equals("java.sql.Date[]")) {
				java.sql.Date[] dates = new java.sql.Date[values.length];
				for (int i = 0; i < values.length; i++) {
					dates[i] = new java.sql.Date(String2Date(values[i])
							.getTime());
				}
				return dates;
			} else
				return null;
		}
	}

	/**
	 * 将日期形式的字符串转换为Date
	 * 
	 * @param s
	 * @return
	 */
	private static Date String2Date(String s) {
		Date date = null;
		Locale locale = Locale.CHINA;
		DateFormat format = null;
		if (Pattern.matches("\\d{4}.\\d{1,2}.\\d{1,2}", s)) {
			format = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		} else if (Pattern.matches("\\d{2}.\\d{2}.\\d{2}", s)) {
			format = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
		} else if (Pattern.matches(
				"\\d{4}.\\d{1,2}.\\d{1,2}[ ]\\d{1,2}.\\d{1,2}.\\d{1,2}", s)) {
			format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
					DateFormat.MEDIUM, locale);
		} else {
			format = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			date = format.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}