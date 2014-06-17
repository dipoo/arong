/**
 * @{#}ExcelDataExpUtil.java
 * Copyright ZTESOFT Corporation. All rights reserved.
 * @author  XiaoLiming
 * History:Jun 27, 2011
 * version 1.0
 */
package org.arong.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;


/**
 * 导出
 * @author Administrator
 * 2012.11.17
 */
public class ExcelDataExpUtil {
	
	
	public static String encode(String s, String enc)throws Exception{
	    int caseDiff = 32;
	
	    BitSet dontNeedEncoding = new BitSet(256);
	
	    for (int i = 97; i <= 122; ++i)
	      dontNeedEncoding.set(i);
	
	    for (int i = 65; i <= 90; ++i)
	      dontNeedEncoding.set(i);
	
	    for (int i = 48; i <= 57; ++i)
	      dontNeedEncoding.set(i);
	
	    dontNeedEncoding.set(32);
	
	    dontNeedEncoding.set(45);
	    dontNeedEncoding.set(95);
	    dontNeedEncoding.set(46);
	    dontNeedEncoding.set(42);
	
	    boolean needToChange = false;
	    boolean wroteUnencodedChar = false;
	    int maxBytesPerChar = 10;
	    StringBuffer out = new StringBuffer(s.length());
	    ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);
	
	    OutputStreamWriter writer = new OutputStreamWriter(buf, enc);
	
	    for (int i = 0; i < s.length(); ++i) {
	      int c = s.charAt(i);
	
	      if (dontNeedEncoding.get(c)) {
	        if (c == 32) {
	          c = 43;
	          needToChange = true;
	        }
	
	        out.append((char)c);
	        wroteUnencodedChar = true;
	      }
	      else {
	        try {
	          if (wroteUnencodedChar) {
	            writer = new OutputStreamWriter(buf, enc);
	            wroteUnencodedChar = false;
	          }
	          writer.write(c);
	
	          if ((c >= 55296) && (c <= 56319) && 
	            (i + 1 < s.length())) {
	            int d = s.charAt(i + 1);
	
	            if ((d >= 56320) && (d <= 57343))
	            {
	              writer.write(d);
	              ++i;
	            }
	          }
	
	          writer.flush();
	        } catch (IOException e) {
	          buf.reset();
	        }
	        byte[] ba = buf.toByteArray();
	        for (int j = 0; j < ba.length; ++j) {
	          out.append('%');
	          char ch = Character.forDigit(ba[j] >> 4 & 0xF, 16);
	
	          if (Character.isLetter(ch))
	            ch = (char)(ch - caseDiff);
	
	          out.append(ch);
	          ch = Character.forDigit(ba[j] & 0xF, 16);
	          if (Character.isLetter(ch))
	            ch = (char)(ch - caseDiff);
	
	          out.append(ch);
	        }
	        buf.reset();
	        needToChange = true;
	      }
	    }
	
