package org.arong.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class DOMUtil {
	// 1.获取解析器
	public static DocumentBuilder getParser() throws Exception {
		// 1.1 解析器工厂
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// 1.2 解析器
		DocumentBuilder parser = dbf.newDocumentBuilder();
		// 1.3 返回
		return parser;
	}

	// 2.获取DOM对象
	public static Document getDOM(File xmlFile) throws Exception {
		DocumentBuilder parser = getParser();
		Document dom = parser.parse(xmlFile);
		return dom;
	}

	// 存储dom对象到xml文件
	public static void writeDom(File xmlFile, Document dom) throws Exception {
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer();
		tf.transform(new DOMSource(dom), new StreamResult(xmlFile));
	}
}
