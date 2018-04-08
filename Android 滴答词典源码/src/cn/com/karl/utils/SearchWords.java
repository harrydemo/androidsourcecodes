package cn.com.karl.utils;

import java.io.InputStream;
import java.net.URLEncoder;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.com.karl.domain.Dict;

public class SearchWords {

	private final static String URL="http://dict-co.iciba.com/api/dictionary.php?w=";
	
	public static  Dict tansWord(String word){
		System.out.println("---------------------------"+URL+word);
		InputStream in=DataAcess.getStreamByUrl(URL+word);
		
		
		//Dict dict=PullXmlHelper.pullParseXml(in);
		Dict dict=null;
		 SAXParser parser = null;  
	        try {  
	            //����SAXParser  
	            parser = SAXParserFactory.newInstance().newSAXParser();  
	            //ʵ����  DefaultHandler����  
	            SaxParseXml parseXml=new SaxParseXml();  
	            //����parse()����  
	            parser.parse(in, parseXml);
	            
	            dict=parseXml.getDict();
	            
	        }catch (Exception e) {
				// TODO: handle exception
	        	e.printStackTrace();
			}
		
		return dict;
		
	}
	
}
