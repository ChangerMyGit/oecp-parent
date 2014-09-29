package oecp.framework.util.soap;

import oecp.framework.exception.BizException;

import org.apache.cxf.tools.common.CommandInterfaceUtils;
import org.apache.cxf.tools.common.ToolContext;
import org.apache.cxf.tools.common.ToolException;
import org.apache.cxf.tools.wsdlto.WSDLToJava;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * wsdl文件转换为java类工具
 * 
 * @author wangliang
 * @date 2011-8-16下午03:56:42
 * @version 1.0
 */
public class Wsdl2JavaUtils {
	/**
	 * wsdl 转换为java文件
	 * 
	 * @author wangliang
	 * @date 2011-8-16下午03:58:01
	 * @param wsurl
	 */
	public static String Wsdl2java(String tempPath, String wsurl)
			throws ToolException, Exception {
		if (!wsurl.endsWith("?wsdl")) {
			wsurl += "?wsdl";
		}

		String[] pargs = new String[] { "-d", tempPath, "-server", wsurl };
		return Wsdl2java(pargs);
	}

	/**
	 * wsdl2java
	 * 
	 * @author wangliang
	 * @date 2011-8-17上午10:28:17
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	public static String Wsdl2java(String[] arg) throws Exception {
		CommandInterfaceUtils.commandCommonMain();
		WSDLToJava w2j = new WSDLToJava(arg);
		w2j.run(new ToolContext());
		return "c:\\tmp";
	}

	/**
	 * java文件打成jar包
	 * 
	 * @author wangliang
	 * @date 2011-8-17上午10:52:41
	 * @param inputFileName
	 *            java文件所在路径
	 * @param outputFileName
	 *            输出文件名(完整路径)
	 * @throws Exception
	 */
	public static void jar(String inputFileName, String outputFileName)
			throws Exception {
		File targetFile = new File(outputFileName);
		if (targetFile.isFile() || outputFileName.endsWith(".jar")) {
			if (targetFile.exists()) {
				// 删除历史文件
				targetFile.delete();
			} else if (!targetFile.getParentFile().exists()) {
				// 创建目录
				if (!targetFile.getParentFile().exists()) {
					targetFile.getParentFile().mkdirs();
				}
			}
		} else {
			throw new BizException("wsdl2java错误，路径没有包含正确的jar文件名");
		}
		JarOutputStream out = new JarOutputStream(new FileOutputStream(
				outputFileName));
		File f = new File(inputFileName);
		jar(out, f, "");
		out.close();
	}

	/**
	 * java文件打成jar包
	 * 
	 * @author wangliang
	 * @date 2011-8-17上午10:52:45
	 * @param out
	 * @param f
	 * @param base
	 * @throws Exception
	 */
	private static void jar(JarOutputStream out, File file, String base)
			throws Exception {
		if (file.isDirectory()) {
			File[] fl = file.listFiles();
			base = base.length() == 0 ? "" : base + "/"; // 注意，这里用左斜杠
			for (int i = 0; i < fl.length; i++) {
				jar(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new JarEntry(base));
			FileInputStream in = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int n = in.read(buffer);
			while (n != -1) {
				out.write(buffer, 0, n);
				n = in.read(buffer);
			}
			in.close();
		}
	}

	/**
	 * 获取lib目录位置
	 * 
	 * @author wangliang
	 * @date 2011-8-17上午10:45:28
	 * @return
	 */
	public static String getLibRoot() {
		String libroot = Wsdl2JavaUtils.class.getResource("").getPath();
		int index = libroot.indexOf("WEB-INF");
		libroot = libroot.substring(1, index) + "WEB-INF/lib";
		return libroot.replaceAll("/", "\\\\");
	}

	/**
	 * 删除文件
	 * 
	 * @author wangliang
	 * @date 2011-8-17上午11:28:17
	 * @param dir
	 * @return
	 */
	public static boolean delete(File dir) {
		if (dir.isDirectory()) {
			File[] listFiles = dir.listFiles();
			for (int i = 0; i < listFiles.length && delete(listFiles[i]); i++) {
			}
		}
		return dir.delete();
	}

	public static void main(String[] args) throws ToolException, Exception {
		String jarPath = getLibRoot() + "\\MySoapJar\\test.jar";
		String tempPath = "c:\\tmp";
		delete(new File(tempPath));
		try {
			String filePath = Wsdl2java(tempPath,
					"http://localhost:8080/oecp_bc/BCRegisterBBBBB?wsdl");
			jar(filePath, jarPath);
		} catch (ToolException te) {
			throw te;
		} catch (Exception ex) {
			throw ex;
		} finally {
			delete(new File(tempPath));
		}
		System.out.println(getLibRoot());
	}
}
