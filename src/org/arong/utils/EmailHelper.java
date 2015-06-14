package org.arong.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailHelper {

	private String host = "smtp.163.com"; // smtp服务器 有好多类型的host

	public static String user = "ssdjakflf@163.com";//

	private String pwd = "fratisuaaxrpretr"; // 授权密码

	private String from = "zuidaima@qq.com"; // 发件人地址

	private String to = ""; // 收件人地址

	private String subject = "www.zuidaima.com"; // 邮件标题

	public void setAddress(String from, String to, String subject) {

		this.from = from;

		this.to = to;

		this.subject = subject;

	}

	public void send(String txt, String filePath,String name) {

		Properties props = new Properties();

		// 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）

		props.put("mail.smtp.host", host);

		// 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）

		props.put("mail.smtp.auth", "true");

		// 用刚刚设置好的props对象构建一个session

		Session session = Session.getDefaultInstance(props);

		// 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使

		// 用（你可以在控制台（console)上看到发送邮件的过程）

		session.setDebug(true);

		// 用session为参数定义消息对象

		MimeMessage message = new MimeMessage(session);

		try {

			// 加载发件人地址

			message.setFrom(new InternetAddress(from));

			// 加载收件人地址

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// 加载标题

			message.setSubject(subject);

			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件

			Multipart multipart = new MimeMultipart();

			// 设置邮件的文本内容

			BodyPart contentPart = new MimeBodyPart();

//			contentPart.setText(txt);
			//设置发送的格式为html
			contentPart.setContent(txt, "text/html;charset = gbk");  

			multipart.addBodyPart(contentPart);

			// 添加附件
			BodyPart messageBodyPart = new MimeBodyPart();
			if (filePath!=null &&name!=null) {
				 DataSource source = new FileDataSource(filePath);
				// 添加附件的内容
				 messageBodyPart.setDataHandler(new DataHandler(source));

				// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码

				 sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();

				 messageBodyPart.setFileName("=?GBK?B?"+

				 enc.encode(name.getBytes()) +"?=");
				 
				 multipart.addBodyPart(messageBodyPart);
				 //将multipart对象放到message中
			}
			 

			// 添加附件的标题
			message.setContent(multipart);

			// 保存邮件

			message.saveChanges();

			// 发送邮件

			Transport transport = session.getTransport("smtp");

			// 连接服务器的邮箱

			transport.connect(host, user, pwd);

			// 把邮件发送出去

			transport.sendMessage(message, message.getAllRecipients());

			transport.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}
