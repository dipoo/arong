package org.arong.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * 概述： 所有已 imp 为开头的方法都是数据导入方法，所有以 exp为开头的都是数据导出方法。
 * 即：imp 是从excel中解析数据，并导入到内存中，  exp 是从内存中读取数据，写入到excel 文件
 * 所有可用方法都是静态方法,可以直接调用。 
 * 提供的接口，主要有简单的直接通过API 解析Excel 和复杂的根据模板来解析Excel 2类
 * 方法命名中 带有 templet 字眼的都是需要 Excel模板文件， 不带templet 字眼，或带Notemplet的都是无需模板的
 * 所有公共静态方法都可以使用， 其他方法基本上为内部使用
 * @author huangdos
 *
 */
@SuppressWarnings("rawtypes")
public class ExcelUitl {
	//单表，主数据的前后缀包括起来的数据
	public static final String mainDatePrefix="#";
	//从表： 数据字段的前后缀
	public static final String subDatePrefix="%";
	//文件结束标示 , 主要用在 导入模板中
	public static final String END="_END_";
	//行序号 -- 标明从表数据的开始行， 用在导入导出模板中
	public static final String ROW_NUM="%STRNUM%";// 表示输出数据的行数
	
	/**
	 *  替换一一对应的数据
	 * @param wtsh
	 * @param oneOne 模板与数据库字段对照集合
     * @param oneData  替换数据 ： Excel 中的主数据格式以： # # 包括起来的数据
	 * @return 返回添加数据后的WritableSheet
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	 private static WritableSheet updateReadOneCell(WritableSheet wtsh,Map oneData) throws RowsExceededException, WriteException{
//		Label lab;	
//		Set set=oneData.keySet();
//		Iterator it=set.iterator();
//		while(it.hasNext()){
//			String key=(String)it.next();
//			for(int i=0;i<wtsh.getRows();i++){
//				for(int j=0;j<wtsh.getColumns();j++){
//				Cell cell=wtsh.getCell(j,i);
//				String content=cell.getContents().toUpperCase().toString();
//				content=content.replaceAll(" ","");
//				String value=mainDatePrefix+key+mainDatePrefix;
//				if(value.equals(content)){
//		    		jxl.format.CellFormat cf=cell.getCellFormat();
//		    		Object con=oneData.get(key);
//		    		if(con!=null)
//		    			lab = new Label(cell.getColumn(), cell.getRow(),con.toString(),cf);
//		    		else
//		    			lab = new Label(cell.getColumn(), cell.getRow(),"",cf);
//		    		wtsh.addCell(lab);						
//				}
//				}
//			}		
//		}
//		return wtsh;
		// 优化成遍历 sheet, 再替换数据
		 Set set=oneData.keySet();
		 Map newdate=new HashMap();
		 Iterator<String> it=set.iterator();
		 while(it.hasNext()){
			 String key= it.next();
			 newdate.put(mainDatePrefix+key+mainDatePrefix, oneData.get(key));
		 }
		 int count=0; //替换的数据个数
		 int cellnum=0;
		for(int i=0;i<wtsh.getRows();i++){
			for(int j=0;j<wtsh.getColumns();j++){
				cellnum++;
			Cell cell=wtsh.getCell(j,i);
			String content=cell.getContents().toString();
			content=content.trim();
			if(content.length()>2&&content.startsWith(mainDatePrefix)){
				Object value= newdate.get(content);
				if(value!=null){
					count++;
					jxl.format.CellFormat cf=cell.getCellFormat();
					Label lab = new Label(cell.getColumn(), cell.getRow(),value.toString(),cf);
					wtsh.addCell(lab);	
				}
			}
			}
		}
		System.out.println("主表替换的数据个数为："+count+",单元格数量："+cellnum);
		return wtsh;
	}
	
	/**
	 * 根据模板 ，将数据替换后导出一个新的excel文件
	 * @deprecated
	 * @param data
	 *            数据集合
	 * @param filename
	 *            模版的在服务器上面的路径
	 * @param outFileUrl
	 *            自己指定输出的Excel文件的绝对路径
	 * @return 返回替换了模版数据之后的Excel 文档
	 * @throws IOException
	 */
	public File expDownExl2(List data, String filename,
			String outFileUrl) throws IOException {
		ExcelCommonMethod Excel = new ExcelCommonMethod();
		FileOutputStream fout = null;
		fout = new FileOutputStream(outFileUrl);
		int startNBR=0;
		try {
			FileInputStream fis = new FileInputStream(new File(filename));
			Workbook rwb = Workbook.getWorkbook(fis);
			Sheet sh = rwb.getSheet(0);
			WritableWorkbook wwb = Workbook.createWorkbook(fout, rwb);
			WritableSheet[] ws = wwb.getSheets();
			Map map=(Map)data.get(0);
			for(int i=0;i<ws.length;i++){
				if(map!=null)
					updateReadOneCell(ws[i],map);
					if(data.size()>0){
						String num=getRowNBR(ws[i]);
						if(!"".equals(num)){
							if(num.indexOf(":")>-1){//多个
								String[] nums=num.split(":");
								int numFlag=0;
								for( int x=0;x<nums.length;x++){
									numFlag=Integer.parseInt(nums[x])+(data.size()-1)*x;
									//addRows(ws[i],data,numFlag);
									//ws[i] = Excel.copyExcelRows(ws[i],numFlag,numFlag,1,data.size());
									if(x==0)
										ws[i] = Excel.copyExcelRows(ws[i],numFlag,numFlag-1,1,data.size()-1);
									if(x==1)
										ws[i] = Excel.copyExcelRows(ws[i],numFlag,numFlag-1,1,data.size()-1);
									addListValue(data,numFlag-1,1,ws[i]);
								}
							}else
							{//单个
								int numFlag=Integer.parseInt(num);
								//addRows(ws[i],data,numFlag);
								//ws[i] = Excel.copyExcelRows(ws[i],numFlag,numFlag,1,data.size());
								ws[i] = Excel.copyExcelRows(ws[i],numFlag,numFlag-1,1,data.size()-1);
								addListValue(data,numFlag-1,1,ws[i]);
							}
							//getReadMuchCell2(ws[i],data);
							//getReadMuchCell3(ws[i],data);
						}		
					}
			}
			wwb.write();
			wwb.close();
			fout.close();
			rwb.close();
			fis.close();
		} catch (Exception e) {
			throw new RuntimeException("导出文件失败", e);
		}
		return new File(outFileUrl);
	}
	//新增一行数据
	static void addListValue(List data,int startRow,int rangeMuch,WritableSheet doSheet){
		for(int i=0;i<data.size();i++){
			addValue((HashMap)data.get(i),startRow+rangeMuch*i,rangeMuch,doSheet,i+1);
		}
	}
	/**
	 * 新增一个数据
	 */
	static WritableSheet addValue(HashMap modelData,int startRow,int rangeMuch,WritableSheet doSheet,int strnum){
		for(int r=startRow;r<startRow+rangeMuch;r++){
			Cell[] cells=doSheet.getRow(r);
			for(int c=0;c<cells.length;c++){
				WritableCell cell = doSheet.getWritableCell(c,r);
				if(cell.getType()==CellType.EMPTY)continue;
				Label lb = (Label) cell;
				String signal=cell.getContents();
				if(signal.equals(ROW_NUM)){
					lb.setString(strnum+"");continue;
				}
				if(signal.length()>2) //去掉前后的 % 号
					signal=signal.substring(1,signal.length()-1);
				if(modelData.containsKey(signal)){
					signal=(modelData.get(signal)==null?"":modelData.get(signal).toString());
				}else signal=cell.getContents();
				lb.setString(signal);
			}
		}
		return doSheet;
	}
	
