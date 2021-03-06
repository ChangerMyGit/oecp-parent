package oecp.framework.fs;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import oecp.framework.fs.ImageUtils.Position;

import org.apache.commons.io.FileUtils;

/**
 * 图片缩小类。使用方型区域颜色平均算法
 * 
 * @author liufang
 * 已经转移到oecp-util
 */
@Deprecated
public class AverageImageScale {
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
	public static void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight) throws IOException {
/*	方法一：	
 * BufferedImage srcImgBuff = ImageIO.read(srcFile);
		int width = srcImgBuff.getWidth();
		int height = srcImgBuff.getHeight();
		if (width <= boxWidth && height <= boxHeight) {
			FileUtils.copyFile(srcFile, destFile);
			return;
		}
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		} else {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		BufferedImage imgBuff = scaleImage(srcImgBuff, width, height,
				zoomWidth, zoomHeight);
		writeFile(imgBuff, destFile);*/
	//方法2：
		BufferedImage imgBuff = ImageIO.read(srcFile);
		int width = imgBuff.getWidth();
		int height = imgBuff.getHeight();
		if (width <= boxWidth && height <= boxHeight) {
			FileUtils.copyFile(srcFile, destFile);
			return;
		}
		int zoomWidth;
		int zoomHeight;
		if((float) width / height > (float) boxWidth / boxHeight){
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		}else{
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		imgBuff=Thumbnails.of(imgBuff).size(zoomWidth, zoomHeight).keepAspectRatio(false).asBufferedImage();//按先等比缩放处理
		writeFile(imgBuff, destFile);
		
	}
	/**
	 * 按图片最小边，进行等比缩放图片后再进行剪裁操作
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
	public static void resizeCutFix(File srcFile, File destFile, int boxWidth,
			int boxHeight) throws IOException {
	/*BufferedImage srcImgBuff = ImageIO.read(srcFile);
		int width = srcImgBuff.getWidth();
		int height = srcImgBuff.getHeight();
		if (width <= boxWidth && height <= boxHeight) {
			writeFile(srcImgBuff, destFile);
			return;
		}
		int zoomWidth;
		int zoomHeight;
		boolean fixH=true;//是否固定高度
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;//高度固定
		} else {
			zoomWidth = boxWidth;//宽度固定
			zoomHeight = Math.round((float) boxWidth * height / width);
			fixH=false;
		}
		BufferedImage imgBuff = scaleImage(srcImgBuff, width, height,
				zoomWidth, zoomHeight);
		writeFile(imgBuff, new File("c:/333.jpg"));
//		缩略之后的图实际宽度和高度
//		imgBuff.getWidth();
//		imgBuff.getHeight();
//		cutTop 相当于图像的起点坐标x
//		cutLef 相当于图像的起点坐标y
		
		int cutTop;
		int cutLeft;
		if(fixH){//如果高度固定，已宽度为基准计算，从图片中间剪裁
			cutTop=Math.round((float)(imgBuff.getWidth()-boxWidth)/2);
			cutLeft=0;
		}else{//如果宽度固定,已高度为基准计算，从图片中间剪裁
			cutTop=0;
			cutLeft=Math.round((float)(imgBuff.getHeight()-boxHeight)/2);
		}
		imgBuff = imgBuff.getSubimage(cutTop, cutLeft, boxWidth,boxHeight);//剪裁图片
		
		writeFile(imgBuff, destFile);
		*/
		BufferedImage imgBuff = ImageIO.read(srcFile);
		int width = imgBuff.getWidth();
		int height = imgBuff.getHeight();
		if (width <= boxWidth && height <= boxHeight) {
			writeFile(imgBuff, destFile);
			return;
		}
		int cutTop;//代表宽度坐标
		int cutLeft;//代表高度坐标
		int zoomWidth;
		int zoomHeight;
		if((float) width / height > (float) boxWidth / boxHeight){
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;//高度固定
		}else{
			zoomWidth = boxWidth;//宽度固定
			zoomHeight = Math.round((float) boxWidth * height / width);
		}
		imgBuff=Thumbnails.of(imgBuff).size(zoomWidth, zoomHeight).keepAspectRatio(false).asBufferedImage();//按先等比缩放处理
		if ((float) width / height > (float) boxWidth / boxHeight) {//如果高度固定，已宽度为基准计算，从图片中间剪裁
			cutTop=Math.round((float)(imgBuff.getWidth()-boxWidth)/2);
			cutLeft=0;
		} else {//如果宽度固定,已高度为基准计算，从图片中间剪裁
			cutTop=0;
			cutLeft=Math.round((float)(imgBuff.getHeight()-boxHeight)/2);
		}
		imgBuff=Thumbnails.of(imgBuff).sourceRegion(cutTop,cutLeft,boxWidth,boxHeight).size(boxWidth, boxHeight).keepAspectRatio(false).outputQuality(1f).asBufferedImage();//剪裁处理
		writeFile(imgBuff, destFile);
	}

	/**
	 * 裁剪并压缩
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
	public static void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight, int cutTop, int cutLeft, int cutWidth, int catHeight)
			throws IOException {
		BufferedImage srcImgBuff = ImageIO.read(srcFile);
		srcImgBuff = srcImgBuff.getSubimage(cutTop, cutLeft, cutWidth,
				catHeight);
		int width = srcImgBuff.getWidth();
		int height = srcImgBuff.getHeight();
		if (width <= boxWidth && height <= boxHeight) {
			writeFile(srcImgBuff, destFile);
			return;
		}
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		} else {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		BufferedImage imgBuff = scaleImage(srcImgBuff, width, height,
				zoomWidth, zoomHeight);
		writeFile(imgBuff, destFile);
	}

	public static void writeFile(BufferedImage imgBuf, File destFile)
			throws IOException {
		File parent = destFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		ImageIO.write(imgBuf, "jpeg", destFile);
	}

	/**
	 * 添加文字水印
	 * 
	 * @param srcFile
	 *            源图片文件。需要加水印的图片文件。
	 * @param destFile
	 *            目标图片。加水印后保存的文件。如果和源图片文件一致，则覆盖源图片文件。
	 * @param minWidth
	 *            需要加水印的最小宽度，如果源图片宽度小于该宽度，则不加水印。
	 * @param minHeight
	 *            需要加水印的最小高度，如果源图片高度小于该高度，则不加水印。
	 * @param pos
	 *            加水印的位置。
	 * @param offsetX
	 *            加水印的位置的偏移量x。
	 * @param offsetY
	 *            加水印的位置的偏移量y。
	 * @param text
	 *            水印文字
	 * @param color
	 *            水印颜色
	 * @param size
	 *            水印字体大小
	 * @param alpha
	 *            透明度
	 * @throws IOException
	 */
	public static void imageMark(File srcFile, File destFile, int minWidth,
			int minHeight, int pos, int offsetX, int offsetY, String text,
			Color color, int size, int alpha) throws IOException {
		BufferedImage imgBuff = ImageIO.read(srcFile);
		int width = imgBuff.getWidth();
		int height = imgBuff.getHeight();
		if (width <= minWidth || height <= minHeight) {
			imgBuff = null;
			if (!srcFile.equals(destFile)) {
				FileUtils.copyFile(srcFile, destFile);
			}
		} else {
			imageMark(imgBuff, width, height, pos, offsetX, offsetY, text,
					color, size, alpha);
			writeFile(imgBuff, destFile);
			imgBuff = null;
		}
	}

	/**
	 * 添加图片水印
	 * 
	 * @param srcFile
	 *            源图片文件。需要加水印的图片文件。
	 * @param destFile
	 *            目标图片。加水印后保存的文件。如果和源图片文件一致，则覆盖源图片文件。
	 * @param minWidth
	 *            需要加水印的最小宽度，如果源图片宽度小于该宽度，则不加水印。
	 * @param minHeight
	 *            需要加水印的最小高度，如果源图片高度小于该高度，则不加水印。
	 * @param pos
	 *            加水印的位置。
	 * @param offsetX
	 *            加水印的位置的偏移量x。
	 * @param offsetY
	 *            加水印的位置的偏移量y。
	 * @param markFile
	 *            水印图片
	 * @throws IOException
	 */
	public static void imageMark(File srcFile, File destFile, int minWidth,
			int minHeight, int pos, int offsetX, int offsetY, File markFile)
			throws IOException {
		BufferedImage imgBuff = ImageIO.read(srcFile);
		int width = imgBuff.getWidth();
		int height = imgBuff.getHeight();
		if (width <= minWidth || height <= minHeight) {
			imgBuff = null;
			if (!srcFile.equals(destFile)) {
				FileUtils.copyFile(srcFile, destFile);
			}
		} else {
			imageMark(imgBuff, width, height, pos, offsetX, offsetY, markFile);
			writeFile(imgBuff, destFile);
			imgBuff = null;
		}

	}

	/**
	 * 添加文字水印
	 * 
	 * @param imgBuff
	 *            原图片
	 * @param width
	 *            原图宽度
	 * @param height
	 *            原图高度
	 * @param pos
	 *            位置。1：左上；2：右上；3左下；4右下；5：中央；0或其他：随机。
	 * @param offsetX
	 *            水平偏移量。
	 * @param offsetY
	 *            垂直偏移量。
	 * @param text
	 *            水印内容
	 * @param color
	 *            水印颜色
	 * @param size
	 *            文字大小
	 * @param alpha
	 *            透明度。0-100。越小越透明。
	 * @throws IOException
	 */
	private static void imageMark(BufferedImage imgBuff, int width, int height,
			int pos, int offsetX, int offsetY, String text, Color color,
			int size, int alpha) throws IOException {
		Position p = ImageUtils.markPosition(width, height, pos, offsetX,
				offsetY);
		Graphics2D g = imgBuff.createGraphics();
		g.setColor(color);
		g.setFont(new Font(null, Font.PLAIN, size));
		AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				(float) alpha / 100);
		g.setComposite(a);
		g.drawString(text, p.getX(), p.getY());
		g.dispose();
	}

	private static void imageMark(BufferedImage imgBuff, int width, int height,
			int pos, int offsetX, int offsetY, File markFile)
			throws IOException {
		Position p = ImageUtils.markPosition(width, height, pos, offsetX,
				offsetY);
		Graphics2D g = imgBuff.createGraphics();
		AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP);
		g.setComposite(a);
		g.drawImage(ImageIO.read(markFile), p.getX(), p.getY(), null);
		g.dispose();
	}

	
	public static void imageMark(File srcFile,File destFile, String pos,File markFile,float alpha) throws IOException{
		BufferedImage imgBuff=Thumbnails.of(srcFile).size(ImageIO.read(srcFile).getWidth(), ImageIO.read(srcFile).getHeight()).watermark(Positions.valueOf(pos), ImageIO.read(markFile), alpha).asBufferedImage();
		writeFile(imgBuff, destFile);
	}
	
	
	
	private static BufferedImage scaleImage(BufferedImage srcImgBuff,
			int width, int height, int zoomWidth, int zoomHeight) {
		int[] colorArray = srcImgBuff.getRGB(0, 0, width, height, null, 0,
				width);
		BufferedImage outBuff = new BufferedImage(zoomWidth, zoomHeight,
				BufferedImage.TYPE_INT_RGB);
		// 宽缩小的倍数
		float wScale = (float) width / zoomWidth;
		int wScaleInt = (int) (wScale + 0.5);
		// 高缩小的倍数
		float hScale = (float) height / zoomHeight;
		int hScaleInt = (int) (hScale + 0.5);
		int area = wScaleInt * hScaleInt;
		int x0, x1, y0, y1;
		int color;
		long red, green, blue;
		int x, y, i, j;
		for (y = 0; y < zoomHeight; y++) {
			// 得到原图高的Y坐标
			y0 = (int) (y * hScale);
			y1 = y0 + hScaleInt;
			for (x = 0; x < zoomWidth; x++) {
				x0 = (int) (x * wScale);
				x1 = x0 + wScaleInt;
				red = green = blue = 0;
				for (i = x0; i < x1; i++) {
					for (j = y0; j < y1; j++) {
						color = colorArray[width * j + i];
						red += getRedValue(color);
						green += getGreenValue(color);
						blue += getBlueValue(color);
					}
				}
				outBuff.setRGB(x, y, comRGB((int) (red / area),
						(int) (green / area), (int) (blue / area)));
			}
		}
		return outBuff;
	}

	private static int getRedValue(int rgbValue) {
		return (rgbValue & 0x00ff0000) >> 16;
	}

	private static int getGreenValue(int rgbValue) {
		return (rgbValue & 0x0000ff00) >> 8;
	}

	private static int getBlueValue(int rgbValue) {
		return rgbValue & 0x000000ff;
	}

	private static int comRGB(int redValue, int greenValue, int blueValue) {
		return (redValue << 16) + (greenValue << 8) + blueValue;
	}

	public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();
//		AverageImageScale.resizeFix(new File("c:/ww.jpg"), new File("c:/ww_1.jpg"), 350, 350,81,0,544,544);
//		AverageImageScale.resizeFix(new File("c:/105.jpg"), new File("c:/105_1.jpg"), 90, 80);
		
//		AverageImageScale.resizeFix(new File("c:/1.jpg"), new File("c:/1_2.jpg"), 350, 350);
//		AverageImageScale.resizeFix(new File("c:/105.jpg"), new File("c:/105_1.jpg"), 90, 80);
		AverageImageScale.imageMark(new File("c:/writer/03.JPG"), new File("c:/writer/test1.JPG"), "TOP_RIGHT", new File("c:/writer/04.png"), 1f);
		time = System.currentTimeMillis() - time;
		
		
		System.out.println("resize2 img in " + time + "ms"); 
	}
}