package org.arong.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4jUtil {
	// 获取DOM对象
	public static Document getDOM(File file) throws Exception {
		return new SAXReader().read(file);
	}

	// 存储dom对象到xml文件
	public static void writeDOM2XML(File file, Document doc) throws Exception {
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF-8"), format);
		writer.write(doc);
		writer.close();
	}
}