	/**
	 * @deprecated
	 * 替换一对多数据
	 * @param wtsh
	 * @param data
	 * @return
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public WritableSheet getReadMuchCell2(WritableSheet wtsh,List data) throws RowsExceededException, WriteException{
		Label lab;
			for(int i=0;i<wtsh.getRows();i++){
				for(int j=0;j<wtsh.getColumns();j++){
				Cell cell=wtsh.getCell(j,i);
				String content=cell.getContents();
				content=content.replaceAll(" ","");
		    	for(int y=0;y<data.size();y++){
		    		Map map=(Map)data.get(y);
		    		jxl.format.CellFormat cf=cell.getCellFormat();
		    		Set set=map.keySet();
		    		Iterator it=set.iterator();
		    		while(it.hasNext()){
		    			String key=(String)it.next();
		    			String keys=subDatePrefix+key+subDatePrefix;
		    			if(keys.equals(content)){
				    		Object con=map.get(key);
				    		if(con!=null)
				    			lab = new Label(cell.getColumn(), cell.getRow() + y,con.toString(),cf);
				    		else
				    			lab = new Label(cell.getColumn(), cell.getRow() + y,"",cf);
				    		wtsh.addCell(lab);			    				
		    			}		
		    		}
		    	}
			}		    		
		}
		return wtsh;
	}
	
	/**
	 * 获取行标
	 * @param wtsh
	 *  
	 * @return
	 */	
		private String getRowNBR(Sheet wtsh){		
			String num="";
			for(int i=0;i<wtsh.getRows();i++){
				Cell[] cell=wtsh.getRow(i);
				for(int j=0;j<cell.length;j++){
					String cont = cell[j].getContents().toString();
					cont = cont.replaceAll(" ", "");
					if(cont.equals(ROW_NUM)){
						int row=i+1;
						num+=row+":";//多个则叠加
					}
				}
			}
			if(num!=null && !"".equals(num))
			num=num.substring(0,num.length()-1);
			return num;
		}
		
