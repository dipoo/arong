package org.arong.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class JdomUtil {
	// 获取解析器
	public static SAXBuilder getParser() {
		return new SAXBuilder();
	}

	// 获取DOM对象
	public static Document getDOM(File file) throws Exception {
		SAXBuilder parser = getParser();
		Document doc = parser.build(file);
		return doc;
	}

	// 存储dom对象
	public static void writeDOM2XML(Document doc, File file) throws Exception {
		Format format = Format.getPrettyFormat();
		XMLOutputter outputter = new XMLOutputter(format);
		outputter.output(doc, new OutputStreamWriter(
				new FileOutputStream(file), "UTF-8"));
	}
}
