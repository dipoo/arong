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

//复杂邮件-文本+图片
public class TextAndImage {
	public static void main(String[] args) throws Exception{
		//创建一封邮件
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setFrom(new InternetAddress("aaa@zhaojun.com"));
		message.setRecipient(RecipientType.TO,new InternetAddress("bbb@haha.cn"));
		message.setSubject("text+image");
		//创建文本
		MimeBodyPart text = new MimeBodyPart();
		//设置文本
		text.setContent("<b>this is a pictrue</b><br/><img src='cid:xxx'/><br/><img src='cid:yyy'/><br/>","text/html;charset=ISO8859-1");
		//创建图片A
		MimeBodyPart imageA = new MimeBodyPart();
		//读取图像的内容
		DataHandler dh = new DataHandler(new FileDataSource("src/cn/itcast/web/email/config/zgl.jpg"));
		//将dh绑定到image对象中
		imageA.setDataHandler(dh);
		//为图片设置一个惟一的编号
		imageA.setContentID("xxx");
		
		//创建图片B
		MimeBodyPart imageB = new MimeBodyPart();
		//读取图像的内容
		dh = new DataHandler(new FileDataSource("src/cn/itcast/web/email/config/lb.jpg"));
		//将dh绑定到image对象中
		imageB.setDataHandler(dh);
		//为图片设置一个惟一的编号
		imageB.setContentID("yyy");
		
		//创建文本和图片的关系
		MimeMultipart mm = new MimeMultipart();
		//依次将文本和图片加入到关系对象中
		mm.addBodyPart(text);
		mm.addBodyPart(imageA);
		//mm.addBodyPart(imageB);
		//设置二者的关系为"related"
		mm.setSubType("related");
		
		//将关系对象设置到邮件对象中
		message.setContent(mm);
		
		//将邮件写到硬盘上
		message.writeTo(new FileOutputStream("d:/demo2.eml"));
	}
}