		/**
		 * 获取第一个： ROW_NUM 对应的行号
		 * @param wtsh
		 * @return
		 */
		private static int getFirstRowNBR(Sheet wtsh){		
			int num=-1;
			for(int i=0;i<wtsh.getRows();i++){
				Cell[] cell=wtsh.getRow(i);
				for(int j=0;j<cell.length;j++){
					String cont = cell[j].getContents().toString();
					cont = cont.replaceAll(" ", "");
					if(cont.equals(ROW_NUM)){  //找到第一个，则返回
						int row=i+1;
						return row;
					}
				}
			}
			return num;
		}	
		
		
	/**
	 * @deprecated	
	 * @param dataMap
	 * @param outExcelName
	 * @param url
	 * @return
	 */
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
						String signName = lb.getString().trim();
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
//				ArrayList ma = (ArrayList) dataMap.get(mapString);
//				if (ma != null && ma.size() > 0 && !mapString.equals(""))
//					sheet2 = this.autoAddRowExcel2_XX(sheet2, ma, startNo, 5);
//				else if (startNo > -1)
//					sheet2.removeRow(startNo);
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
	 * 单个主表的数据替换，生成excel
	 * @param templetFile 模板文件
	 * @param mainData 主表 
	 * @param outFile  指定输出的excel文件全路径
	 * @return 返回输出文件对象
	 * @throws Exception
	 * @throws IOException
	 */
	public static File expMainFile(String templetFile,Map mainData,String outFile) throws Exception, IOException{
		File f=new File(outFile); //输出的文件
		Workbook workbook = Workbook.getWorkbook(new File(templetFile));//获取操作Workbook
		WritableWorkbook excelbook = Workbook.createWorkbook(f, workbook);//复制一个模板，并进行编辑模板
		WritableSheet wtsh = excelbook.getSheet(0);
		ExcelUitl.updateReadOneCell(wtsh, mainData); //更新 主信息
		excelbook.write();
		excelbook.close();
		return f;
	}
	
	
	/**
	 * 单个子表的多行数据替换 。 暂不考虑多个子表，只处理第一个 子表行(子表中有合并单元格需另外处理)
	 * @param templetFile 模板文件
	 * @param listdate 从表数据
	 * @param outFile 指定输出的excel文件全路径
	 * @return 返回输出文件对象
	 * @throws Exception
	 * @throws IOException
	 */
	public static File expListDateFile(String templetFile, List<Map> listdate,
			String outFile) throws Exception, IOException {
		File f = new File(outFile); // 输出的文件
		Workbook workbook = Workbook.getWorkbook(new File(templetFile));// 获取操作Workbook
		WritableWorkbook wwb = Workbook.createWorkbook(f, workbook);// 复制一个模板，并进行编辑模板
		ExcelCommonMethod Excel = new ExcelCommonMethod();
		WritableSheet[] ws = wwb.getSheets();
		int i = 0; // 只处理第一个sheet
		if (listdate.size() > 0) {
			int num = getFirstRowNBR(ws[i]);
			if (num > 0) {// 单个
				int numFlag = num;
				ws[i] = Excel.copyExcelRows(ws[i], numFlag, numFlag - 1, 1,
						listdate.size() - 1); // 复制 需插入的数据行，有多少数据，就复制多少行
				addListValue(listdate, numFlag - 1, 1, ws[i]);
			}
		}
		
		wwb.write();
		wwb.close();

		return f;
	}
	
	
	/**
	 * 在某 excel 文件上直接输出list数据 ，不建议使用
	 * 需要模板
	 * @deprecated
	 * @param listdate
	 * @param inFile 直接在模板文件上输出 列表数据，根据 %STRNUM% 找到起始行，然后根据Map中的键 进行值替换
	 * @return 返回输出文件对象
	 * @throws Exception
	 * @throws IOException
	 */
	public static File expListDateFileIntemplte(List<Map> listdate,
			String inFile) throws Exception, IOException {
		File f = new File(inFile); // 输出的文件
		Workbook workbook = Workbook.getWorkbook(new File(inFile));// 获取操作Workbook
		WritableWorkbook wwb = Workbook.createWorkbook(f,workbook);// 复制一个模板，并进行编辑模板
		ExcelCommonMethod Excel = new ExcelCommonMethod();
		WritableSheet[] ws = wwb.getSheets();
		int i = 0; // 只处理第一个sheet
		if (listdate.size() > 0) {
			int num = getFirstRowNBR(ws[i]);
			if (num > 0) {// 单个
				int numFlag = num;
				ws[i] = Excel.copyExcelRows(ws[i], numFlag, numFlag - 1, 1,
						listdate.size() - 1); // 复制 需插入的数据行，有多少数据，就复制多少行
				addListValue(listdate, numFlag - 1, 1, ws[i]);
			}
		}
		
		wwb.write();
		wwb.close();

		return f;
	}
	
