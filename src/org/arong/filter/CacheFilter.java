package org.arong.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CacheFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		String uri = req.getRequestURI();
		if(uri.endsWith("jsp")){
			resp.setDateHeader("expires", -1);
			resp.setHeader("cache-control", "no-cache");
			resp.setHeader("pragma", "no-cache");
		}
		else if(uri.endsWith("html")){
			Long l = System.currentTimeMillis() + 60 * 60 * 24 * 1000;
			resp.setDateHeader("expires", l);
			resp.setHeader("cache-control", String.valueOf(l / 1000));
			resp.setHeader("pragma", String.valueOf(l / 1000));
		}
		chain.doFilter(request, response);
	}

	public void destroy() {

	}

}
