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

//复杂邮件-文本+附件
public class TextAndAttachment {
	public static void main(String[] args) throws Exception{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setFrom(new InternetAddress("aaa@zhaojun.com"));
		message.setRecipient(RecipientType.TO,new InternetAddress("bbb@haha.cn"));
		message.setSubject("text+append");
		
		//文本
		MimeBodyPart text = new MimeBodyPart();
		text.setContent("<b>there is an appned</b><br/>","text/html;charset=ISO8859-1");
		
		//附件
		MimeBodyPart append = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource("src/cn/itcast/web/email/config/year.zip"));
		append.setDataHandler(dh);
		//附件必须得设置一个名字
		//图片必须得设置一个ID
		append.setFileName(dh.getName());
		
		//设置文本和附件的关系
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(append);
		mm.addBodyPart(text);
		mm.setSubType("mixed");
		
		//设置邮件的关系
		message.setContent(mm);
		
		//写入到硬盘
		message.writeTo(new FileOutputStream("d:/demo3.eml"));
	}
}