	/**
	 * 根据模板输出Excel文件 ，一主多从的数据
	 * @param templetFile 模板文件
	 * @param mainData 主表数据
	 * @param listdate 从表数据,多个用数组按照输出先后顺序存入
	 * (顺序依次为：第一个sheet的第一个STRNUM,第一个sheet的第二个STRNUM ..，第2个sheet的第一个STRNUM..依次类推：直到第N个sheet的第M个STRNUM)
	 * 其中 listdate.length 需要是和所有的 STRNUM 的总数相同, 否则输出的数据只取这2个数的最小值
	 * @param outFile  指定输出到的文件
	 * @return 输出的文件对象
	 * @throws Exception
	 * @throws IOException
	 */
	public static File expMainAndSubDateFilebytemletFile(String templetFile,Map mainData, List<Map>[] listdate,
			String outFile) throws Exception, IOException{
		File f=new File(outFile); //输出的文件
		Workbook templetbook = Workbook.getWorkbook(new File(templetFile));//获取操作Workbook
		WritableWorkbook excelbook = Workbook.createWorkbook(f, templetbook);//复制一个模板，并进行编辑模板
		WritableSheet wtsh[] = excelbook.getSheets();  //如果有多页则，依次执行输出
		for(int i=0;i<wtsh.length;i++)
			ExcelUitl.updateReadOneCell(wtsh[i], mainData); //更新 主信息
		if(listdate==null){
			excelbook.write();
			excelbook.close();
			return f;
		}
		ExcelCommonMethod Excel = new ExcelCommonMethod();
		//更新从表信息
//		for(int i=0;i<listdate.length;i++){  //---输出模式：遍历每个sheet,将list中的第一个子表数据输出到每个sheet 的第一个数据中。 缺点：如果第二个sheet是另外一个从表，则会出问题。
//			List sub=listdate[i];
//			if(sub!=null&&sub.size()>0)
//			for(int t=0;t<wtsh.length;t++){ //逐个sheet 进行替换输出
//				WritableSheet ws=wtsh[t];
//				int num = getFirstRowNBR(ws);
//				if (num > 0) {// 单个
//					int numFlag = num;
//					ws = Excel.copyExcelRows(ws, numFlag, numFlag - 1, 1,
//							sub.size() - 1); // 复制 需插入的数据行，有多少数据，就复制多少行
//					addListValue(sub, numFlag - 1, 1, ws);
//				}
//				
//			}
//		}
//		
		//以子表的先后顺序进行输出: 先输出第一个sheet 中的数据，如果第二个sheet 还有需要输出，则输出第2个sheet。
		int len=listdate.length; //所有待输出的子表数据
		int i=0;
		for(int t=0;t<wtsh.length;t++){ //逐个sheet 进行替换输出
		WritableSheet ws=wtsh[t];
		int num = getFirstRowNBR(ws);
			while(num>0&&i<len){
				int numFlag = num;
				List sub=listdate[i];
				ws = Excel.copyExcelRows(ws, numFlag, numFlag - 1, 1,
						sub.size() - 1); // 复制 需插入的数据行，有多少数据，就复制多少行
				addListValue(sub, numFlag - 1, 1, ws);
				i++;//换下一个子表数据输出
				num = getFirstRowNBR(ws); //取当前sheet 的下一个子表输出
			}
		} 
		
		excelbook.write();
		excelbook.close();
		return f;
	}
	
