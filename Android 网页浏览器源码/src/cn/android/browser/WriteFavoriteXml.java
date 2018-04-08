package cn.android.browser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class WriteFavoriteXml 
{
	List<HistoryBean> xml_data = new ArrayList<HistoryBean>();	
	String[] dialog_data = new String[]{};
	Context context;
	
	public WriteFavoriteXml()
	{
		
	}
	
	public void onReadXml()
    {      
      File file = new File("/data/data/cn.android.browser/files/history.xml");
      if( !file.exists() )
      {
        Log.e("File_Error","Couldn't find file...");
      }
      
      try 
      {
	         SAXParserFactory spf = SAXParserFactory.newInstance();
	         SAXParser sp = spf.newSAXParser();
	         XMLReader xr = sp.getXMLReader();
	         FavoriteXmlHandler favoriteHandler = new FavoriteXmlHandler();
	         xr.setContentHandler(favoriteHandler);

	         InputStream stream = new FileInputStream(file);
	         InputSource sour = new InputSource(stream);       
	         xr.parse(sour);
	         
	        
	         xml_data = favoriteHandler.getParsedData();
	         dialog_data = favoriteHandler.getData2();
       
	
      } catch (Exception e) {        	
       	Log.e("error","WriteFavoriteXml");
       	Log.e("error",e.getLocalizedMessage());
      }
    }
    
    public boolean Write(Context context,String path,String txt)
    {
    	this.context = context;
	    try
	    {
		    OutputStream os = context.openFileOutput(path,context.MODE_PRIVATE);
		    OutputStreamWriter osw=new OutputStreamWriter(os);
		    osw.write(txt);
		    osw.close();
		    os.close();
	    }
	    catch(FileNotFoundException e)
	    {
	    	Log.e("Write","Erroe");
	    	return false;
	    }
	    catch(IOException e)
	    {
	    	Log.e("Write","Erroe");
	    	return false;
	    }
	    return true;
	 }
    
    public String insertElement(String n,String u)
    {
    	XmlSerializer serializer = Xml.newSerializer();
    	StringWriter writer = new StringWriter();
    	try{
    	serializer.setOutput(writer);
    	serializer.startDocument("UTF-8",true);
    	serializer.startTag("", "historys");
    	for(int i=0;i<xml_data.size();i++)
    	{
    		HistoryBean hb = xml_data.get(i);
    		if(hb!=null)
    		{
    			if(!hb.getName().equals(n))
    			{
	    			serializer.startTag("","history");
	    	    	serializer.startTag("","name");
	    	    	serializer.text(hb.getName());
	    	    	serializer.endTag("","name");
	    	    	serializer.startTag("","url");
	    	    	serializer.text(hb.getURL());
	    	    	serializer.endTag("","url");
	    	    	serializer.endTag("","history");
    			}
    			else
    				Toast.makeText(context, "已经在收藏夹中，请勿重复收藏", Toast.LENGTH_LONG).show();
    		}
    	}
    	serializer.startTag("","history");
    	serializer.startTag("","name");
    	serializer.text(n);
    	serializer.endTag("","name");
    	serializer.startTag("","url");
    	serializer.text(u);
    	serializer.endTag("","url");
    	serializer.endTag("","history");
    	
    	serializer.endTag("","historys");

    	serializer.endDocument();
    	return writer.toString();
    	}
    	catch(Exception e)
    	{
    		Log.e("error","insertElement");
    		throw new RuntimeException(e);
    	}
    }
    public String deleteElement(String name)
    {
    	XmlSerializer serializer = Xml.newSerializer();
    	StringWriter writer = new StringWriter();
    	try{
    	serializer.setOutput(writer);
    	serializer.startDocument("UTF-8",true);
    	serializer.startTag("", "historys");
    	for(int i=0;i<xml_data.size();i++)
    	{
    		HistoryBean hb = xml_data.get(i);
    		if(hb!=null && !hb.getName().equals(name))
    		{
    			serializer.startTag("","history");
    	    	serializer.startTag("","name");
    	    	serializer.text(hb.getName());
    	    	serializer.endTag("","name");
    	    	serializer.startTag("","url");
    	    	serializer.text(hb.getURL());
    	    	serializer.endTag("","url");
    	    	serializer.endTag("","history");
    		}
    	}
	
    	serializer.endTag("","historys");

    	serializer.endDocument();
    	return writer.toString();
    	}
    	catch(Exception e)
    	{
    		Log.e("error","deleteElement");
    		throw new RuntimeException(e);
    	}
    }
    
    public List<HistoryBean> getXmlData() { 
        return this.xml_data; 
    } 
   
    public String[] getDialogData()
    {
  	  return this.dialog_data;
    }
}
