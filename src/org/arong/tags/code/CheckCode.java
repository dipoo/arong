package org.arong.tags.code;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class CheckCode extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	private int num = 4;
	private String pattern = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private String var = "checkCode";

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		if(num > 6)
			this.num = 6;
		else
			this.num = num;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
	
	public int doEndTag() throws JspException {
		BufferedImage img = new BufferedImage(68, 22,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		Random r = new Random();
		Color c = new Color(255, 255, 255);
		g.setColor(c);
		g.fillRect(0, 0, 68, 22);
		StringBuffer sb = new StringBuffer();
		char[] ch = this.getPattern().toCharArray();
		int index, len = ch.length;
		for (int i = 0; i < this.getNum(); i++) {
			index = r.nextInt(len);
			g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
			g.setFont(new Font("Microsoft Yahei", Font.BOLD | Font.ITALIC, 22));
			g.drawString("" + ch[index], (i * 15) + 3, 18);
			sb.append(ch[index]);
		}
		try {
			//先把code的字符串放进session里
			this.pageContext.setAttribute(this.getVar(), sb.toString(), PageContext.SESSION_SCOPE);
			//获取字节输出流
			ServletOutputStream out = this.pageContext.getResponse().getOutputStream();	
			//输出图片
			ImageIO.write(img, "jpg", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}
}
