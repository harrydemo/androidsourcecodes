package irdc.ex08_13; 

/* import相关class */
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes; 
import org.xml.sax.SAXException; 
import org.xml.sax.helpers.DefaultHandler; 

public class MyHandler extends DefaultHandler
{ 
  /* 变量声明 */
  private boolean in_item = false;
  private boolean in_title = false;
  private boolean in_link = false;
  private boolean in_desc = false;
  private boolean in_date = false;
  private boolean in_mainTitle = false;
  private List<News> li;
  private News news;
  private String title="";
  private StringBuffer buf=new StringBuffer();

  /* 将转换成List<News>的XML数据回传 */
  public List<News> getParsedData()
  { 
    return li; 
  }
  /* 将解析出的RSS title回传 */
  public String getRssTitle()
  { 
    return title; 
  }
  /* XML文件开始解析时调用此method */
  @Override 
  public void startDocument() throws SAXException
  { 
    li = new ArrayList<News>(); 
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
    if (localName.equals("item"))
    { 
      this.in_item = true;
      /* 解析到item的开头时new一个News对象 */
      news=new News();
    }
    else if (localName.equals("title"))
    { 
      if(this.in_item)
      {
        this.in_title = true;
      }
      else
      {
        this.in_mainTitle = true;
      }
    }
    else if (localName.equals("link"))
    { 
      if(this.in_item)
      {
        this.in_link = true;
      }
    }
    else if (localName.equals("description"))
    { 
      if(this.in_item)
      {
        this.in_desc = true;
      }
    }
    else if (localName.equals("pubDate"))
    { 
      if(this.in_item)
      {
        this.in_date = true;
      }
    } 
  }
  /* 解析到Element的结尾时调用此method */
  @Override 
  public void endElement(String namespaceURI, String localName,
                         String qName) throws SAXException
  { 
    if (localName.equals("item"))
    { 
      this.in_item = false;
      /* 解析到item的结尾时将News对象写入List中 */
      li.add(news);
    }
    else if (localName.equals("title"))
    { 
      if(this.in_item)
      {
        /* 设定News对象的title */
        news.setTitle(buf.toString().trim());
        buf.setLength(0);
        this.in_title = false;
      }
      else
      {
        /* 设定RSS的title */
        title=buf.toString().trim();
        buf.setLength(0);
        this.in_mainTitle = false;
      }
    }
    else if (localName.equals("link"))
    { 
      if(this.in_item)
      {
        /* 设定News对象的link */
        news.setLink(buf.toString().trim());
        buf.setLength(0);
        this.in_link = false;
      }
    }
    else if (localName.equals("description"))
    { 
      if(in_item)
      {
        /* 设定News对象的description */
        news.setDesc(buf.toString().trim());
        buf.setLength(0);
        this.in_desc = false;
      }
    }
    else if (localName.equals("pubDate"))
    { 
      if(in_item)
      {
        /* 设定News对象的pubDate */
        news.setDate(buf.toString().trim());
        buf.setLength(0);
        this.in_date = false;
      }
    } 
  } 
  /* 取得Element的开头结尾中间夹的字符串 */
  @Override 
  public void characters(char ch[], int start, int length)
  { 
    if(this.in_item||this.in_mainTitle)
    {
      /* 将char[]加入StringBuffer */
      buf.append(ch,start,length);
    }
  } 
}

