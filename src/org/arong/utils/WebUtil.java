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

import org.arong.bean.BeanUtil;

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
					BeanUtil.setProperty(obj, name, values);
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
					BeanUtil.setProperty(obj, name, values);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("参数封装出错");
		}
		return obj;
	}
}