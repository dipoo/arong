package org.arong.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public  class  ListHandler <T> extends DefaultHandler {
	private T T = null;
	
	public List<T> getList() {
		return list;
	}
	private List<T> list = null;
	@SuppressWarnings("unused")
	private String name = null;
	public void startDocument() throws SAXException {
		super.startDocument();
		list = new ArrayList<T>();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		this.name = qName;
		if("T".equals(qName)){
//			T = new T();
//			T.setId(attributes.getValue(0));
		}
	}
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		
//		if("name".equals(this.name)){
//			T.setName(new String(ch, start, length));
//		}
//		if("info".equals(this.name)){
//			T.setInfo(new String(ch, start, length));
//		}
//		if("dT".equals(this.name)){
//			T.setDownloadTimes(new String(ch, start, length));
//		}
//		if("uD".equals(this.name)){
//			T.setUpdateDate(new String(ch, start, length));
//		}
	}
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		this.name = null;
		if("T".equals(qName)){
			list.add(this.T);
			T = null;
		}
	}
}
