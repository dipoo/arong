package org.arong.javamail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

//演示socket程序发送邮件，客户端
public class SocketSend {
	public static void main(String[] args) throws Exception{
		
		for(int i=1;i<=10;i++){
		
		//远程连接邮件服务器的25号端口
		Socket socket = new Socket("192.168.10.222",25);
		//构造输入流
		InputStream is = socket.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		//构造输出流
		OutputStream os = socket.getOutputStream();
		
		//获取服务端的数据
		System.out.println(br.readLine());
		
		//向服务端发送数据
		os.write("ehlo aaa@zhaojun.com\r\n".getBytes());
		System.out.println(br.readLine());
		System.out.println(br.readLine());
		
		os.write("auth login\r\n".getBytes()); 
		System.out.println(br.readLine());
		
		os.write("YWFhQHpoYW9qdW4uY29t\r\n".getBytes());//用户名
		System.out.println(br.readLine());
		
		os.write("MTIzNDU2\r\n".getBytes());//密码
		System.out.println(br.readLine());
		
		os.write("mail from:<aaa@zhaojun.com>\r\n".getBytes());
		System.out.println(br.readLine());
		
		os.write("rcpt to:<admin@0731life.com>\r\n".getBytes());
		
		System.out.println(br.readLine());

		os.write("data\r\n".getBytes());
		System.out.println(br.readLine());
		
		os.write("from:aaa@zhaojun.com\r\n".getBytes());
		os.write("to:admin@0731life.com\r\n".getBytes());
		os.write("subject:xxxx\r\n".getBytes());
		os.write("Hello! How are you doing!\r\n".getBytes());
		os.write(".\r\n".getBytes());
		System.out.println(br.readLine());
		
		os.write("quit\r\n".getBytes());
	
		}
	}
}