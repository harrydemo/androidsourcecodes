package irdc.ex09_08; 

/* import相关class */
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
  
  /* 回传分辨率72的相片信息 */
  public List<String> getSmallPhoto()
  { 
    return list1;
  }
  /* 回传分辨率288的相片信息 */
  public List<String> getBigPhoto()
  { 
    return list2;
  }

  /* XML文件开始解析时调用此method */
  @Override 
  public void startDocument() throws SAXException
  { 
    list1 = new ArrayList<String>();
    list2 = new ArrayList<String>();
  } 
  /* XML文件结束解析时调用此method */
  @Override 
  public void endDocument() throws SAXException
  {
  }

  /* 解析到Element的开头时调用此method */
  @Override 
  public void startElement(String namespaceURI, String localName, 
               String qName, Attributes atts) throws SAXException
  { 
    if (localName.equals("thumbnail"))
    { 
      if(thumbnailNum==0)
      {
        /* 将第一笔url(分辨率72的相片链接)写入list1 */
        list1.add(atts.getValue("url"));
      }
      else if(thumbnailNum==2)
      {
        /* 将第三笔url(分辨率288的相片链接)写入list2 */
        list2.add(atts.getValue("url"));
      }
      thumbnailNum++;
    }
  }
  /* 解析到Element的结尾时调用此method */
  @Override 
  public void endElement(String namespaceURI, String localName,
                         String qName) throws SAXException
  { 
    if (localName.equals("group"))
    { 
      /* 解析到group的结尾时将thumbnailNum重设为0 */
      thumbnailNum=0;
    }
  } 
}

