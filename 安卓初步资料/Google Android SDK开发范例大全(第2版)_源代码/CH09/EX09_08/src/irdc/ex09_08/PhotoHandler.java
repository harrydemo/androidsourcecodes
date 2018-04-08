package irdc.ex09_08; 

/* import���class */
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes; 
import org.xml.sax.SAXException; 
import org.xml.sax.helpers.DefaultHandler; 

public class PhotoHandler extends DefaultHandler
{
  private int thumbnailNum=0;
  private List<String> list1;
  private List<String> list2;
  
  /* �ش��ֱ���72����Ƭ��Ϣ */
  public List<String> getSmallPhoto()
  { 
    return list1;
  }
  /* �ش��ֱ���288����Ƭ��Ϣ */
  public List<String> getBigPhoto()
  { 
    return list2;
  }

  /* XML�ļ���ʼ����ʱ���ô�method */
  @Override 
  public void startDocument() throws SAXException
  { 
    list1 = new ArrayList<String>();
    list2 = new ArrayList<String>();
  } 
  /* XML�ļ���������ʱ���ô�method */
  @Override 
  public void endDocument() throws SAXException
  {
  }

  /* ������Element�Ŀ�ͷʱ���ô�method */
  @Override 
  public void startElement(String namespaceURI, String localName, 
               String qName, Attributes atts) throws SAXException
  { 
    if (localName.equals("thumbnail"))
    { 
      if(thumbnailNum==0)
      {
        /* ����һ��url(�ֱ���72����Ƭ����)д��list1 */
        list1.add(atts.getValue("url"));
      }
      else if(thumbnailNum==2)
      {
        /* ��������url(�ֱ���288����Ƭ����)д��list2 */
        list2.add(atts.getValue("url"));
      }
      thumbnailNum++;
    }
  }
  /* ������Element�Ľ�βʱ���ô�method */
  @Override 
  public void endElement(String namespaceURI, String localName,
                         String qName) throws SAXException
  { 
    if (localName.equals("group"))
    { 
      /* ������group�Ľ�βʱ��thumbnailNum����Ϊ0 */
      thumbnailNum=0;
    }
  } 
}

