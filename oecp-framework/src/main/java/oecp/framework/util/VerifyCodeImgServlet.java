/*
 * Copyright (c) 2011 DOCONLINE All Rights Reserved.                	
 * <a href="http://www.doconline.cn">医师在线</a> 
*/                                                                
  

package oecp.framework.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oecp.framework.utils.verifyCode.service.VerifyCodeService;
import oecp.framework.utils.verifyCode.service.VerifyCodeServiceImpl;

/** 
 * 验证码图片生成
 * @author hailang  
 * @date 2011-12-2 下午04:31:40 
 * @version 1.0
 *  
 */
public class VerifyCodeImgServlet extends HttpServlet{
	private ApplicationContext context;

    private static final long serialVersionUID = 5274323889605521606L;

    @Override
    public void init()throws ServletException
    {
        super.init();
        context = WebApplicationContextUtils.getWebApplicationContext( getServletContext() );
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )throws ServletException,IOException
    {
        String key = request.getParameter("key");

        if ( key == null || key.length() == 0 )
        {
            response.sendError( 400, "验证码不存在" );
        }else{
        	VerifyCodeService verifyCodeService = (VerifyCodeServiceImpl) context.getBean( "verifyCodeService" );
            try{
                response.setContentType( "image/jpeg" );
                OutputStream out = response.getOutputStream();
                out.write(verifyCodeService.generateVerifyCodeImage(key));
                out.close();
            }catch (Exception e){
                response.sendError( 400, e.getMessage());
            }
        }
    }

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
    
}
