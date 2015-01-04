package org.arong.javamail;

import java.io.FileOutputStream;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//简单邮件-只含文本
public class SimpleText {
	public static void main(String[] args) throws Exception{
		//创建一封使用MIME协议的邮件
		Properties props = new Properties();
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(props));
		//设置邮件的相关属性
		message.setFrom(new InternetAddress("aaa@zhaojun.com"));
		message.setRecipient(RecipientType.TO,new InternetAddress("bbb@haha.cn"));
		message.setSubject("BBS注册信息");
		message.setContent("<font size='44' color='green'>恭喜你，注册成为，激活码是123456</font>","text/html;charset=UTF-8");
		//将邮件存在硬盘上
		message.writeTo(new FileOutputStream("d:/demo1.eml"));
	}
}