package com.ldci.t56.mobile.handler;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class PhoneNumberXml extends DefaultHandler {
	public static String mPhoneNumberStr;
	
	@Override
	public void setDocumentLocator(Locator locator) {
		super.setDocumentLocator(locator);
		mPhoneNumberStr = "";
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		Log.d("phonenumber","xml data is "+new String(ch,start,length));
		mPhoneNumberStr += "\n"+new String(ch,start,length);
	}
	
}