	/**
	 *  只有一个主表和一个从表的导出，只支持第一个sheet的文件输出
	 * @param templetFile 模板文件  
	 * @param mainData 主表数据
	 * @param listdate 从表数据
	 * @param outFile 输出的文件路径
	 * @return 输出的excel文件路径对应的文件对象
	 * @throws Exception
	 * @throws IOException
	 */
	public static File expMainAndoneSubDateFilebytemletFile(String templetFile,Map mainData, List<Map> listdate,
			String outFile) throws Exception, IOException{
		List sub[]=new List[]{listdate};
		return expMainAndSubDateFilebytemletFile(templetFile,mainData,sub,outFile);
		
	}
//---------------------------- 下面 imp 主要是导入excel ，从excel 中读取数据----------
	/**
	 * 根据导入模板，自动读取数据。 
	 * 方法只支持读取单个List 数据简单数据行读取。 模板中只读取第一个sheet 中的数据
	 */
	public static List<Map> impListByTemletOneList(String templetFile, String impFile)throws Exception, IOException{
		return impListByTemletOneList(templetFile,new FileInputStream(impFile));
	}
	/**
	 * 根据导入模板，自动读取数据。 
	 * 方法只支持读取单个List 数据简单数据行读取。 模板中只读取第一个sheet 中的数据
	 */
	public static List<Map> impListByTemletOneList(String templetFile, InputStream impFile)throws Exception, IOException{
		List ls=new ArrayList();
		Workbook templetbook = Workbook.getWorkbook(new File(templetFile));//获取操作Workbook
		Sheet tmplets = templetbook.getSheet(0);
		int rownum= getFirstRowNBR(tmplets);
		int tmpendrow=getEndNBR(tmplets);

		Workbook imp = Workbook.getWorkbook(impFile);//获取操作Workbook
		Sheet impsh = imp.getSheet(0);
		int impend=getEndNBR(impsh);
//		System.out.println(tmpendrow+",rownum="+rownum+" , impend="+impend);
		int datesize=impend-tmpendrow+1;  //需要导入的最大行数(不含空行)
		//下面是数据读取： 按照模板中的%%关键字，每行生成一个Map对象
		//先获取Map 中的关键字顺序：
		Cell keys[]=tmplets.getRow(rownum-1);  //取出模板行
		String keylist[]=new String[keys.length];
		for(int i=0;i<keys.length;i++){
			String key = keys[i].getContents().trim();
//			System.out.println(key);
			if(key.length()>2&&key.startsWith(subDatePrefix))
				keylist[i]=(key.substring(1, key.length()-1));
		}
		//下面根据key 的顺序，从 imp 文件中读取数据
		for(int i=0;i<datesize;i++){
			Cell values[]= impsh.getRow(i+rownum-1);
			Map m=new HashMap();
			for(int j=0;j<values.length;j++){
				String value=values[j].getContents().trim();
				if(keylist[j]!=null){ //将数据设置进map 中
					m.put(keylist[j], value);
				}
//				System.out.println("i="+i+",j="+j+",value="+value);
			}
			ls.add(m);
		}
//		System.out.println(ls);
		templetbook.close();
		imp.close();
		impFile.close();
		return ls;
	}
	
	
	/**
	 * @deprecated
	 * 简单的 excel 列表数据解析，将一个列表的数据，按照指定的 单元格开始解析，得到待用的数据
	 * @param templetFile 模板文件
	 * @param impFile  待解析文件
	 * @param fistrow  非空字段标示 ，采用 %% 包括起来
	 * @return 如果 fistrow 指定的字段列的 某行为空的话，则不导入该行
	 * @throws Exception
	 * @throws IOException
	 */
	public static List<Map> impListByTemletOneList(String templetFile, String impFile,String fistrow)throws Exception, IOException{
		List ls=new ArrayList();
		Workbook templetbook = Workbook.getWorkbook(new File(templetFile));//获取操作Workbook
		Sheet tmplets = templetbook.getSheet(0);
		Map cellm= getCellNBR(tmplets,fistrow);
		int rownum= (Integer) cellm.get("row");
		int cellnum= (Integer) cellm.get("cell");

		Workbook imp = Workbook.getWorkbook(new File(impFile));//获取操作Workbook
		Sheet impsh = imp.getSheet(0);
		//下面是数据读取： 按照模板中的%%关键字，每行生成一个Map对象
		//先获取Map 中的关键字顺序：
		Cell keys[]=tmplets.getRow(rownum-1);  //取出模板行
		String keylist[]=new String[keys.length];
		for(int i=0;i<keys.length;i++){
			String key = keys[i].getContents().trim();
			if(key.length()>2&&key.startsWith(subDatePrefix))
				keylist[i]=(key.substring(1, key.length()-1));
		}
		//下面根据key 的顺序，从 imp 文件中读取数据
		 
		for(int i=rownum;i<impsh.getRows();i++){
			Cell values[]= impsh.getRow(i-1);
			Map m=new HashMap();
			// 关键字对应的行若为空则不保存数据
			if(values==null||values[cellnum].getContents().trim().equals(""))
				continue;
			for(int j=0;j<values.length;j++){
				String value=values[j].getContents().trim();
				if(keylist[j]!=null){ //将数据设置进map 中
					m.put(keylist[j], value);
				}
			}
			ls.add(m);
		}
//		System.out.println(ls);
		templetbook.close();
		imp.close();
		return ls;
	}
	/**
	 * 获取指定的 表格内容对应的所在 行号和列号
	 * @param wtsh
	 * @param fistrow
	 * @return Map： row 行号 ，cell 列号
	 */
	private static Map getCellNBR(Sheet wtsh,String fistrow){		
		int num=-1;
		Map m=new HashMap();
		for(int i=0;i<wtsh.getRows();i++){
			Cell[] cell=wtsh.getRow(i);
			for(int j=0;j<cell.length;j++){
				String cont = cell[j].getContents().toString();
				cont = cont.replaceAll(" ", "");
				if(cont.equals(fistrow)){  //找到第一个，则返回
					int row=i+1;
					m.put("row", row); // 行
					m.put("cell", j); // 列
					return m;
				}
			}
		}
		return m;
	}	
	
	
	/**
	 * 解析导入的excel, 读取一个主表，一个从表的数据 ，主表数据，直接存入 maindate参数，从表数据则通过方法返回值获取
	 * @param templetFile 导入模板
	 * @param maindate  主表数据
	 * @param impFile  导入的文件
	 * @return List<Map> 从表数据, 每行数据一个Map对象
	 * @throws Exception
	 * @throws IOException
	 */
	public static List<Map> imptByTemletForMainAndOneList(String templetFile, Map maindate,String impFile)throws Exception, IOException{
		return imptByTemletForMainAndOneList(templetFile, maindate,   new FileInputStream(impFile)); 
	}
	
