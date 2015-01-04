package org.arong.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class EncodingFilter implements Filter {

	public void init(FilterConfig arg0) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		// 出现的问题是get方式提交的解决不了，那么我们将request进行重写，判断如果是get方式我们通过
		// getBytes("iso8859-1")
		chain.doFilter(new MyHttpServletRequest(request), response);
	}
}

class MyHttpServletRequest extends HttpServletRequestWrapper {
	HttpServletRequest request;

	public MyHttpServletRequest(HttpServletRequest request) {
		super(request);
		this.request = request;
	}

	@Override
	public String getParameter(String name) {
		// 得到当前表单是get还是post方式提交
		String method = request.getMethod();
		String value = request.getParameter(name);// 没有转码
		if ("GET".equals(method)) {
			try {
				value = new String(value.getBytes("iso8859-1"), "utf-8"); // 转码
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new RuntimeException("转码失败");
			}
		}
		return value;
	}
}
