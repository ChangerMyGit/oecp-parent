/*
 * Copyright (c) 2013 上海医师在线信息技术有限公司  All Rights Reserved.                	
*/                                                                
  

package oecp.platform.web.spring.context;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

/** 
 * 加载业务组件的classpath
 * @author slx  
 * @date 2013-11-12 下午3:11:52 
 * @version 1.0
 *  
 */
public class OECPClassLoaderHelper {
	private static Method addURL = initAddMethod();
	
	private URLClassLoader classloader;
	
	public OECPClassLoaderHelper(URLClassLoader classloader) {
		this.classloader = classloader;
	}
	
	private static File webroot = new File(BCDeployConfig.platformDocBase);
	public static void copyWebResouces(){
		List<BCDeploy> bcs = BCDeployConfig.getAllBCDeployInfos();
		
		for (BCDeploy bc : bcs) {
			copyWebResouces(bc);
		}
	}
	
	/**
	 * 拷贝一个业务组件的资源文件到平台web目录
	 * @author songlixiao
	 * @date 2013-12-6上午10:43:30
	 * @param bc
	 */
	public static void copyWebResouces(String bcname){
		copyWebResouces(BCDeployConfig.getBCDeployInfoByName(bcname));
	}
	
	private static String[] FILESNOTCOPY = new String[]{"META-INF","WEB-INF","lib","classes",".svn"};
	private static void copyWebResouces(BCDeploy bc){
		if(bc.isReloadWebResouces()){
			File bcroot = new File(bc.getWebResouceRoot());
			try {
				FileUtils.copyDirectory(bcroot, webroot, new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						for (int i=0 ;i < FILESNOTCOPY.length ; i++) {
							if(FILESNOTCOPY[i].equalsIgnoreCase(pathname.getName())){
								return false;
							}
						}
						return true;
					}
				});
			} catch (IOException e) {
				throw new RuntimeException("组件["+bc.getName()+"]资源加载失败！",e);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		FileUtils.copyDirectoryToDirectory(new File("D:/test/test1"), new File("D:/test/test2"));
	}
	
    /** 
     * 初始化addUrl 方法.
     * @return 可访问addUrl方法的Method对象
     */
    private static Method initAddMethod() {
        try {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            add.setAccessible(true);
            return add;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 加载jar classpath。
     */
    public void loadClass() {
        List<String> files = getJarFiles();
        for (String f : files) {
            loadClasspath(f);
        }

        List<String> resFiles = getResFiles();

        for (String r : resFiles) {
            loadResourceDir(r);
        }
    }

    private void loadClasspath(String filepath) {
        File file = new File(filepath);
        loopFiles(file);
    }

    private void loadResourceDir(String filepath) {
        File file = new File(filepath);
        loopDirs(file);
    }

    /**
     * 循环遍历目录，找出所有的资源路径。
     * @param file 当前遍历文件
     */
    private void loopDirs(File file) {
        // 资源文件只加载路径
        if (file.isDirectory()) {
            addURL(file);
            File[] tmps = file.listFiles();
            for (File tmp : tmps) {
                loopDirs(tmp);
            }
        }
    }

    /**
     * 循环遍历目录，找出所有的jar包。
     * @param file 当前遍历文件
     */
    private void loopFiles(File file) {
        if (file.isDirectory()) {
            File[] tmps = file.listFiles();
            for (File tmp : tmps) {
                loopFiles(tmp);
            }
        }
        else {
            if (file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".zip")) {
                addURL(file);
            }
        }
    }

    /**
     * 通过filepath加载文件到classpath。
     * @param filePath 文件路径
     * @return URL
     * @throws Exception 异常
     */
    private void addURL(File file) {
        try {
            addURL.invoke(classloader, new Object[] { file.toURI().toURL() });
        }
        catch (Exception e) {
        }
    }

    /**
     * 从配置文件中得到配置的需要加载到classpath里的路径集合。
     * @return
     */
    private List<String> getJarFiles() {
    	List<String> jars = new ArrayList<String>();
    	List<BCDeploy> bcs = BCDeployConfig.getAllBCDeployInfos();
    	for (BCDeploy bc : bcs) {
    		if(bc.getJarDirs()!=null){
    			jars.addAll(bc.getJarDirs());
    		}
		}
        return jars;
    }

    /**
     * 从配置文件中得到配置的需要加载classpath里的资源路径集合
     * @return
     */
    private List<String> getResFiles() {
    	List<String> resdirs = new ArrayList<String>();
    	List<BCDeploy> bcs = BCDeployConfig.getAllBCDeployInfos();
    	for (BCDeploy bc : bcs) {
    		if(bc.getClassDirs()!=null){
    			resdirs.addAll(bc.getClassDirs());
    		}
		}
        return resdirs;
    }
}
