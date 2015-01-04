package org.arong.xml;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;

public class SAXUtil {
	//1.获取SAX解析器
		public static SAXParser getSaxParser() throws Exception{
			SAXParserFactory saxpf = SAXParserFactory.newInstance();
			return saxpf.newSAXParser();
		}
		//2.开始解析xml
		public static void parseXML(File xmlFile, DefaultHandler dh) throws Exception{
			SAXParser parser = getSaxParser();
			parser.parse(xmlFile, dh);
		}
}
