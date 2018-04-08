package irdc.ex09_08; 

/* import相关class */
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes; 
import org.xml.sax.SAXException; 
import org.xml.sax.helpers.DefaultHandler; 

public class AlbumHandler extends DefaultHandler
{
  private String gphotoURI="";
  private String mediaURI="";
  private boolean in_entry = false;
  private boolean in_title = false;
  private boolean in_id = false;
  private List<String[]> li;
  private String[] s;
  private StringBuffer buf=new StringBuffer();

  /* 将转换成List的XML数据回传 */
  public List<String[]> getParsedData()
  { 
    return li; 
  }

  /* XML文件开始解析时调用此method */
  @Override 
  public void startDocument() throws SAXException
  { 
    li = new ArrayList<String[]>(); 
  }
  
  /* XML文件结束解析时调用此method */
  @Override 
  public void endDocument() throws SAXException
  {
  }
  
  /* 取得prefix的method */
  @Override 
  public void startPrefixMapping(String prefix,String uri)
  {
    if(prefix.equals("gphoto"))
    {
      gphotoURI=uri;
    }
    else if(prefix.equals("media"))
    {
      mediaURI=uri;
    }
  }
  
  /* 解析到Element的开头时调用此method */
  @Override 
  public void startElement(String namespaceURI, String localName, 
               String qName, Attributes atts) throws SAXException
  { 
    if (localName.equals("entry"))
    { 
      this.in_entry = true;
      /* 解析到entry的开头时new一个String[] */
      s=new String[3];
    }
    else if (localName.equals("title"))
    { 
      if(this.in_entry)
      {
        this.in_title = true;
      }
    }
    else if (localName.equals("id"))
    { 
      if(gphotoURI.equals(namespaceURI))
      {
        this.in_id = true;  
      }
    }
    else if (localName.equals("thumbnail"))
    { 
      if(mediaURI.equals(namespaceURI))
      {
        /* 相簿网址 */
        s[1]=atts.getValue("url");
      }
    }
  }
  
  /* 解析到Element的结尾时调用此method */
  @Override 
  public void endElement(String namespaceURI, String localName,
                         String qName) throws SAXException
  { 
    if (localName.equals("entry"))
    { 
      this.in_entry = false;
      /* 解析到item的结尾时将String[]写入List */
      li.add(s);
    }
    else if (localName.equals("title"))
    { 
      if(this.in_entry)
      {
        /* 相簿名称 */
        s[2]=buf.toString().trim();
        buf.setLength(0);
        this.in_title = false;
      }
    }
    else if (localName.equals("id"))
    { 
      if(gphotoURI.equals(namespaceURI))
      {
        /* 相簿ID */
        s[0]=buf.toString().trim();
        buf.setLength(0);
        this.in_id = false;
      }
    }
  }
  
  /* 取得Element的开头结尾中间夹的字符串 */
  @Override 
  public void characters(char ch[], int start, int length)
  { 
    if(this.in_title||this.in_id)
    {
      /* 将char[]加入StringBuffer */
      buf.append(ch,start,length);
    }
  } 
}

