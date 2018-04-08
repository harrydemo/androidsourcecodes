package cn.android.browser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes; 
import org.xml.sax.SAXException; 
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;
public class FavoriteXmlHandler extends DefaultHandler
{
	private List<HistoryBean> historyList;
     
     private boolean inHistory = false;    
     private boolean historyName = false;   
     private boolean historyUrl = false;
     
     private HistoryBean curHistory;
     private List<String> data;
    
     public FavoriteXmlHandler() 
     { 
         historyList = new ArrayList<HistoryBean>();
         data = new ArrayList<String>();
     }
     
     public List<HistoryBean> getParsedData() { 
          return this.historyList; 
     } 
     
     public List<String> getData() 
     { 	 
          return data; 
     } 
     
     public String[] getData2()
     {
    	 //return data2;
    	 String[] s = new String[data.size()];
    	 for(int i=0;i<data.size();i++)
    		 s[i] = data.get(i);
    	 return s;
     }
     
     @Override
     public void startElement(String uri, String localName, String qName,
             Attributes attributes) throws SAXException 
     {
    	 String tagName = localName;
    	 //Log.e("startElement","<"+tagName+">");
         if(tagName.equals("history")) {
             inHistory  = true;
             curHistory = new HistoryBean();
         }
         
         if(inHistory) 
         {
             
             if(tagName.equals("name")) 
             {
                 historyName = true;
             }
             else if(tagName.equals("url")) 
             {
                 historyUrl = true;
             }
         }
         
     }
     
     @Override
     public void endElement(String uri, String localName, String qName)
             throws SAXException 
     {
    	 String tagName = localName;
         //Log.e("endElement","</"+tagName+">");
         if(tagName.equals("history")) 
         {
             inHistory  = false;
             if(curHistory!=null)
             {
	             historyList.add(curHistory);
	             data.add(curHistory.getName());
	             Log.d("----","name="+curHistory.getName()+",url="+curHistory.getURL());
             }
             //
         }
         
         if(inHistory) 
         {          
             if(tagName.equals("name")) 
             {
                 historyName = false;
             }
             else if(tagName.equals("url")) 
             {
                 historyUrl = false;
             }
         }
     }
     
     @Override
     public void characters(char[] ch, int start, int length)
             throws SAXException 
     {
    	 //Log.e("characters",new String(ch,start,length));
    	 
         if(historyName) 
         {
             curHistory.setName(new String(ch,start,length));
         }
         else if (historyUrl) 
         {
             curHistory.setURL(new String(ch,start,length));
         }
     }
}


