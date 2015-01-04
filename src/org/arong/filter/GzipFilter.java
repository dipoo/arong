package org.arong.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class GzipFilter implements Filter {

	public void destroy() {

	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		MyHttpServletResponse myresp = new MyHttpServletResponse(resp);
		chain.doFilter(request, myresp);
		byte[] gbuf = myresp.gzipBuf();
		// 通知浏览器以gzip格式解析数据
		resp.setHeader("content-encoding", "gzip");
		resp.setHeader("content-length", gbuf.length + "");
		resp.getOutputStream().write(gbuf);
	}
}

/**
 * 根据修饰模式，自定义一个response类
 * 
 * @author Administrator
 * 
 */
class MyHttpServletResponse extends HttpServletResponseWrapper {
	private ByteArrayOutputStream bos = new ByteArrayOutputStream();
	private PrintWriter pw;

	public MyHttpServletResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		this.pw = new PrintWriter(new OutputStreamWriter(this.bos, "utf-8"));
		return this.pw;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new MyOutputStream(this.bos);
	}

	// 获取servlet发送的字节
	public byte[] getBuf() {
		if (this.pw != null) {
			pw.flush();
		}
		return bos.toByteArray();
	}

	// 压缩
	public byte[] gzipBuf() {
		// 将这个数组中的内容进行压缩
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// 它里面装入的是压缩后的信息。
		GZIPOutputStream gzop = null;
		try {
			gzop = new GZIPOutputStream(baos);
			gzop.write(getBuf());
			gzop.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				gzop.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}
}

class MyOutputStream extends ServletOutputStream {
	private ByteArrayOutputStream bos;

	public MyOutputStream(ByteArrayOutputStream bos) {
		super();
		this.bos = bos;
	}

	@Override
	public void write(int b) throws IOException {
		if (bos != null)
			this.bos.write(b);
	}

	/**
	 * 需要重写的方法，将字节数组写入bos中
	 */
	@Override
	public void write(byte[] b) throws IOException {
		if (bos != null) {
			bos.write(b);
		}
	}
}