package com.bn.helper;

import java.net.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.google.android.maps.GeoPoint;

//���ڴ������ϻ�ȡ�г�·����Ϣ�Ĺ�����
public class NavigateUtil
{
	//�������ϻ�ȡЯ���г�·����Ϣ��XML�ĵ��ķ���
	public static Document getPointsroute
	(		
		GeoPoint start,  //��㾭γ��
		GeoPoint end     //�յ㾭γ��
	)
	{
		//��������յ㾭γ�ȶ�̬���������URL
		StringBuilder str=new StringBuilder("http://maps.google.com/maps?f=d&hl=en");
		str.append("&saddr=");
		str.append(start.getLatitudeE6()/1E6);
		str.append(",");
		str.append(start.getLongitudeE6()/1E6);
		str.append("&daddr=");
		str.append(end.getLatitudeE6()/1E6);
		str.append(",");
		str.append(end.getLongitudeE6()/1E6);
		str.append("&ie=UTF8&oe=UTF8&output=kml"); 		
		String urlStr=null;
		try
		{
			urlStr=new String(str.toString().getBytes(),"UTF-8");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//����URL�������ϻ�ȡXML�ĵ�
		Document doc=null;    	
    	try
    	{
    		URL url=new URL(urlStr);
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder=factory.newDocumentBuilder();		    
		    //��ȡXML�ĵ�����
		    doc=builder.parse(url.openStream());    
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}    	
    	return doc;	
	}
	
	//��XML�ĵ��л�ȡ·���нڵ���б�
    public static GeoPoint[] getRoutePoints(Document doc)
    {
    	GeoPoint[] result=null;
    	    	
    	String rountPointsStr=null;
    	try
    	{
    		NodeList nld =doc.getElementsByTagName("GeometryCollection");
    		Element gc=(Element)nld.item(0);
    		nld=gc.getElementsByTagName("coordinates");
    		Element coordinates=(Element)nld.item(0);    		
    		rountPointsStr=coordinates.getChildNodes().item(0).getNodeValue();     		
        	
        	String[] sa=rountPointsStr.split(" ");
        	int count=sa.length;
        	
        	GeoPoint[] temp=new GeoPoint[count];
        	
        	for(int i=0;i<count;i++)
        	{
        		String tsa[]=sa[i].split(",");
        		int lon=(int)(Double.parseDouble(tsa[0])*1E6);
        		int lat=(int)(Double.parseDouble(tsa[1])*1E6);
        		temp[i]=new GeoPoint
                (
                		lat, //γ��
                		lon //����
                );       		
        	}    
    		result=temp;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}	
    	return result;
    }
}