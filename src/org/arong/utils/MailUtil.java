package org.arong.utils;

import java.util.Properties;
import java.util.UUID;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//邮箱发送帮助类
public final class MailUtil {
	private static String managerEmail = "admin@admin.com";
	private static String host = "127.0.0.1";
	@SuppressWarnings("static-access")
	public static void send(String username, String email)throws Exception {
		//创建Session对象
		Properties props = new Properties();
		//发送邮件的协议
		props.put("mail.transport.protocol","smtp");
		//发送邮件的中转邮件服务器的IP地址
		props.put("mail.host",host);
		Session session = Session.getDefaultInstance(props);
		//取得传输对象，类似Connection对象
		Transport t = session.getTransport();
		for(int i=1;i<=5;i++){
			//创建邮件
			Message message = createMessage(session,username,email);
			//发送邮件
			t.send(message);
		}
		//关闭
		t.close();	
	}
	//创建邮件
	private static Message createMessage(Session session,String username,String email) throws Exception{
		MimeMessage message = new MimeMessage(session);
		//管理员
		message.setFrom(new InternetAddress(managerEmail));
		//注册用户名
		message.setRecipient(RecipientType.TO,new InternetAddress(email));
		message.setSubject("BBS网站");
		message.setContent("恭喜["+username+"]用户，注册成功，你是激活码是"+UUID.randomUUID().toString(),"text/html;charset=UTF-8");
		return message;
	}
	public static void main(String[] args) throws Exception {
		MailUtil.setHost("192.168.10.55");
		MailUtil.send("张绍荣", "bbb@yy.com");
	}
	public static void setManagerEmail(String managerEmail) {
		MailUtil.managerEmail = managerEmail;
	}
	public static void setHost(String host) {
		MailUtil.host = host;
	}
}