package org.arong.tags.code;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

@SuppressWarnings("serial")
public class ParamToken extends BodyTagSupport {

	@Override
	public int doEndTag() throws JspException {
		// 获取父类提供的pageContext对象
		PageContext pageContext = this.pageContext;
		// 获取request对象
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		HttpSession session = request.getSession();
		String UUIDStr = UUID.randomUUID().toString();
		session.setAttribute("token", UUIDStr);
		try {
			pageContext.getOut().flush();
			pageContext.getOut().write(UUIDStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}
}
