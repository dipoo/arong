package org.arong.javamail;

import java.io.FileOutputStream;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

//复杂邮件-文本+附件+图片
public class TextAndImageAndAttachment {
	public static void main(String[] args) throws Exception{
		
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setFrom(new InternetAddress("aaa@zhaojun.com"));
		message.setRecipient(RecipientType.TO,new InternetAddress("bbb@haha.cn"));
		message.setSubject("text+append");
		
		//文本
		MimeBodyPart text = new MimeBodyPart();
		text.setContent("<img src='cid:xxx'/><br/>","text/html;charset=ISO8859-1");
		
		//图片
		MimeBodyPart image = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource("src/cn/itcast/web/email/config/zgl.jpg"));
		image.setDataHandler(dh);
		image.setContentID("xxx");
		
		//附件
		MimeBodyPart append = new MimeBodyPart();
		dh = new DataHandler(new FileDataSource("src/cn/itcast/web/email/config/year.zip"));
		append.setDataHandler(dh);
		append.setFileName(dh.getName());
		
		//设置文本和图片的关系为"related"
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(text);
		mm.addBodyPart(image);
		mm.setSubType("related");
		
		//将关系封装成一个邮件的组成部分
		MimeBodyPart textImage = new MimeBodyPart();
		textImage.setContent(mm);
		
		//设置文件图片和附件的关系
		MimeMultipart mmm = new MimeMultipart();
		mmm.addBodyPart(textImage);
		mmm.addBodyPart(append);
		mmm.setSubType("mixed");
		
		//设置邮件的关系
		message.setContent(mmm);
		message.writeTo(new FileOutputStream("d:/demo4.eml"));
	}
}