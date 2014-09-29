package oecp.framework.util.image;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

/**
 * 图片缩小接口
 * 
 * @author liufang
 * 
 */
public interface ImageScale {
	/**
	 * 缩小图片
	 * 
	 * @param srcFile
	 *            原图片
	 * @param destFile
	 *            目标图片
	 * @param boxWidth
	 *            缩略图最大宽度
	 * @param boxHeight
	 *            缩略图最大高度
	 * @throws IOException
	 */
	public void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight) throws Exception;

	/**
	 * 按最大边进行等比缩放图片后裁剪图片
	 * 
	 * @param srcFile
	 *            原文件
	 * @param destFile
	 *            目标文件
	 * @param boxWidth
	 *            缩略图最大宽度
	 * @param boxHeight
	 *            缩略图最大高度
	 * @param cutTop
	 *            裁剪TOP
	 * @param cutLeft
	 *            裁剪LEFT
	 * @param cutWidth
	 *            裁剪宽度
	 * @param catHeight
	 *            裁剪高度
	 * @throws IOException
	 */
	public void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight, int cutTop, int cutLeft, int cutWidth, int catHeight)
			throws Exception;
	
	/**
	 * 生成加水印后的图片，水印本身为文字 hailang注释方法含义
	 * @param srcFile   源图片文件
	 * @param destFile  目标图片文件
	 * @param minWidth  图片最小宽度 ，定义判断是否加入水印图片的边界下限
	 * @param minHeight 图片最小高度，定义判断是否加入水印图片的边界上限
	 * @param pos       水印位置  0-5
	 * @param offsetX   水平偏移量
	 * @param offsetY    垂直偏移量
	 * @param text       水印文字
	 * @param color      水印文字颜色对象
	 * @param size       水印文字大小
	 * @param alpha      水印文字透明度
	 * @throws Exception
	 */
	public void imageMark(File srcFile, File destFile, int minWidth,
			int minHeight, int pos, int offsetX, int offsetY, String text,
			Color color, int size, int alpha) throws Exception;
	
	/**
	 * 生成加水印后的图片，水印本身为图片 hailang注释方法含义
	 * @param srcFile   源图片文件
	 * @param destFile  目标图片文件
	 * @param minWidth  图片最小宽度 ，定义判断是否加入水印图片的边界下限
	 * @param minHeight 图片最小高度，定义判断是否加入水印图片的边界上限
	 * @param pos       水印位置  0-5
	 * @param offsetX   水平偏移量
	 * @param offsetY    垂直偏移量
	 * @param markFile   水印图片文件
	 * @throws Exception  异常
	 */
	public void imageMark(File srcFile, File destFile, int minWidth,
			int minHeight, int pos, int offsetX, int offsetY, File markFile)
			throws Exception;
	
	/**
	 * 
	 * @param srcFile   原文件
	 * @param destFile   目标文件
	 * @param pos    水印位置 
	 * @param markFile 水印图片
	 * @param alpha 透明度
	 */
	public void imageMark(File srcFile,File destFile, String pos,File markFile,float alpha) throws Exception;
	
	
	/**
	 * 按最小边进行等比缩放图片后裁剪图片
	 * 
	 * @param srcFile
	 *            原文件
	 * @param destFile
	 *            目标文件
	 * @param boxWidth
	 *            缩略图最大宽度
	 * @param boxHeight
	 *            缩略图最大高度
	 * @param cutTop
	 *            裁剪TOP
	 * @param cutLeft
	 *            裁剪LEFT
	 * @param cutWidth
	 *            裁剪宽度
	 * @param catHeight
	 *            裁剪高度
	 * @throws IOException
	 */
	public void resizeCutFix(File srcFile, File destFile, int boxWidth,
			int boxHeight)
			throws Exception;
}
