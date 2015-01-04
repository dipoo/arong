package org.arong.tags.code;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class Referer extends BodyTagSupport {

	private static final long serialVersionUID = 1L;
	private String site;
	private String back;

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public int doEndTag() throws JspException {
		//获取父类提供的pageContext对象
		PageContext pageContext = this.pageContext;
		//获取request对象
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		//获取网站的根路径
		String webRoot = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath();
		//如果site属性没赋值，则使用网站根路径
		if (getSite() == null)
			setSite(webRoot);
		//如果back属性没赋值，则使用网站根路径
		if (getBack() == null)
			setBack(webRoot);
		//获取请求头信息的referer信息
		String header = request.getHeader("referer");
		//判断请求头信息是否符合规定
		if (header != null && header.startsWith(getSite())) {
			//返回指令继续执行的int值
			return EVAL_PAGE;
		} else {
			try {
				//不符合规定重定向到back属性指定的路径
				((HttpServletResponse) pageContext.getResponse())
						.sendRedirect(getBack());
			} catch (IOException e) {
				e.printStackTrace();
			}
			//返回指令不执行的int值
			return SKIP_PAGE;
		}
	}
}