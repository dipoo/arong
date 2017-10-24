package org.arong.util.excel;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
//import com.bean.feedback;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class JxlExltool {

	static WritableWorkbook book = null;

	@SuppressWarnings("rawtypes")
	public static void OutputExcel(List arlist, String Path) {
		try {
			book = Workbook.createWorkbook(new File(Path));
			// 设置表名
			WritableSheet sheet = book.createSheet("反馈表", 0);
			// 生成表格题头
			Label labe1 = new Label(0, 0, "FULLNAME");
			Label labe2 = new Label(1, 0, "USERID");
			Label labe3 = new Label(2, 0, "DEPARTMENT");
			Label labe4 = new Label(3, 0, "LOGINTIME");
			Label labe5 = new Label(4, 0, "IP");

			// 将生成的单元格添加到工作表中
			sheet.addCell(labe1);
			sheet.addCell(labe2);
			sheet.addCell(labe3);
			sheet.addCell(labe4);
			sheet.addCell(labe5);

			Iterator it = arlist.iterator();
			int i = 1;

			/*while (it.hasNext()) {
				// 通过迭代获得arlist里的MarkesData对象
				feedback temp = (feedback) it.next();

				Label label1 = new Label(0, i, temp.getUserName());
				Label label2 = new Label(1, i, temp.getTelephone());
				Label label3 = new Label(2, i, temp.getType());
				Label label4 = new Label(3, i, temp.getClassify());
				Label label5 = new Label(4, i, temp.getDepartment());
			

				// 将生成的单元格添加到工作表中
				sheet.addCell(label1);
				sheet.addCell(label2);
				sheet.addCell(label3);
				sheet.addCell(label4);
				sheet.addCell(label5);
				

				i++;
			}*/
			book.write();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (book != null)
					book.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out
						.println("exception when closing Connection in finally");
			}
		}
	}
}