/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.jpdl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;



/**
 *
 * @author yangtao
 * @date 2011-9-30上午09:26:14
 * @version 1.0
 */
public class Test {

	/**
	 * 
	 * @author yangtao
	 * @date 2011-9-30上午09:26:14
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			File file = new File("D:/test.jpdl.xml");
			InputStream is = new FileInputStream(file);
			JpdlModel jpdlModel = new JpdlModel (is);     
			ImageIO.write(new JpdlModelDrawer().draw(jpdlModel), "png", new File("D:/test.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
