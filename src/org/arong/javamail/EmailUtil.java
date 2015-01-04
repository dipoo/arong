package org.arong.javamail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//帮助类
public final class EmailUtil {
	//发送邮件
	@SuppressWarnings("static-access")
	public static void send(String username, String email) throws Exception {
		Properties props = new Properties();
		props.put("mail.transport.protocol","smtp");
		props.put("mail.host","127.0.0.1");
		Session session = Session.getDefaultInstance(props);
		Message message = createMessage(session,username,email);
		Transport transport = session.getTransport();
		transport.connect("127.0.0.1","aaa@zhaojun.com","123456");
		transport.send(message);
		transport.close();
	}
	//创建邮件
	public static Message createMessage(Session session,String usrename,String email) throws Exception{
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("aaa@zhaojun.com"));//网站管理员
		message.setRecipient(RecipientType.TO,new InternetAddress(email));//新用户的邮箱
		message.setSubject("BBS论坛");
		message.setContent("<b>恭喜"+usrename+"用户，注册成功</b>","text/html;charset=UTF-8");
		return message;
	}
}