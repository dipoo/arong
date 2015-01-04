package org.arong.filter;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EncodingFilter2 implements Filter {

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
		RequestProxy requestProxy = new RequestProxy(request);
		HttpServletRequest xx = requestProxy.getRequest();
		chain.doFilter(xx, response);
	}
}

// 动态代理
class RequestProxy {
	// 目标对象
	private HttpServletRequest request;

	public RequestProxy(HttpServletRequest request) {
		this.request = request;
	}

	// 产生动态代理对象
	public HttpServletRequest getRequest() {
		return (HttpServletRequest) Proxy.newProxyInstance(RequestProxy.class
				.getClassLoader(), this.request.getClass().getInterfaces(),
				new InvocationHandler() {

					public Object invoke(Object object, Method method,
							Object[] args) throws Throwable {

						Object returnValue = "";
						// 获取方法名
						String methodName = method.getName();
						if ("getParameter".equals(methodName)) {
							// 请求方式
							String methodType = request.getMethod();
							if ("GET".equalsIgnoreCase(methodType)) {
								String temp = (String) method.invoke(request,
										args);
								if (temp != null)
									returnValue = new String(temp
											.getBytes("ISO-8859-1"), "UTF-8");
							} else {
								request.setCharacterEncoding("UTF-8");
								returnValue = method.invoke(request, args);
							}
						}
						// 其他方法，不做处理
						else {
							returnValue = method.invoke(request, args);
						}
						return returnValue;
					}
				});
	}
}