package org.arong.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CopyUtil 
{
	/**
	 * 将一个存储着javabean的list集合复制一份
	 * 深度复制，除了内存地址外，和源集合完全一样
	 * @param src
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException
	{           
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);
		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in =new ObjectInputStream(byteIn);
		List<T> dest = (List<T>)in.readObject();
		return dest;
	}
	/**
	 * 简单复制，采用循环把源集合数据依次添加到目标数组中，所以只能<br>
	 在集合数据量不大的情况下可以使用
	 * @param src
	 * @return
	 */
	public static <T> List<T> simpleCopy(List<T> src){
		if(src != null && src.size() > 0){
			List<T> dest = new ArrayList<T>();
			for(int i = 0; i < src.size(); i ++){
				dest.add(src.get(i));
			}
			return dest;
		}else
			return null;
	}
}
