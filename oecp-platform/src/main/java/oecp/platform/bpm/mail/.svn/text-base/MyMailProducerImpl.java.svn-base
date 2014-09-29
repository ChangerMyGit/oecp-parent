/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.mail;

import java.util.Collection;


import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;

import org.jbpm.api.Execution;
import org.jbpm.pvm.internal.email.impl.MailTemplate;
import org.jbpm.pvm.internal.email.spi.MailProducer;

/**
 *
 * @author yangtao
 * @date 2011-8-17下午04:37:09
 * @version 1.0
 */
public class MyMailProducerImpl implements MailProducer {

	private MailTemplate template;

	public MailTemplate getTemplate() {
		return template;
	}

	public void setTemplate(MailTemplate template) {
		this.template = template;
	}
	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.email.spi.MailProducer#produce(org.jbpm.api.Execution)
	 */
	@Override
	public Collection<Message> produce(Execution execution) {
		// TODO Auto-generated method stub
		/******************/
//		Message message = instantiateEmail();
//		message.setSubject("test");
//		message.setFrom(new InternetAddress(this.template.getFrom()));
//		message.setC

		/******************/
		return null;
	}

	 protected Message instantiateEmail() {
		    return new MimeMessage((Session)null);
	}
}
