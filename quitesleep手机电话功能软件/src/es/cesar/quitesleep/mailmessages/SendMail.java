/* 
 	Copyright 2010 Cesar Valiente Gordo
 
 	This file is part of QuiteSleep.

    QuiteSleep is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    QuiteSleep is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with QuiteSleep.  If not, see <http://www.gnu.org/licenses/>.
*/

package es.cesar.quitesleep.mailmessages;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.util.Log;
import es.cesar.quitesleep.ddbb.CallLog;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Contact;
import es.cesar.quitesleep.ddbb.Mail;
import es.cesar.quitesleep.ddbb.Phone;
import es.cesar.quitesleep.ddbb.Settings;
import es.cesar.quitesleep.utils.ByteArrayDataSource;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;

/**
 * 
 * @author 	Cesar Valiente Gordo
 * @mail	cesar.valiente@gmail.com
 * 
 * @mostcodefrom http://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-android-ap
 *
 */
public class SendMail extends Authenticator implements Runnable {	
	
	private final String CLASS_NAME = getClass().getName();
	
	private String user;
	private String passwd;	
	private String subject;
	private String body;
	private String incomingCallNumber;
	private List<String> receiverMailList;		
	private CallLog callLog;
	
	
	//---------------		Getters & Setters		--------------------------//
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}		
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getIncomingCallNumber() {
		return incomingCallNumber;
	}
	public void setIncomingCallNumber(String incomingCallNumber) {
		this.incomingCallNumber = incomingCallNumber;
	}
	
	public List<String> getReceiverMailList() {
		return receiverMailList;
	}
	public void setReceiverMailList (List<String> receiverMailList) {
		this.receiverMailList = receiverMailList;
	}
	
	public CallLog getCallLog() {
		return callLog;
	}
	public void setCallLog(CallLog callLog) {
		this.callLog = callLog;
	}
	//------------------------------------------------------------------------//
	
	
	

	static {
		Security.addProvider(new JSSEProvider());
	}
	
	/**
	 * Constructor with all parameters
	 * 
	 * @param user
	 * @param passwd
	 * @param subject
	 * @param body
	 * @param incomingCallNumber
	 */
	public SendMail (
			String user, 
			String passwd, 
			String subject, 
			String body, 
			String incomingCallNumber) {
		
		this.user = user;
		this.passwd = passwd;
		this.subject = subject;
		this.body = body;
		this.incomingCallNumber = incomingCallNumber;
		
		getReceiverMailList(incomingCallNumber);
	}

	
	/**
	 * Constructor empty
	 */
	public SendMail (String incomingCallNumber, CallLog callLog) {
		
		this.incomingCallNumber = incomingCallNumber;
		this.callLog = callLog;
		
		getAllData();
		getReceiverMailList(incomingCallNumber);
	}
	
	/**
	 * Get all mail data form the ddbb. Assume that previously have been check
	 * if mail service is activated.
	 */
	private void getAllData () {
		
		try {
		
			ClientDDBB clientDDBB = new ClientDDBB();
			Settings settings = clientDDBB.getSelects().selectSettings();
			if (settings != null) {
				user = settings.getUser();
				passwd = settings.getPasswd();
				subject = settings.getSubject();
				body = settings.getBody();
			}
			clientDDBB.close();
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * Function that obtains the mail asociated to the phone number passed
	 * 
	 * @param incomingCallNumber
	 */
	private void getReceiverMailList (String incomingCallNumber){
		
		try {
			ClientDDBB clientDDBB = new ClientDDBB();
			Phone phone = clientDDBB.getSelects().selectPhoneForPhoneNumber(incomingCallNumber);
			if (phone != null) {
				Contact contact = phone.getContact();
				List<Mail> mailList = 
					clientDDBB.getSelects().selectAllContactMailForContact(contact);
				clientDDBB.close();
				
				//We don't check if is a banned contact because previously must be done
				
				receiverMailList = new ArrayList<String>();
				boolean findAtLeastOne = false;
				
				for (int i=0; i<mailList.size(); i++) {
					Mail mail = mailList.get(i);
					if (mail.isUsedToSend())  {
						receiverMailList.add(mail.getContactMail());
						findAtLeastOne = true;
					}
				}
				if (!findAtLeastOne)
					receiverMailList = null;
					
				
				//---	Used when only one of all mail list was used for send email	----/
				/*
				int i=0;
				boolean find = false;
				Mail mail = null;
				while (i<mailList.size() && !find) {
					mail = mailList.get(i);
					if (mail.isUsedToSend()) 
						find = true;
					i++;
				}
				if (find) 
					receiverMailList = mail.getContactMail();
				else
					receiverMailList = null;
				 */
				//------------------------------------------------------------//				
			}
			/* The else case mustn't be never, because previously we have check
			 * that this receiver is in the banned list
			 */		
			
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
			receiverMailList = null;
		}
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		  return new PasswordAuthentication(user, passwd);
	}

	
	@Override
	public void run () {
		int numShipments = sendMail();
		saveNumShipments(numShipments);		
	}
	
	/**
	 * Send an email to the receiver associated to the phone number who has been
	 * do the call.
	 * 
	 * @return			true or false depends of the result action
	 * @see				boolean
	 */
	public synchronized int sendMail () {
		
		try {
									
			if (receiverMailList != null) {
				
				if (MailConfig.getProperties() == null)
					MailConfig.initProperties();
				
				int numShipments = 0;
				
				Properties properties = MailConfig.getProperties();
				Session session = Session.getDefaultInstance(properties, this);
				
				MimeMessage message = new MimeMessage(session);
				DataHandler dataHandler = new DataHandler(
						new ByteArrayDataSource(body.getBytes(), "text/plain"));
				message.setSender(new InternetAddress(user));
				message.setSubject(subject);
				message.setDataHandler(dataHandler);
				
				for (int i=0; i<receiverMailList.size(); i++) {
															
					String mailToSend = receiverMailList.get(i);
										
					message.setRecipient(
							Message.RecipientType.TO, new InternetAddress(mailToSend));
					Transport.send(message);
					
					numShipments ++;
				}									
				return numShipments;
			}			
			return 0;
										
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return -1;
		}
	}
	
	/**
	 * Save the CallLog object if the numShipments is greater than 0.
	 * @param numShipments
	 */
	private void saveNumShipments (int numShipments) {
		
		try {
			
			if (numShipments > 0) {
				/*
				ClientDDBB clientDDBB = new ClientDDBB();
				clientDDBB.getUpdates().insertCallLog(callLog);
			
				clientDDBB.commit();
				clientDDBB.close();
				*/
				
				//Use when join all threads finish
				callLog.setNumSendMail(numShipments);				
			}
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
}
