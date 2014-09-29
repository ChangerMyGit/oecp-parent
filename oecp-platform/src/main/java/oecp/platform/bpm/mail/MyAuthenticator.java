/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author yangtao
 * @date 2011-8-17下午03:38:09
 * @version 1.0
 */
public class MyAuthenticator extends Authenticator{
	 
	 private String userName;
	 private String password;
	 
	 public MyAuthenticator() {
	  System.out.println("myAuthenticator is construct");
	 }
	 
	 @Override
	 protected PasswordAuthentication getPasswordAuthentication() {
	  // TODO Auto-generated method stub
	  return new PasswordAuthentication(userName, password);
	 }

	 public String getUserName() {
	  return userName;
	 }

	 public void setUserName(String userName) {
	  this.userName = userName;
	 }

	 public String getPassword() {
	  return password;
	 }

	 public void setPassword(String password) {
	  this.password = password;
	 }
	 
	}

