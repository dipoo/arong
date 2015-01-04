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
import javax.mail.internet.MimeUtility;

//复杂邮件-文本+附件+图片，解决中文问题
public class WithChineseEncoding {
	public static void main(String[] args) throws Exception{
		
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setFrom(new InternetAddress("aaa@zhaojun.com"));
		message.setRecipient(RecipientType.TO,new InternetAddress("bbb@haha.cn"));
		message.setSubject("创建复杂邮件");
		
		//文本
		MimeBodyPart text = new MimeBodyPart();
		text.setContent("<b>这是一张图片</b><br/><img src='cid:xxx'/><br/>","text/html;charset=UTF-8");
		
		//图片
		MimeBodyPart image = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource("src/cn/itcast/web/email/config/神奇的图片.JPG"));
		image.setDataHandler(dh);
		image.setContentID("xxx");
		
		//附件
		MimeBodyPart append = new MimeBodyPart();
		dh = new DataHandler(new FileDataSource("src/cn/itcast/web/email/config/万年历.zip"));
		append.setDataHandler(dh);
		//附件中如果有中文的话，一定要进行编码
		//任意一个邮件接收程序，会自动进行解码
		append.setFileName(MimeUtility.encodeText(dh.getName()));
		
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
		message.writeTo(new FileOutputStream("d:/demo5.eml"));
	}
}