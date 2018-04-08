package cn.com.karl.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import cn.com.karl.domain.Dict;
import cn.com.karl.domain.Sent;

public class SaxParseXml extends DefaultHandler{
    private Dict dict;
    private List<Sent> sents=new ArrayList<Sent>();
    private Sent sent=null;
    //用来存放每次遍历后的元素名称(节点名称)  
    private String tagName;  
	
	public Dict getDict() {
		return dict;
	}


	public void setDict(Dict dict) {
		this.dict = dict;
	}


	//只调用一次  初始化list集合    
    @Override  
    public void startDocument() throws SAXException {  
    	   
    }  
      
      
    //调用多次    开始解析  
    @Override  
    public void startElement(String uri, String localName, String qName,  
            Attributes attributes) throws SAXException {  
        if(qName.equals("sent")){  
        	sent=new Sent();  
            
        }else if(qName.equals("dict")){
        	dict=new Dict(); 
        }
        this.tagName=qName;  
    }  
      
      
    //调用多次    
    @Override  
    public void endElement(String uri, String localName, String qName)  
            throws SAXException {  
        if(qName.equals("sent")){  
            this.sents.add(sent);
            sent=null;
        } else if(qName.equals("dict")){
        	dict.setSents(sents);
        	
        } 
        this.tagName=null;  
    }  
      
      
    //只调用一次  
    @Override  
    public void endDocument() throws SAXException {  
    	   
    }  
      
    //调用多次  
    @Override  
    public void characters(char[] ch, int start, int length)  
            throws SAXException {  
        if(this.tagName!=null){  
            String date=new String(ch,start,length);  
            if(this.tagName.equals("key")){  
                dict.setKey(date); 
            }  
            else if(this.tagName.equals("ps")){  
               dict.setPs(date);
            }  
            else if(this.tagName.equals("pron")){  
                dict.setPron(date);  
            }  
            else if(this.tagName.equals("pos")){  
                dict.setPos(date);  
            }  
            else if(this.tagName.equals("acceptation")){  
                dict.setAcceptation(date) ;
            }  
            else if(this.tagName.equals("orig")){  
                sent.setOrig(date); 
            } 
            else if(this.tagName.equals("pron")){  
                sent.setPronUrl(date); 
            } 
            else if(this.tagName.equals("trans")){  
                sent.setTrans(date); 
            } 
        } 
    }
}
