 
package org.arong.util.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import jxl.Cell;
import jxl.CellType;
import jxl.Range;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * @author huangdos
 * excel内部方法实现
 */
@SuppressWarnings("rawtypes")
public class ExcelCommonMethod {

	public File exportExcelByModulex1ToSheet(HashMap dataMap, String outExcelName,
			String url){
		File f = new File(outExcelName);//模板路径
		
		return f;
	}
	
	public File exportExcelByModulex11(HashMap dataMap, String outExcelName,
			String url) {
		File f = new File(outExcelName);//模板路径
		try {
			Workbook workbook = Workbook.getWorkbook(new File(url));//获取操作Workbook
			WritableWorkbook excelbook = Workbook.createWorkbook(f, workbook);//编辑模板
			WritableSheet sheet2 = excelbook.getSheet(0);
			int controlNo = 0;
			int startNo = -1;
			String mapString = "";
			for (int r = 0; r < sheet2.getRows(); r++) {
				for (int c = 0; c < sheet2.getColumns(); c++) {
					//	System.out.println(" (r,c): -- "+"(r="+r+" c="+c+")");
					WritableCell cell = sheet2.getWritableCell(c, r);//可编辑单元格
					if (cell.getType() == CellType.EMPTY)
						continue;
					Label lb = (Label) cell;
					String signName = lb.getString().toUpperCase().trim();
					//	System.out.println("-signName- > : "+signName);
					if (dataMap.containsKey(signName)
							&& signName.indexOf("AGGREGATE_START") > -1) {
						controlNo++;
						startNo = r;
						mapString = signName;
					} else if (dataMap.containsKey(signName)) {
						controlNo++;
						lb.setString(dataMap.get(signName).toString());
					}
					if (controlNo == dataMap.size()) {
						r = sheet2.getRows();
						c = sheet2.getColumns();
					} else if (cell.getContents().equals("[ORVER]")) {
						r = sheet2.getRows();
						c = sheet2.getColumns();
						lb.setString("");
					}
				}
			}
			ArrayList ma = (ArrayList) dataMap.get(mapString);
			if (ma != null && ma.size() > 0 && !mapString.equals(""))
				sheet2 = this.autoAddRowExcel2_XX(sheet2, ma, startNo, 5);
			else if (startNo > -1)
				sheet2.removeRow(startNo);
			excelbook.write();
			excelbook.close();
		} catch (Exception e) {
			throw new RuntimeException("写入文件失败。", e);
		} finally {
			//System.out.println("导出完成---Excel");
		}
		return f;
	}
	public WritableSheet autoAddRowExcel2_XX(WritableSheet sheet,
			ArrayList rowsData, int standardIndex, int modelRows) {
		for (int j = 0; j < rowsData.size(); j++) {
			for (int c = 1; c < modelRows + 1; c++) {
				sheet.insertRow(standardIndex + c - 1 + (j + 1) * modelRows);
				HashMap rowData = (HashMap) rowsData.get(j);//行数据 。
				String cellContent = "";
				for (int i = 0; i < sheet.getColumns(); i++) {
					CellFormat fmtPt = sheet.getCell(i, standardIndex)
							.getCellFormat();
					jxl.write.WritableCellFormat wcsB = new jxl.write.WritableCellFormat();
					try {
						wcsB.setBorder(jxl.format.Border.ALL,
								jxl.format.BorderLineStyle.THIN);
						Label lb = new Label(i, standardIndex+ c-1 +(j+1)* modelRows, cellContent, wcsB);
						sheet.addCell(lb);
					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}
				}
			}
		}

	//	System.out.println("---区域合并元素个数： -----"+ this.getRangeInFiled(mergedCell, standardIndex, 2).length);
		Range[] mergedCell = this.getRangeInFiled(sheet, standardIndex, modelRows);
		for (int j = 0; j < rowsData.size(); j++) {
			for (int r = 0; r < mergedCell.length; r++) {
				Range rangeCell = mergedCell[r];
				Cell cellBR = rangeCell.getBottomRight();
				Cell cellTL = rangeCell.getTopLeft();
				int colBR = cellBR.getColumn();
				int rowBR = cellBR.getRow() + (j + 1) * modelRows;
				int colTL = cellTL.getColumn();
				int rowTL = cellTL.getRow() + (j + 1) * modelRows;
				Cell rangeCellX = sheet.getCell(colBR - 1, rowTL + 1);
				try {
					sheet.mergeCells(colTL, rowTL, colBR, rowBR);
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}
			}
			//System.out.println(" --->>>>>>>>>>>>>>>>>>>>>>>>>>>>  格式化开始 --");
			for(int f=1;f<modelRows+1;f++){
				int rowIndexStard= standardIndex+f-1;
				for(int cc=0;cc<sheet.getRow(rowIndexStard).length;cc++){
					WritableCell cell = sheet.getWritableCell(cc,rowIndexStard);
					String cellContent = "n-o";
					if (cell.getType() == CellType.EMPTY) {
						//标准行为空时，继续执行。
						//System.out.println("-----  EMPTY  ---------------------------");
						continue;
					} else {
						String key = this.getNormalValueByClear(((Label) cell)
								.getString().toUpperCase(), "@");
						cellContent ="have";
					}
					CellFormat fmtPt = cell.getCellFormat();
					WritableCell cellNew = sheet.getWritableCell(cc,rowIndexStard+(j+1)*modelRows);
					Label lb = (Label) cellNew;
					lb.setString(cell.getContents());
					lb.setCellFormat(fmtPt);
				}
				try {
					sheet.setRowView(rowIndexStard+modelRows*(j+1),(sheet.getRowHeight(rowIndexStard)));
				} catch (RowsExceededException e) {
					e.printStackTrace();
				}
			}
		}
		//        for(int i=0;i<modelRows;i++)
		//        {
		//        	sheet.removeRow(standardIndex+i);
		//        }
		return sheet;
	}
	public Range[] getRangeInFiled(WritableSheet sheet , int startedIndex,
			int rowSpan) {
		Range[] changeRange=sheet.getMergedCells();
		Range[] result = new Range[200];
		
		int r = 0;
		for (int i = 0; i < changeRange.length; i++) {
			Range mergerdCell = changeRange[i];
			Cell cellBR = mergerdCell.getBottomRight();
			//System.out.println("BottomRightrow=="+cellBR.getRow()+"   BottomRightcolumon=="+cellBR.getColumn()+"  content=="+cellBR.getContents().toString());
			Cell cellTL = mergerdCell.getTopLeft();
			//System.out.println("TopLeftrow=="+cellTL.getRow()+"   TopLeftcolumon=="+cellTL.getColumn()+"   content=="+cellTL.getContents().toString());
			int rowBR = cellBR.getRow();
			int rowTL = cellTL.getRow();
			if (startedIndex - 1 < rowTL && rowTL < startedIndex + rowSpan) {
				result[r] = changeRange[i];
				r++;
			}
		}
		Range[] returnResult = new Range[r];
		for (int i = 0; i < r; i++) {
			returnResult[i] = result[i];
		}
		return returnResult;

	}