	/**
	 *  解析导入的excel, 读取一个主表，一个从表的数据 ，主表数据，直接存入 maindate参数，从表数据则通过方法返回值获取
	 * @param templetFile
	 * @param maindate
	 * @param inputfile 输入文件流，用在 直接从上传的文件流中截取数据
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public static List<Map> imptByTemletForMainAndOneList(String templetFile, Map maindate,InputStream inputfile)throws Exception, IOException{
		List<Map> ls=new ArrayList<Map>();
		Workbook templetbook = Workbook.getWorkbook(new File(templetFile));//获取操作Workbook
		Sheet tmplets = templetbook.getSheet(0);
		int rownum= getFirstRowNBR(tmplets); //模板的子表起始行
		int tmpendrow=getEndNBR(tmplets);

		Workbook imp = Workbook.getWorkbook(inputfile);//获取操作Workbook
		Sheet impsh = imp.getSheet(0);
		int impend=getEndNBR(impsh);
		if(impend<1)
			throw new RuntimeException("导入的数据确实结尾标示，请确认导入文件的格式是否和模板一致");
		int datesize=impend-tmpendrow+1;  //需要导入的最大行数(不含空行)
		//下面是数据读取： 按照模板中的%%关键字，每行生成一个Map对象
		//先获取Map 中的关键字顺序：
		Cell keys[]=tmplets.getRow(rownum-1);  //取出模板行
		String keylist[]=new String[keys.length];
		for(int i=0;i<keys.length;i++){
			String key = keys[i].getContents().trim();
			if(key.length()>2&&key.startsWith(subDatePrefix))
				keylist[i]=(key.substring(1, key.length()-1));
		}
		//下面根据key 的顺序，从 imp 文件中读取数据
		for(int i=0;i<datesize;i++){
			Cell values[]= impsh.getRow(i+rownum-1);
			Map m=new HashMap();
			for(int j=0;j<values.length;j++){
				String value=values[j].getContents().trim();
				if(keylist[j]!=null){ //将数据设置进map 中
					m.put(keylist[j], value);
				}
			} 
			ls.add(m);
		}
		//下面读取主表：
		//先从模板中获取主表关键字的位置
		for(int i=0;i<tmplets.getRows();i++){
			Cell[] cell=tmplets.getRow(i); 
			for(int j=0;j<cell.length;j++){
				String cont = cell[j].getContents().trim();
				if(cont.length()>2&&cont.startsWith(mainDatePrefix)){  //找到## 包括的数据
					String key=cont.substring(1, cont.length()-1);
					// 根据位置直接获取 另外一个模板的数据 -- 如果主表中有字段的位置在从表的下面，则需要根据子表的数据条数依次下移
					String value="";
					if(i>=rownum)
						 value=impsh.getCell(j, i+datesize-1).getContents().trim();  
					else
						 value= impsh.getCell(j, i).getContents().trim();
//					System.out.println("key="+key+",value="+value);
					maindate.put(key, value);
				}
			}
		}
		
//		System.out.println(maindate);
		templetbook.close();
		imp.close();
		inputfile.close();
		return ls;
	}
	
	
	/**
	 * 只是根据模板导入主表
	 * @param templetFile  模板文件(不含从表,只有一个子表的数据导入)
	 * @param impFile  待导入解析的文件
	 * @return Map 主表数据，根据模板中定义的键值对进行解析
	 * @throws Exception
	 * @throws IOException
	 */
	public static Map imptByTemletForOnlyMain(String templetFile,String impFile)throws Exception, IOException{
		Map maindate=new HashMap();
		Workbook templetbook = Workbook.getWorkbook(new File(templetFile));//获取操作Workbook
		Sheet tmplets = templetbook.getSheet(0);
		Workbook imp = Workbook.getWorkbook(new File(impFile));//获取操作Workbook
		Sheet impsh = imp.getSheet(0);
	
		//下面读取主表：
		//先从模板中获取主表关键字的位置
		for(int i=0;i<tmplets.getRows();i++){
			Cell[] cell=tmplets.getRow(i); 
			for(int j=0;j<cell.length;j++){
				String cont = cell[j].getContents().trim();
				if(cont.length()>2&&cont.startsWith(mainDatePrefix)){  //找到## 包括的数据
					String key=cont.substring(1, cont.length()-1);
					// 根据位置直接获取 另外一个模板的数据 -- 如果主表中有字段的位置在从表的下面，则需要根据子表的数据条数依次下移
					String value= impsh.getCell(j, i).getContents().trim();
//					System.out.println("key="+key+",value="+value);
					maindate.put(key, value);
				}
			}
		}
		
//		System.out.println(maindate);
		templetbook.close();
		imp.close();
		return maindate;
	}
	
