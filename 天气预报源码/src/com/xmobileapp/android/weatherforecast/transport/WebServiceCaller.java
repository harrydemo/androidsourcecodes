package com.xmobileapp.android.weatherforecast.transport;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.xmobileapp.android.weatherforecast.meta.Constant;

public class WebServiceCaller {

	public static String doCallWebService(String soapAction, String methodName, Map<String, String> props){
    	
    	String xmlStr;
    	
    	SoapObject request = new SoapObject(Constant.NAMESPACE, methodName);
    	Set<String> sets = props.keySet();
    	Iterator it = sets.iterator();
    	while(it.hasNext()){
    		String name =(String)it.next();
    		String value =(String)props.get(name);
    		request.addProperty(name, value);
    	}
    	
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; 
        envelope.setOutputSoapObject(request);
        AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport (Constant.URL);		        
        try {
        	androidHttpTransport.call(soapAction, envelope);
        	
            Object result = envelope.getResponse();
            xmlStr = result.toString();
        } catch(Exception e) {
        	e.printStackTrace();

        	return null;
        }
        
        return xmlStr;
    }
}
