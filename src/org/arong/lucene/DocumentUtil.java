package org.arong.lucene;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Fieldable;
import org.arong.utils.DateUtil;

/**
 * @author arong
 * @version 1.1 2013-03-22
 */
public final class DocumentUtil {
	/**
	 * 将lucene的document文档转换成相应的pojo类 <br>
	 * 遵守一一对应原则
	 * 
	 * @param doc
	 * @return
	 */
	public static <T> T document2Pojo(Document doc,Class<T> clazz) {
		List<Fieldable> fields = doc.getFields();
		Integer fieldsLength = fields.size();
		String fieldName = null;
		T t = null;
		try {
			t = clazz.newInstance();
			Method[] methods = clazz.getDeclaredMethods();
			String methodName = null;
			String menberName = null;
			Integer methodsLength = methods.length;
			String fieldValue = null;
			for (int i = 0; i < fieldsLength; i++) {
				fieldName = fields.get(i).name();
				fieldValue = fields.get(i).stringValue();
				for (int j = 0; j < methodsLength; j++) {
					methodName = methods[j].getName();
					if (methodName.startsWith("set")) {
						menberName = Introspector.decapitalize(methodName
								.substring(3, methodName.length()));
						if (menberName.equals(fieldName)) {
							methods[j].invoke(
									t,
									typeConvertorForString(fieldValue,
											methods[j].getParameterTypes()[0]));
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 将一个pojo类转换为lucene的document文档<br>
	 * 遵守一一对应原则
	 * 
	 * @param t
	 * @return
	 */
	public static <T> Document pojo2Document(T t) {
		Method[] methods = t.getClass().getDeclaredMethods();
		Integer methodsLength = methods.length;
		String methodName = null;
		String menberName = null;
		Integer set = null;
		Document doc = new Document();
		try {
			for (int i = 0; i < methodsLength; i++) {
				methodName = methods[i].getName();
				if (methodName.startsWith("get") || methodName.startsWith("is")) {
					set = methodName.startsWith("get") ? 3 : 2;
					menberName = Introspector.decapitalize(methodName
							.substring(set, methodName.length()));
					doc.add(new Field(menberName, String.valueOf(methods[i].invoke(t)), Store.YES, Index.ANALYZED));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * String 类型转换
	 * 
	 * @param value
	 *            String值
	 * @param clazz
	 *            类型的字节码
	 * @return
	 */
	private static Object typeConvertorForString(String value, Class<?> clazz) {
		Object object = null;
		String typeName = clazz.getCanonicalName();
		if (typeName.equals("java.lang.Integer") || typeName.equals("int")) {
			object = Integer.parseInt(value);
		} else if (typeName.equals("java.lang.Long") || typeName.equals("long")) {
			object = Long.parseLong(value);
		} else if (typeName.equals("java.lang.Double")
				|| typeName.equals("double")) {
			object = Double.parseDouble(value);
		} else if (typeName.equals("java.lang.Float")
				|| typeName.equals("float")) {
			object = Float.parseFloat(value);
		} else if (typeName.equals("java.util.Date")) {
			object = DateUtil.String2Date(value);
		} else {
			object = value;
		}
		return object;
	}

}