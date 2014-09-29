/*
 * Copyright (c) 2011 DOCONLINE All Rights Reserved.                	
 * <a href="http://www.doconline.cn">医师在线</a> 
 */

package oecp.framework.fs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

import javax.imageio.ImageIO;

import oecp.framework.util.ResourceUtils;

/**
 * 图片的上传类
 * 
 * @author yongtree
 * @date 2011-10-25 上午11:01:28
 * @version 1.0
 * 
 * 
 */
public class ImageUploader extends Uploader {

	public void save(Watermark watermark) throws Exception {
		if (watermark.getText() != null) {
			pressText(watermark);
		} else if (watermark.getImagePath() != null) {
			pressImage(watermark);
		} else {
			saveInputStream(this.file);
		}
	}

	private void pressText(Watermark watermark) throws Exception {
		ImageScale imageScale = new ImageScaleImpl();
		File destFile = new File(getTmpFilePath());
		imageScale.imageMark(this.file, destFile, getSize()[0],
				getSize()[1], watermark.getAlign(), watermark.getOffsetX(),
				watermark.getOffsetY(), watermark.getText(),
				watermark.getColor(), watermark.getFontSize(),
				watermark.getAlpha());
		this.saveInputStream(destFile);
		destFile.delete();
	}

	private void pressImage(Watermark watermark) throws Exception {
		ImageScale imageScale = new ImageScaleImpl();
		File destFile = new File(getTmpFilePath());
		imageScale.imageMark(this.file,destFile, getSize()[0],
				getSize()[1], watermark.getAlign(), watermark.getOffsetX(),
				watermark.getOffsetY(), new File(watermark.getImagePath()));
		this.saveInputStream(file);
		destFile.delete();
	}

	public void compress(int maxWidth, int maxHeight) throws Exception {
		ImageScale imageScale = new ImageScaleImpl();
		File destFile = new File(getTmpFilePath());
		imageScale.resizeFix(file, destFile, maxWidth, maxWidth);
		this.saveInputStream(destFile);
		destFile.delete();
	}

	private String getTmpFilePath() {
		String filename = UUID.randomUUID().toString();
		return request.getRealPath(ResourceUtils.getString("fs", "tmp.dir"))
				+ "/" + filename + "." + getExtention();
	}

	private BufferedImage openImage(File f) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(f);
		} catch (Exception ex) {
		}
		return bi;
	}

	public int[] getSize() {
		int[] size = new int[2];
		BufferedImage image = openImage(this.file);
		size[0] = image.getWidth(null);
		size[1] = image.getHeight(null);
		return size;
	}

}
