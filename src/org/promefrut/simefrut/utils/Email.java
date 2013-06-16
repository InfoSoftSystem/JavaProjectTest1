package org.promefrut.simefrut.utils;


import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.promefrut.simefrut.struts.administration.beans.ParametersBean;
import org.promefrut.simefrut.struts.administration.forms.ParametersForm;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;

public class Email {
	private String host = "smtp.gmail.com";
	public String EMAIL_ACCOUNT;
	public String EMAIL_PASSWORD;
	private SessionManager managerDB = null;
	
	class GJMailAuthenticator extends javax.mail.Authenticator{
		protected PasswordAuthentication getPasswordAuthentication(){
			return new PasswordAuthentication(EMAIL_ACCOUNT, EMAIL_PASSWORD);
		}
	}

	public Email()throws Exception, Error {
		try{
			Context ic = new InitialContext();
			Object obj = ic.lookup(LookUpResourceSchema.DATA_SOURCE); // Datasource Prod
			DataSource ds = (DataSource)obj; //getDataSource
			
			this.managerDB = new SessionManager(ds);
			
			ParametersBean bean = new ParametersBean(this.managerDB, null);
			EMAIL_ACCOUNT = bean.getParameterValue(ParametersForm.emailAccountID);
			EMAIL_PASSWORD = bean.getParameterValue(ParametersForm.emailPasswordID);
		}catch(Exception ee){
			ee.printStackTrace();
			throw ee;
		} catch(Error ee) {
			ee.printStackTrace();
			throw ee;
		}finally{
			if(managerDB!=null){
				managerDB.close();
			}
		}
	}

	public void sendEmail(String titulo, String[] replyTo, /*String from,*/ String[] to, String[] bcco, String msgText, String att) {
		Object[] atts = null;
		if(att != null)
			atts = new Object[] { att };
		sendEmailAtt(titulo, replyTo, /*from,*/ to, bcco, msgText, atts);
	}

	public void sendEmailAtt(String titulo,String replyTo[], /*String from, */String[] to, String[] bcco, String msgText, Object[] att) {
		Properties props=new Properties();
		props.setProperty("mail.transport.protocol","smtp");
		props.setProperty("mail.host", host);
		props.put("mail.smtp.auth","true");
		props.put("mail.smtp.port","465");
		props.put("mail.debug","false");
		props.put("mail.smtp.socketFactory.port","465");
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback","false");

		Session session = Session.getDefaultInstance(props,new GJMailAuthenticator());
		
		try {
			// create a message
			Message msg = new MimeMessage(session);
			//msg.setFrom(new InternetAddress(from));
			msg.setFrom(new InternetAddress(this.EMAIL_ACCOUNT));
			InternetAddress[] address = new InternetAddress[to.length];
			InternetAddress[] bcc = null;
			InternetAddress[] replyToAddr = null;
			
			if(bcco != null)
				bcc = new InternetAddress[bcco.length];
			
			//Transport transport=session.getTransport();
			
			//creamos la lista de envios
			for(int i = 0; i < to.length; i++) {
				address[i] = new InternetAddress(to[i]);
			}

			//seteamos a las personas que le llegaran los correos
			msg.setRecipients(Message.RecipientType.TO, address);
			
			if(replyTo !=null){
				replyToAddr = new InternetAddress[replyTo.length];
				
				for(int i = 0; i < replyTo.length; i++) {
					replyToAddr[i] = new InternetAddress(replyTo[i]);
				}
				msg.setReplyTo(replyToAddr);
			}
			
			
			if(bcco != null) {
				bcc = new InternetAddress[bcco.length];
				
				for(int i = 0; i < bcco.length; i++) {
					bcc[i] = new InternetAddress(bcco[i]);
				}
				//seteamos a las personas que le llegara con copia oculta
				msg.setRecipients(Message.RecipientType.BCC, bcc);
			}
			//seteamos el tema del mensaje
			msg.setSubject(titulo);
			//seteamos la fecha
			msg.setSentDate(new Date());

			MimeBodyPart mbp1 = new MimeBodyPart();

			//agregamos el texto al cuerpo del mensaje
			MimeMultipart mpContent = new MimeMultipart("Texto");
			mbp1.setContent(msgText, "text/html");
			mpContent.addBodyPart(mbp1);

			if(att != null && att.length > 0) {
				for(int i = 0; i < att.length; i++) {
					if(!StringUtils.isEmpty((String)att[i])) {
						MimeBodyPart mbp2 = new MimeBodyPart();
						FileDataSource fds = new FileDataSource((String)att[i]);
						mbp2.setDataHandler(new DataHandler(fds));
						mbp2.setFileName(fds.getName());
						mpContent.addBodyPart(mbp2);
					}
				}
			}
			//agregamos al mensaje el contenido
			msg.setContent(mpContent);

			//enviamos el mensaje
			////transport.connect();
			Transport.send(msg);
			//transport.close();

		} catch(MessagingException mex) {
			System.out.println("\n--Exception al momento de enviar el mensaje");
			mex.printStackTrace();
			System.out.println("Error: " + mex.getMessage());
			Exception ex = mex;
			do {
				if(ex instanceof SendFailedException) {
					SendFailedException sfex = (SendFailedException)ex;
					Address[] invalid = sfex.getInvalidAddresses();
					if(invalid != null) {
						System.out.println("    ** Invalid Addresses");
						if(invalid != null) {
							for(int i = 0; i < invalid.length; i++)
								System.out.println("         " + invalid[i]);
						}
					}
					Address[] validUnsent = sfex.getValidUnsentAddresses();
					if(validUnsent != null) {
						System.out.println("    ** ValidUnsent Addresses");
						if(validUnsent != null) {
							for(int i = 0; i < validUnsent.length; i++)
								System.out.println("         " + validUnsent[i]);
						}
					}
					Address[] validSent = sfex.getValidSentAddresses();
					if(validSent != null) {
						System.out.println("    ** ValidSent Addresses");
						if(validSent != null) {
							for(int i = 0; i < validSent.length; i++)
								System.out.println("         " + validSent[i]);
						}
					}
				}
				System.out.println();
				if(ex instanceof MessagingException)
					ex = ((MessagingException)ex).getNextException();
				else
					ex = null;
			} while(ex != null);
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