	/**
	 * @功能 ： 判断是否符合特定的格式
	 * @param estimateString
	 * @param identifier
	 * @return
	 */
	private boolean isFormula(String estimateString, String identifier) {
		if (estimateString == null || estimateString.trim().length() < 3)
			return false;
		else if (estimateString.startsWith(identifier)
				&& estimateString.endsWith(identifier))
			return true;
		return false;
	}

	/**
	 * @功能 ： 去掉格式符.
	 * @param preStringValue
	 * @param identifier
	 * @return
	 */
	private String getNormalValueByClear(String preStringValue,
			String identifier) {
		preStringValue = preStringValue.substring(1,
				preStringValue.length() - 1).toUpperCase().trim();
		return preStringValue;
	}
	
/**
 * 
 *-------------------------------------------------------------------------------------------------
 */
	public Range[] getRangeInFiled(Range[] changeRange, int startedIndex,int startedColumn,
			int rowSpan,int colSpan) {
		Range[] result = new Range[50];
		int r = 0;
		for (int i = 0; i < changeRange.length; i++) {
			Range mergerdCell = changeRange[i];
			Cell cellBR = mergerdCell.getBottomRight();
			Cell cellTL = mergerdCell.getTopLeft();
			int rowBR = cellBR.getRow();
			int rowTL = cellTL.getRow();
			int colTL=cellTL.getColumn();
			if (startedIndex - 1 < rowTL && rowTL < startedIndex + rowSpan) {
				 if(startedColumn-1<colTL&&colTL<startedColumn+colSpan) {
				 	result[r] = changeRange[i];
					r++;
				}	
			}
		}
		Range[] returnResult = new Range[r];
		for (int i = 0; i < r; i++) {
			returnResult[i] = result[i];
		}
		//System.out.println("------------r r r r r r r---------->>> : "+r);
		return returnResult;
	}
//	public static void main(String[] x) {
//		ExcelCommonMethod xx = new ExcelCommonMethod();
//		HashMap data = new HashMap();
//		ArrayList subData = new ArrayList();
//		HashMap data1 = new HashMap();
//		data1.put("MONEY_MAIN", "data---1");
//		data1.put("XJ", "data--XXX-1");
//		data1.put("ORDER_NBR", "#ORDER_NBR 哈哈");
//		HashMap data2 = new HashMap();
//		subData.add(data1);
//
//		data.put("AGGREGATE_START", subData);
//		xx.exportExcelByModulex11(data1,"D:/test.xls", "d:/ICT任务单.xls");
//	}
	public File exportExcelByModulex1( String outExcelName,
			String url) {
		File f = new File(outExcelName);//输出文件
		try {
			Workbook workbook = Workbook.getWorkbook(new File(url));//获取模板
			WritableWorkbook excelbook = Workbook.createWorkbook(f, workbook);//编辑模板
			WritableSheet sheet2 = excelbook.getSheet(0);//编辑第一个Sheet
	
			//sheet2 = this.copyExcelRows(sheet2,12,10,2,2);
			sheet2 = this.copyExcelRows(sheet2,12,10,2,1);
			excelbook.write();
			excelbook.close();
		} catch (Exception e) {
			throw new RuntimeException("写入文件失败。", e);
		} finally {
			//System.out.println("导出完成---Excel");
		}
		return f;
	}
	/**
	 * 
	 * @param sheet  所要编辑Sheet
	 * @param toPositionRow  从那一行开始循环插入
	 * @param standardIndex   插入那一行的样式开始
	 * @param modelRows   共几行
	 * @param copyTimes
	 * @return
	 */
	public WritableSheet copyExcelRows(WritableSheet sheet,
			int toPositionRow, int standardIndex, int modelRows,int copyTimes) {
		for (int j = 0; j < copyTimes; j++) {
			for (int c = 1; c < modelRows + 1; c++) {
				int ss= c - 1 +toPositionRow +(j) * modelRows;
				sheet.insertRow( c - 1 +toPositionRow +(j) * modelRows);//每次循环从第几行插入--复制的行数*数据的条数
				for (int i = 0; i < sheet.getColumns(); i++) {
					jxl.write.WritableCellFormat wcsB = new jxl.write.WritableCellFormat();
					try {
						wcsB.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
						Label lb = new Label(i,  c-1 +toPositionRow+(j)* modelRows,"", wcsB);
						sheet.addCell(lb);
					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}
				}
			}
		}
		Range[] mergedCell = this.getRangeInFiled(sheet, standardIndex, modelRows);
		for (int j = 0; j < copyTimes; j++) {
			for (int r = 0; r < mergedCell.length; r++) {
				Range rangeCell = mergedCell[r];
				Cell cellBR = rangeCell.getBottomRight();
				Cell cellTL = rangeCell.getTopLeft();
				int colBR = cellBR.getColumn();
				int rowBR = cellBR.getRow() + (j) * modelRows+toPositionRow-standardIndex;
				int colTL = cellTL.getColumn();
				int rowTL = cellTL.getRow() + (j) * modelRows+toPositionRow-standardIndex;
				try {
					sheet.mergeCells(colTL, rowTL, colBR, rowBR);
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}
			}
			for(int f=1;f<modelRows+1;f++){
				int rowIndexStard= standardIndex+f-1;
				int rowFormatIndex=toPositionRow+f-1;
				for(int cc=0;cc<sheet.getRow(rowIndexStard).length;cc++){
					WritableCell cell = sheet.getWritableCell(cc,rowIndexStard);
					WritableCell cellNew = sheet.getWritableCell(cc,rowFormatIndex+(j)*modelRows);
					Label lb = (Label) cellNew;	 
					String content=cell.getContents().toString();
//					String middle=content.length()>2?cell.getContents().substring(1,content.length()-1):content;
//					String frist=content.length()>2?cell.getContents().substring(0,1):"";
//					String end=content.length()>2?cell.getContents().substring(content.length()-1,content.length()):"";
					lb.setString(content);
					lb.setCellFormat(cell.getCellFormat());
				}
				try {
					sheet.setRowView(rowFormatIndex+modelRows*(j),(sheet.getRowHeight(rowIndexStard)));
				} catch (RowsExceededException e) {
					e.printStackTrace();
				}
			}
		}
		return sheet;
	}
	
}
