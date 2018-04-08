package irdc.ex08_12;

/* import相关class */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class EX08_12 extends Activity
{
  /* 变量声明 */
  Button mButton;
  EditText mEdit1;
  EditText mEdit2;
  EditText mEdit3;
  EditText mEdit4;
  EditText mEdit5;
  /* 乐多部落格XML-RPC网址 */
  private String path=
    "http://xmlrpc.blog.roodo.com/cgi-bin/mt/mt-xmlrpc.cgi";
  /* XML-RPC发布文章的method name */
  private String method="metaWeblog.newPost";
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    /* 初始化对象 */
    mEdit1=(EditText)findViewById(R.id.blogId);
    mEdit2=(EditText)findViewById(R.id.blogAccount);
    mEdit3=(EditText)findViewById(R.id.blogPwd);
    mEdit4=(EditText)findViewById(R.id.artTitle);
    mEdit5=(EditText)findViewById(R.id.artContent);  
    mButton=(Button)findViewById(R.id.myButton);
    /* 设定发布文章的onClick事件 */
    mButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        /* 取得输入的信息 */
        String blogId=mEdit1.getText().toString();
        String account=mEdit2.getText().toString();
        String pwd=mEdit3.getText().toString();
        String title=mEdit4.getText().toString();
        String content=mEdit5.getText().toString();
        
        if(blogId.equals("")||account.equals("")||pwd.equals("")||
           title.equals("")||content.equals(""))
        {
          showDialog("有字段未填写喔!!");
        }
        else
        {
          /* 发送XML POST并显示Response内容 */
          String outS=getPostString(method,blogId,account,
                                    pwd,title,content);
          String re=sendPost(outS);
          showDialog(re);
        }
      }
    });
  }
  
  /* 发送request至博客的对应网址的method */
  private String sendPost(String outString)
  {
    HttpURLConnection conn=null;
    String result="";
    URL url = null;   
    try
    {
      url = new URL(path);
      conn = (HttpURLConnection)url.openConnection();
      /* 允许Input、Output */
      conn.setDoInput(true);
      conn.setDoOutput(true);
      /* 设定传送的method=POST */
      conn.setRequestMethod("POST");
      /* setRequestProperty */
      conn.setRequestProperty("Content-Type", "text/xml");
      conn.setRequestProperty("Charset", "UTF-8");
      
      /* 送出request */
      OutputStreamWriter out = 
        new OutputStreamWriter(conn.getOutputStream(), "utf-8");
      out.write(outString);
      out.flush();
      out.close();
      /* 解析回传的XML内容 */
      result=parseXML(conn.getInputStream());
      conn.disconnect();
    }
    catch(Exception e)
    {
      conn.disconnect();
      e.printStackTrace();
      showDialog(""+e);
    }
    return result;
  }
  
  /* 解析Response的XML内容的method */
  private String parseXML(InputStream is)
  {
    String result="";
    Document doc = null;
    try
    {
      /* 将XML转换成Document对象 */
      DocumentBuilderFactory dbf=
        DocumentBuilderFactory.newInstance();
      DocumentBuilder db=dbf.newDocumentBuilder();
      doc = db.parse(is);
      doc.getDocumentElement().normalize();
      /* 检查回传值是否有包含fault这个tag，有就代表发布错误 */
      int fault=doc.getElementsByTagName("fault").getLength();
      if(fault>0)
      {
        result+="发布错误!\n";
        /* 取得faultCode(错误代码) */
        NodeList nList1=doc.getElementsByTagName("int");
        for (int i = 0; i < nList1.getLength(); ++i)
        {
          String errCode=nList1.item(i).getChildNodes().item(0)
                         .getNodeValue();
          result+="错误代码："+errCode+"\n";
        }
        /* 取得faultString(错误信息) */
        NodeList nList2=doc.getElementsByTagName("string");
        for (int i = 0; i < nList2.getLength(); ++i)
        {
          String errString=nList2.item(i).getChildNodes().item(0)
                           .getNodeValue();
          result+="错误讯息："+errString+"\n";
        }
      }
      else
      {
        /* 发布成功，取得文章编号 */
        NodeList nList=doc.getElementsByTagName("string");
        for (int i = 0; i < nList.getLength(); ++i)
        {
          String artId=nList.item(i).getChildNodes().item(0)
                       .getNodeValue();
          result+="发布成功!!文章编号「"+artId+"」";
        }
      }
    }
    catch (Exception ioe) 
    {
      showDialog(""+ioe);
    }
    return result;
  }
  
  /* 组要发送的XML内容的method */  
  private String getPostString(String method,String blogId,
      String account,String pwd,String title,String content)
  {
    String s="";
    s+="<methodCall>";
    s+="<methodName>"+method+"</methodName>";
    s+="<params>";
    s+="<param><value><string>"+blogId+"</string></value></param>";
    s+="<param><value><string>"+account+"</string></value></param>";
    s+="<param><value><string>"+pwd+"</string></value></param>";
    s+="<param><value><struct>";
    s+="<member><name>title</name>" +
       "<value><string>"+title+"</string></value></member>";
    s+="<member><name>description</name>" +
       "<value><string>"+content+"</string></value></member>";
    s+="</struct></value></param>";
    s+="<param><value><boolean>1</boolean></value></param>";
    s+="</params>";
    s+="</methodCall>";

    return s;
  }
  
  /* 弹出Dialog的method */
  private void showDialog(String mess)
  {
    new AlertDialog.Builder(EX08_12.this).setTitle("Message")
    .setMessage(mess)
    .setNegativeButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {          
      }
    })
    .show();
  }
}

