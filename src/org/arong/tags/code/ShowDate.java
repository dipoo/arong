package org.arong.tags.code;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class ShowDate extends BodyTagSupport {
	private static final long serialVersionUID = 1L;

	private String pattern = "yyyy-MM-dd";

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int doEndTag() throws JspException {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat(this.pattern);
		String str = format.format(date);
		JspWriter out = this.pageContext.getOut();
		BodyContent body = this.getBodyContent();
		if(body != null)
			str = body.getString() + str;
		try {
			out.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}
}
