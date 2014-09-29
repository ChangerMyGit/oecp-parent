/**
 * 
 */
package oecp.framework.util.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 把一个对象数据输出到excel当中(excel生成在指定的路径下面)
 * 注意：此处支持一个excel生成多个sheet的情况，每个sheet对应一个entity
 * @author YangTao
 * @date 2012-8-9
 * @version 1.0
 */
public class Entity2Excel<E> {

	/**
	 * 支持一个excel文件里面根据多个数据集合生成多个sheet
	 * 
	 * @author YangTao
	 * @date 2012-8-9
	 * @outputFileName 生成excel文件放在硬盘上指定的路径下面
	 * @response 生成excel文件直接写入response流里面下载
	 * @list 多个sheet的配置
	 */
	public void entity2Excel(String outputFileName,HttpServletResponse response,List<Entity2SheetConfig> list) throws Exception {
		OutputStream out = null;
		if(StringUtils.isNotBlank(outputFileName)){//将生成excel文件放在指定文件路径下面
			//递归创建文件夹
			this.createFileDir(outputFileName.substring(0, outputFileName.lastIndexOf("\\")));
			File file = new File(outputFileName);
			if (!file.exists())
				file.createNewFile();
			out = new FileOutputStream(file);
		}else{//直接将excel文件下载
			response.setContentType ("application/ms-excel") ;  
			response.setHeader ("Content-Disposition" , "attachment;filename="+new String("导出Excel.xls".getBytes(),"iso-8859-1"));  
			out = response.getOutputStream();
		}
		
		try {
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 根据多个ExcelSheetConfig创建多个sheet
			for (Entity2SheetConfig<E> eec : list) {
				entity2ExcelSheet(workbook, eec, "yyyy-MM-dd");
			}
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	/**
	 * 生成每个sheet及数据
	 * 
	 * @author YangTao
	 * @date 2012-8-9
	 * @desc
	 */
	public void entity2ExcelSheet(HSSFWorkbook workbook,
			Entity2SheetConfig<E> eec, String pattern) throws Exception {
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(eec.getTitle());
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < eec.getHeadTitles().length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(
					eec.getHeadTitles()[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		if(eec.getDataset()!=null){
			Iterator it = eec.getDataset().iterator();
			int index = 0;
			while (it.hasNext()) {
				index++;
				row = sheet.createRow(index);
				// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
				E t = (E) it.next();
				for (short i = 0; i < eec.getHead2Field().length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style2);
					String fieldName = eec.getHead2Field()[i];
					try {
						Object value = "";
						Class tCls = t.getClass();
						//取数据时，根据数据载体不同，进行不同取值操作
						String getMethodName = null;
						if(eec.getEntity2SheetType()==Entity2SheetType.Entity){
							getMethodName = "get"+ fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
							Method getMethod = tCls.getMethod(getMethodName,
									new Class[] {});
							value = getMethod.invoke(t, new Object[] {});
						}else if(eec.getEntity2SheetType()==Entity2SheetType.SimpleDataVo){
							getMethodName = "getValue";
							Method getMethod = tCls.getMethod(getMethodName,
									new Class[] {String.class});
							value = getMethod.invoke(t, new Object[] {fieldName});
						}
						
						// 判断值的类型后进行强制类型转换
						String textValue = null;
						if (value instanceof Boolean) {
							boolean bValue = (Boolean) value;
							if (bValue)
								textValue = "true";
							else
								textValue = "false";
							
						} else if (value instanceof Date) {
							Date date = (Date) value;
							SimpleDateFormat sdf = new SimpleDateFormat(pattern);
							textValue = sdf.format(date);
						} else {
							// 其它数据类型都当作字符串简单处理
							textValue = value==null?null:value.toString();
						}
						// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
						if (textValue != null) {
							Pattern p = Pattern.compile("^//d+(//.//d+)?$");
							Matcher matcher = p.matcher(textValue);
							if (matcher.matches()) {
								// 是数字当作double处理
								cell.setCellValue(Double.parseDouble(textValue));
							} else {
								HSSFRichTextString richString = new HSSFRichTextString(
										textValue);
								HSSFFont font3 = workbook.createFont();
								font3.setColor(HSSFColor.BLUE.index);
								richString.applyFont(font3);
								cell.setCellValue(richString);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
				}
			}
		}
	}
	
	//递归创建文件夹
	private void createFileDir(String mkdirName){
		File  dirFile = new File(mkdirName);//mkdirName为传建文件夹路径
        if(dirFile.exists()){
           
        }else{
	       	File parentFile = dirFile.getParentFile();
	       	createFileDir(parentFile.getAbsolutePath());
	       	dirFile.mkdir();
        }
	}
}
