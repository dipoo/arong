package org.arong.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 * 图片旋转
 * @author arong
 */
public class RotateImage {
	public static void main(String[] args) throws IOException {
		/*BufferedImage src = ImageIO.read(new File("f:/000.jpg"));
		InputStream is = rotateImgAsInputStream(src, 280, null);
		FileOutputStream fos = new FileOutputStream("f:/003.jpg");
		byte[] b = new byte[1024];
		int len = 0;
		while((len = is.read(b)) != -1){
			fos.write(b, 0, len);
		}
		fos.flush();
		fos.close();
		is.close();*/
		
		/**
		 * 关于matrix(cosθ,sinθ,-sinθ,cosθ,0,0)
		 */
		//获取角度
		System.out.println(Math.toDegrees(Math.acos(0.8829480)));//28
		
		
	}
	
	public static InputStream rotateImgAsInputStream(BufferedImage image, int degree, Color bgcolor) throws IOException {

		BufferedImage rotatedImage = rotateImg(image, degree, bgcolor);
		
		ByteArrayOutputStream byteOut= new ByteArrayOutputStream();
		ImageOutputStream iamgeOut = ImageIO.createImageOutputStream(byteOut);
		
		ImageIO.write(rotatedImage, "png", iamgeOut);
		InputStream inputStream = new ByteArrayInputStream(byteOut.toByteArray());
		
		return inputStream;
	}
	
	public static BufferedImage rotateImg(BufferedImage image, int degree, Color bgcolor) throws IOException {
		int iw = image.getWidth();//原始图象的宽度 
		int ih = image.getHeight();//原始图象的高度
		int w = 0;
		int h = 0;
		int x = 0;
		int y = 0;
		degree = degree % 360;
		if (degree < 0)
			degree = 360 + degree;//将角度转换到0-360度之间
		double ang = Math.toRadians(degree);//将角度转为弧度

		/** 
		*确定旋转后的图象的高度和宽度 
		*/ 
		if (degree == 180 || degree == 0 || degree == 360) { 
			w = iw; 
			h = ih; 
		} else if (degree == 90 || degree == 270) { 
			w = ih; 
			h = iw; 
		} else { 
			//int d = iw + ih; 
			double cosVal = Math.abs(Math.cos(ang));
			double sinVal = Math.abs(Math.sin(ang));
			w = (int) (sinVal*ih) + (int) (cosVal*iw); 
			h = (int) (sinVal*iw) + (int) (cosVal*ih); 
		}

		x = (w / 2) - (iw / 2);//确定原点坐标
		y = (h / 2) - (ih / 2);
		BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		Graphics2D gs = (Graphics2D)rotatedImage.getGraphics();
		if(bgcolor==null){
			rotatedImage  = gs.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		}else{
			gs.setColor(bgcolor);
			gs.fillRect(0, 0, w, h);//以给定颜色绘制旋转后图片的背景
		}
		
		AffineTransform at = new AffineTransform();
		at.rotate(ang, w / 2, h / 2);//旋转图象
		at.translate(x, y);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		op.filter(image, rotatedImage);
		return rotatedImage;
	}
}