	/**
	 * @deprecated
	 * 导入的数据有一主多从的情况
	 * @param templetFile  模板文件
	 * @param maindate   主表
	 * @param impFile   待解析的导入文件
	 * @return  按照模板的先后顺序导出
	 * @throws Exception
	 * @throws IOException
	 */
	public static List[] imptByTemletForMainAndMoreList(String templetFile, Map maindate,String impFile)throws Exception, IOException{
		//TO DO
		return null;
	}
	
	
	public static int getEndNBR(Sheet wtsh){		
		int num=-1;
		for(int i=wtsh.getRows()-1;i>0;i--){
			Cell[] cell=wtsh.getRow(i);
			for(int j=0;j<cell.length;j++){
				String cont = cell[j].getContents().toString();
				cont = cont.trim();
				if(cont.equals(END)){  //找到第一个，则返回
					int row=i+1;
					return row;
				}
			}
		}
		return num;
	}	
	/**
	 * 根据列名，获取指定列的数据的数据
	 */
	private static List<String> getLieList(Sheet wtsh,String lie){		
		int lienum=-1;// 获取列名对应的 坐标
		int row=-1;
//		for(int i=wtsh.getRows()-1;i>0;i--){
//			Cell[] cell=wtsh.getRow(i);
//			for(int j=0;j<cell.length;j++){
//				String cont = cell[j].getContents().toString();
//				cont = cont.trim();
//				if(cont.equals(lie)){  //找到第一个，则返回
//					row=i;
//					lienum=j;
//					break;
//				}
//			}
//			if(row>0)
//				break;
//		}
		for(int i=0;i<wtsh.getRows();i++){
			Cell[] cell=wtsh.getRow(i);
			for(int j=0;j<cell.length;j++){
				String cont = cell[j].getContents().toString();
				cont = cont.trim();
				if(cont.equals(lie)){  //找到第一个，则返回
					row=i;
					lienum=j;
					break;
				}
			}
			if(row>=0&&lienum>=0)
				break;
		}
		
		if(lienum==-1){
			return null;
		}
		List<String> list=new ArrayList<String>(wtsh.getRows()-row+1);
		for(int i=row+1;i<wtsh.getRows();i++){ //从 标题对应行的，下一行开始获取数据
			Cell cell=wtsh.getCell(lienum,i);
			String data=cell.getContents().trim();
			//System.out.println("i="+i+",lie="+lienum+",date="+data);
			list.add(data);
		}
		
		return list;
	}	
}
