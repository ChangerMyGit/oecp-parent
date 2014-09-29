package oecp.framework.fs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import magick.CompositeOperator;
import magick.DrawInfo;
import magick.MagickException;
import magick.MagickImage;
import magick.PixelPacket;
import magick.ImageInfo;
import org.apache.commons.io.FileUtils;

import oecp.framework.fs.ImageUtils.Position;

/**
 * magick图片缩小方式
 * @author hailang添加注释
 *
 */
public class MagickImageScale {
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
	 * @throws MagickException
	 */
	public static void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight) throws IOException, MagickException {
		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);
		// 计算缩小宽高
		Dimension dim = image.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		} else {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		// 缩小
		MagickImage scaled = image.scaleImage(zoomWidth, zoomHeight);
		// 输出
		scaled.setFileName(destFile.getAbsolutePath());
		scaled.writeImage(info);
		scaled.destroyImages();
	}

	/**
	 * 裁剪并缩小
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
	 * @throws MagickException
	 */
	public static void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight, int cutTop, int cutLeft, int cutWidth, int catHeight)
			throws IOException, MagickException {
		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);
		// 剪切
		Rectangle rect = new Rectangle(cutTop, cutLeft, cutWidth, catHeight);
		// 计算压缩宽高
		MagickImage cropped = image.cropImage(rect);
		Dimension dim = cropped.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		} else {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		// 缩小
		MagickImage scaled = cropped.scaleImage(zoomWidth, zoomHeight);
		// 输出
		scaled.setFileName(destFile.getAbsolutePath());
		scaled.writeImage(info);
		scaled.destroyImages();
	}
	
	/**
	 * 按图片最小边，进行等比缩放图片后再进行剪裁操作
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
	 * @throws MagickException
	 */
	public static void resizeCutFix(File srcFile, File destFile, int boxWidth,
			int boxHeight, int cutTop, int cutLeft, int cutWidth, int catHeight)
			throws IOException, MagickException {
		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);
		// 剪切
		Rectangle rect = new Rectangle(cutTop, cutLeft, cutWidth, catHeight);
		// 计算压缩宽高
		MagickImage cropped = image.cropImage(rect);
		Dimension dim = cropped.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		} else {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		}
		// 缩小
		MagickImage scaled = cropped.scaleImage(zoomWidth, zoomHeight);
		// 输出
		scaled.setFileName(destFile.getAbsolutePath());
		scaled.writeImage(info);
		scaled.destroyImages();
	}
	/**
	 * 生成加水印后的图片，水印本身为文字 hailang注释方法含义
	 * @param srcFile   源图片文件
	 * @param destFile  目标图片文件
	 * @param minWidth  图片最小宽度 ，定义判断是否加入水印图片的边界下限
	 * @param minHeight 图片最小高度，定义判断是否加入水印图片的边界上限
	 * @param pos       水印位置  0-5
	 * @param offsetX   水平偏移量
	 * @param offsetY    垂直偏移量
	 * @param markContent 水印文字内容
	 * @param markColor 水印文字颜色对象
	 * @param markSize 水印文字大小
	 * @param alpha   水印文字透明度
	 * @throws IOException
	 * @throws MagickException
	 */
	public static void imageMark(File srcFile, File destFile, int minWidth,
			int minHeight, int pos, int offsetX, int offsetY,
			String markContent, Color markColor, int markSize, int alpha)
			throws IOException, MagickException {
		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);

		Dimension dim = image.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		if (width < minWidth || height < minHeight) {
			image.destroyImages();
			if (!srcFile.equals(destFile)) {
				FileUtils.copyFile(srcFile, destFile);
			}
		} else {
			imageMark(image, info, width, height, pos, offsetX, offsetY,
					markContent, markColor, markSize, alpha);
			image.setFileName(destFile.getAbsolutePath());
			image.writeImage(info);
			image.destroyImages();
		}
	}
	
	/**
	 * 生成加水印后的图片，水印本身为图片 hailang注释方法含义
	 * @param srcFile   源图片文件
	 * @param destFile  目标图片文件
	 * @param minWidth  图片最小宽度 ，定义判断是否加入水印图片的边界下限
	 * @param minHeight 图片最小高度，定义判断是否加入水印图片的边界上限
	 * @param pos       水印位置  0-5
	 * @param offsetX   水平偏移量
	 * @param offsetY    垂直偏移量
	 * @param markFile  水印文件
	 * @throws IOException
	 * @throws MagickException
	 */
	public static void imageMark(File srcFile, File destFile, int minWidth,
			int minHeight, int pos, int offsetX, int offsetY, File markFile)
			throws IOException, MagickException {
		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);

		Dimension dim = image.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		if (width < minWidth || height < minHeight) {
			image.destroyImages();
			if (!srcFile.equals(destFile)) {
				FileUtils.copyFile(srcFile, destFile);
			}
		} else {
			imageMark(image, info, width, height, pos, offsetX, offsetY,
					markFile);
			image.setFileName(destFile.getAbsolutePath());
			image.writeImage(info);
			image.destroyImages();
		}
	}

	private static void imageMark(MagickImage image, ImageInfo info, int width,
			int height, int pos, int offsetX, int offsetY, String text,
			Color color, int size, int alpha) throws MagickException {
		Position p = ImageUtils.markPosition(width, height, pos, offsetX,
				offsetY);
		DrawInfo draw = new DrawInfo(info);
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		draw.setFill(new PixelPacket(r * r, g * g, b * b,
				65535 - (alpha * 65535 / 100)));
		draw.setPointsize(size);
		draw.setTextAntialias(true);
		draw.setText(text);
		draw.setGeometry("+" + p.getX() + "+" + p.getY());
		image.annotateImage(draw);
	}

	private static void imageMark(MagickImage image, ImageInfo info, int width,
			int height, int pos, int offsetX, int offsetY, File markFile)
			throws MagickException {
		Position p = ImageUtils.markPosition(width, height, pos, offsetX,
				offsetY);
		MagickImage mark = new MagickImage(new ImageInfo(markFile
				.getAbsolutePath()));
		image.compositeImage(CompositeOperator.AtopCompositeOp, mark, p.getX(),
				p.getY());
		mark.destroyImages();
	}

	public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();
		MagickImageScale.resizeFix(
				new File("test/com/jeecms/common/util/1.bmp"), new File(
						"test/com/jeecms/common/util/1-n-3.bmp"), 310, 310, 50,
				50, 320, 320);
		time = System.currentTimeMillis() - time;
		System.out.println("resize new img in " + time + "ms");
	}
}
