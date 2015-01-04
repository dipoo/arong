package org.arong.tags.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLOpera {
	/**
	 * tomcat example里的一个用于过滤html特殊字符的方法
	 * @param message
	 * @return
	 */
	public static String filter(String message) {
		
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
	 *            按字节长度截取字符串(支持截取带HTML代码样式的字符串)
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
	 * @param str
	 * @param length
	 * @return
	 */
	public static String getNoHTMLString(String str, int length) {
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
}