	    return ((needToChange) ? out.toString() : s);
  }
	
	
	/**
	 * 
	 * @param title 表头
	 * @param datalist  数据
	 * @param columns 行
	 * @return
	 * @throws Exception
	 */
	public HSSFWorkbook commExpExcel( List<String> title,
			List<Map<String, String>> datalist,String[] columns)
			throws Exception {
		return commExpExcel(title, datalist, columns, "");
	}
	
	/**
	 * 
	 * @param title 表头
	 * @param datalist  数据
	 * @param columns 行
	 * @param empty 值为空时单元格默认值
	 * @return
	 * @throws Exception
	 */
	public HSSFWorkbook commExpExcel( List<String> title,
			List<Map<String, String>> datalist,String[] columns, String empty)
			throws Exception {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet-1");

		HSSFRow row = null;
		HSSFCell cell = null;
		// 设置表格默认列宽度为15个字节
		sheet.autoSizeColumn((short) 1, true);
//		sheet.setDefaultColumnWidth((short) 20);
		sheet.setDisplayGridlines(false);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

//		// 生成并设置第三个样式
		HSSFCellStyle style3 = workbook.createCellStyle();
		style3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		for (short i = 0; i < title.size(); i++) {
			row = sheet.createRow(0);
			HSSFCell cell3 = row.createCell(i);
			cell3.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell3.setCellStyle(style3);
			cell3.setCellValue(title.get(i));
		}

		// 遍历集合数据，产生数据行
		Map<String, String> map = null;
		BeanUtilsBean beanUtil = BeanUtilsBean.getInstance();
		String colValue = "";
		
		if (datalist != null && datalist.size() > 0) {
			for (short index = 0; index < datalist.size(); index++) {
				row = sheet.createRow(index + 1);
				map = datalist.get(index);
				try {
					 //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
		            for (short i = 0; i < columns.length; i++) {
		               HSSFCell cell1 = row.createCell(i);
		               cell1.setEncoding(HSSFCell.ENCODING_UTF_16); 
		               cell1.setCellStyle(style2);
		               try {
		            	   //colValue = beanUtil.getProperty(map, columns[i]);             
		                   //如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
		                   if(map.get(columns[i])!=null && !"".equals(map.get(columns[i]))){
		                     /* if(colValue.length() >= 1500) {
		                    	  colValue = colValue.substring(0, 1500);
		                      }*/
		                	   HSSFRichTextString richString = new HSSFRichTextString(new StringBuffer(map.get(columns[i]).toString()).toString());
		                	   cell1.setCellValue(richString);
		                   } else {
		                	   HSSFRichTextString richString = new HSSFRichTextString(new StringBuffer(map.get(columns[i]) == null ? empty : map.get(columns[i]).toString()).toString());
		                	   cell1.setCellValue(richString);
		                   }
		               } catch (IllegalArgumentException e) {
		                   e.printStackTrace();
		               } finally {
		               }
		            }
				} catch (Exception e) {
					throw new Exception();
				}
			}
		}
         return workbook;
	}
	
	
	/**
	 * 
	 * @param title 表头
	 * @param datalist  数据
	 * @param columns 行
	 * @return
	 * @throws Exception
	 */
	public HSSFWorkbook commExpExcel( List<String> title,
			List<Map<String, String>> datalist,String[] columns,int width)
			throws Exception {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet-1");

		HSSFRow row = null;
		HSSFCell cell = null;
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) width);
		sheet.setDisplayGridlines(false);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

//		// 生成并设置第三个样式
		HSSFCellStyle style3 = workbook.createCellStyle();
		style3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		for (short i = 0; i < title.size(); i++) {
			row = sheet.createRow(0);
			HSSFCell cell3 = row.createCell(i);
			cell3.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell3.setCellStyle(style3);
			cell3.setCellValue(title.get(i));
		}

		// 遍历集合数据，产生数据行
		Map<String, String> map = null;
		BeanUtilsBean beanUtil = BeanUtilsBean.getInstance();
		String colValue = "";
		
		if (datalist != null && datalist.size() > 0) {
			for (short index = 0; index < datalist.size(); index++) {
				row = sheet.createRow(index+1);
				map = datalist.get(index);
				try {
					 //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
		            for (short i = 0; i < columns.length; i++) {
		               HSSFCell cell1 = row.createCell(i);
		               cell1.setEncoding(HSSFCell.ENCODING_UTF_16); 
		               cell1.setCellStyle(style2);
		               try {
		            	   colValue = beanUtil.getProperty(map, columns[i]);             
		                   //如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
		                   if(colValue!=null){
		                      cell1.setCellValue(colValue);
		                   }
		               } catch (NoSuchMethodException e) {
		                   e.printStackTrace();
		               } catch (IllegalArgumentException e) {
		                   e.printStackTrace();
		               } catch (IllegalAccessException e) {
		                   e.printStackTrace();
		               } catch (InvocationTargetException e) {
		                   e.printStackTrace();
		               } finally {
		               }
		            }
				} catch (Exception e) {
					throw new Exception();
				}
			}
		}
         return workbook;
	}
}