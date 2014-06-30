package org.arong.util.excel;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/**
 * 基于poi的排班数据导入
 * @author Administrator
 *
 */
public class ImportDemo {
	public static void main(String[] args) {
		
	}
	
	/**
	 * 导入数据
	 */
	public Map saveImportXsl(InputStream in, Map map) {
		String month  = map.get("month") == null ? new SimpleDateFormat("yyyy-MM").format(new Date()) : map.get("month").toString();
		
		Map retMap = new HashMap();
		
		List<Map> retList = new ArrayList();
		// 非空字段集合
		Set notNullFieldSet = new HashSet();
		notNullFieldSet.add("姓名");
		for(int i = 1; i <= 31; i ++){
			notNullFieldSet.add("" + i);
		}
		HSSFWorkbook hssfWorkBook;
		try {
			hssfWorkBook = new HSSFWorkbook(in);
			HSSFSheet sheet = hssfWorkBook.getSheetAt(0);
			int rowCount = sheet.getLastRowNum();// 总行数
			for (int i = 1; i <= rowCount; i++) {
				HSSFRow row = sheet.getRow(i);// 获取行
				if (row == null) {
					continue;
				}
				Map<String, String> rowMap = new HashMap<String, String>();
				int columnCount = row.getPhysicalNumberOfCells();// 总列数
				for (int k = 0; k < columnCount; k++) { // 遍历列
					HSSFCell cellHead = sheet.getRow(1).getCell((short) k);// 获取头，从第二行开始
					if (cellHead == null) {
						continue;
					}
					// 实际数据信息
					HSSFCell hssfCell = row.getCell((short) k);
					String cellContent = "";
					if (hssfCell != null) {
						if (hssfCell.getCellType() == hssfCell.CELL_TYPE_FORMULA) {
							cellContent =  String.valueOf(hssfCell.getBooleanCellValue());
						} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
							cellContent = String.valueOf(hssfCell.getNumericCellValue()).replaceAll(
									"E10", "").replaceAll("\\.", "").trim();
						} else {
							cellContent = hssfCell.getStringCellValue().trim();
						}
					}

					String cellHeadName = cellHead.getStringCellValue().trim();
					rowMap.put(cellHeadName, cellContent);
				}
				retList.add(rowMap);
			}
			in.close();
			System.out.println(retList);
			return null;
		} catch (IOException e) {
			retMap.put("success", "false");
			retMap.put("msg", "以xsl方式打开文件失败,请按照模板来构造数据");
		}
		return null;
	}
}
