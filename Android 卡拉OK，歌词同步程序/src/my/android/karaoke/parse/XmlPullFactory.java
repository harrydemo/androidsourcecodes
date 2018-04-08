package my.android.karaoke.parse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class XmlPullFactory {
	
	private final static String CODING_TYPE="utf-8";

	public static XmlPullParser CreateXppFromAssets(String xmlPath,Context context) throws IOException {
		AssetManager asset = context.getAssets();
		//return asset.openXmlResourceParser(xmlPath);
		InputStream inputStream = null ;
		 
		  try {
		 
		   inputStream = asset.open(xmlPath);
		 
		  } catch (IOException e) {
		 
		   Log.e ("tag", e.getMessage());
		 
		  }

		  XmlPullParser xpp = null;

			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				
				factory.setNamespaceAware(true); 
				xpp = factory.newPullParser();

			} catch (XmlPullParserException e1) {			
				e1.printStackTrace();
			}        
			
			String xmltext = "";
			
			try {			
				InputStreamReader inStreamReader = new InputStreamReader(inputStream,CODING_TYPE);				
				BufferedReader br = new BufferedReader(inStreamReader);
				
		    	String data = null;
		    	
		    	while((data = br.readLine())!=null)
		    	{
		    		xmltext = xmltext + data;
		    	}
		    	
		    	inStreamReader.close();
		    	inputStream.close();
		    	br.close();

		    	StringReader strReader = new StringReader(xmltext);
		    	xpp.setInput(strReader);
		    	
		    //	strReader.close();
			} catch (FileNotFoundException e) {			
				e.printStackTrace();
			} catch(XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {			
				e.printStackTrace();
			}
			
			return xpp;
		
	}
	
	public static XmlPullParser CreateXppFromXml(String xmlPath) {
		
		XmlPullParser xpp = null;

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			
			factory.setNamespaceAware(true); 
			xpp = factory.newPullParser();

		} catch (XmlPullParserException e1) {			
			e1.printStackTrace();
		}        
		
		String xmltext = "";
		
		try {			
			FileInputStream fileInStream = new FileInputStream(xmlPath);
			InputStreamReader inStreamReader = new InputStreamReader(fileInStream,CODING_TYPE);				
			BufferedReader br = new BufferedReader(inStreamReader);
			
	    	String data = null;
	    	
	    	while((data = br.readLine())!=null)
	    	{
	    		xmltext = xmltext + data;
	    	}
	    	
	    	inStreamReader.close();
	    	fileInStream.close();
	    	br.close();

	    	StringReader strReader = new StringReader(xmltext);
	    	xpp.setInput(strReader);
	    	
	    //	strReader.close();
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch(XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		return xpp;
	}	
    
    public static String getAttrValueByName(String name, XmlPullParser xpp){
    	String valueStr = "";
    	
    	int attrCount = xpp.getAttributeCount();
		for(int i=0 ; i<attrCount ; i++) {
			if(name.equals(xpp.getAttributeName(i))){
				valueStr = xpp.getAttributeValue(i);
				break;
			}
		}
		
		return valueStr;
    }
}
