package org.arong.util.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import com.bean.feedback;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
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
			Label labe1 = new Label(0, 0, "反馈人姓名");
			Label labe2 = new Label(1, 0, "联系方式");
			Label labe3 = new Label(2, 0, "评价类别");
			Label labe4 = new Label(3, 0, "事项分类");
			Label labe5 = new Label(4, 0, "对应部门");
			Label labe6 = new Label(5, 0, "事项对客户的影响程度");
			Label labe7 = new Label(6, 0, "事项导致的后果");
			Label labe8 = new Label(7, 0, "是否需要立即传递负责人");
			Label labe9 = new Label(8, 0, "事件描述");
			Label labe10 = new Label(9, 0, "上传证据");

			// 将生成的单元格添加到工作表中
			sheet.addCell(labe1);
			sheet.addCell(labe2);
			sheet.addCell(labe3);
			sheet.addCell(labe4);
			sheet.addCell(labe5);
			sheet.addCell(labe6);
			sheet.addCell(labe7);
			sheet.addCell(labe8);
			sheet.addCell(labe9);
			sheet.addCell(labe10);

			Iterator it = arlist.iterator();
			int i = 1;

			while (it.hasNext()) {
				// 通过迭代获得arlist里的MarkesData对象
				/*feedback temp = (feedback) it.next();

				Label label1 = new Label(0, i, temp.getUserName());
				Label label2 = new Label(1, i, temp.getTelephone());
				Label label3 = new Label(2, i, temp.getType());
				Label label4 = new Label(3, i, temp.getClassify());
				Label label5 = new Label(4, i, temp.getDepartment());
				Label label6 = new Label(5, i, temp.getLevel());
				Label label7 = new Label(6, i, temp.getSequent());
				Label label8 = new Label(7, i, temp.getIssend());
				Label label9 = new Label(8, i, temp.getDescribe());
				Label label10 = new Label(9, i, temp.getUploadImage());

				// 将生成的单元格添加到工作表中
				sheet.addCell(label1);
				sheet.addCell(label2);
				sheet.addCell(label3);
				sheet.addCell(label4);
				sheet.addCell(label5);
				sheet.addCell(label6);
				sheet.addCell(label7);
				sheet.addCell(label8);
				sheet.addCell(label9);
				sheet.addCell(label10);*/

				i++;
			}
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