package irdc.ex08_13; 

/* import���class */
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes; 
import org.xml.sax.SAXException; 
import org.xml.sax.helpers.DefaultHandler; 

public class MyHandler extends DefaultHandler
{ 
  /* �������� */
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

  /* ��ת����List<News>��XML���ݻش� */
  public List<News> getParsedData()
  { 
    return li; 
  }
  /* ����������RSS title�ش� */
  public String getRssTitle()
  { 
    return title; 
  }
  /* XML�ļ���ʼ����ʱ���ô�method */
  @Override 
  public void startDocument() throws SAXException
  { 
    li = new ArrayList<News>(); 
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
    if (localName.equals("item"))
    { 
      this.in_item = true;
      /* ������item�Ŀ�ͷʱnewһ��News���� */
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
  /* ������Element�Ľ�βʱ���ô�method */
  @Override 
  public void endElement(String namespaceURI, String localName,
                         String qName) throws SAXException
  { 
    if (localName.equals("item"))
    { 
      this.in_item = false;
      /* ������item�Ľ�βʱ��News����д��List�� */
      li.add(news);
    }
    else if (localName.equals("title"))
    { 
      if(this.in_item)
      {
        /* �趨News�����title */
        news.setTitle(buf.toString().trim());
        buf.setLength(0);
        this.in_title = false;
      }
      else
      {
        /* �趨RSS��title */
        title=buf.toString().trim();
        buf.setLength(0);
        this.in_mainTitle = false;
      }
    }
    else if (localName.equals("link"))
    { 
      if(this.in_item)
      {
        /* �趨News�����link */
        news.setLink(buf.toString().trim());
        buf.setLength(0);
        this.in_link = false;
      }
    }
    else if (localName.equals("description"))
    { 
      if(in_item)
      {
        /* �趨News�����description */
        news.setDesc(buf.toString().trim());
        buf.setLength(0);
        this.in_desc = false;
      }
    }
    else if (localName.equals("pubDate"))
    { 
      if(in_item)
      {
        /* �趨News�����pubDate */
        news.setDate(buf.toString().trim());
        buf.setLength(0);
        this.in_date = false;
      }
    } 
  } 
  /* ȡ��Element�Ŀ�ͷ��β�м�е��ַ��� */
  @Override 
  public void characters(char ch[], int start, int length)
  { 
    if(this.in_item||this.in_mainTitle)
    {
      /* ��char[]����StringBuffer */
      buf.append(ch,start,length);
    }
  } 
}

