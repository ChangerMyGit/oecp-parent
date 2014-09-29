/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelReader {
	public static List readExcel(String excelFileName) throws BiffException,
			IOException {
		// 创建输入流
		InputStream stream = new FileInputStream(excelFileName);
		return readExcel(stream);

	}

	public static List readExcel(File excelFile) throws BiffException,
			IOException {
		return readExcel(new FileInputStream(excelFile));
	}

	public static List readExcel(InputStream is) throws BiffException,
			IOException {
		// 创建一个list 用来存储读取的内容
		List list = new ArrayList();
		Workbook rwb = null;
		Cell cell = null;

		// 创建输入流

		// 获取Excel文件对象
		rwb = Workbook.getWorkbook(is);

		// 获取文件的指定工作表 默认的第一个
		Sheet sheet = rwb.getSheet(0);

		// 行数(表头的目录不需要，从1开始)
		for (int i = 1; i < sheet.getRows(); i++) {

			// 创建一个数组 用来存储每一列的值
			String[] str = new String[sheet.getColumns()];

			// 列数
			for (int j = 0; j < sheet.getColumns(); j++) {

				// 获取第i行，第j列的值
				cell = sheet.getCell(j, i);
				str[j] = cell.getContents();

			}
			// 把刚获取的列存入list
			list.add(str);
		}

		// 返回值集合
		return list;
	}

	public static void main(String[] args) throws BiffException, IOException {
		readExcel("C:\\test.xls");
	}
}