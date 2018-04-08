package irdc.ex08_11;

/* import相关class */
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EX08_11 extends Activity
{
  /* 变量声明
   * newName：上传后在服务器上的文件名称
   * uploadFile：要上传的文件路径
   * actionUrl：吱服器勺对应的程序路径 */
  private String newName="image.jpg";
  private String uploadFile="/data/data/irdc.ex08_11/image.jpg";
  private String actionUrl="http://10.10.100.33/upload/upload.jsp";
  private TextView mText1;
  private TextView mText2;
  private Button mButton;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    mText1 = (TextView) findViewById(R.id.myText2);
    mText1.setText("文件路径：\n"+uploadFile);
    mText2 = (TextView) findViewById(R.id.myText3);
    mText2.setText("上传网址：\n"+actionUrl);
    /* 设定mButton的onClick事件处理 */    
    mButton = (Button) findViewById(R.id.myButton);
    mButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        uploadFile();
      }
    });
  }
  
  /* 上传文件吹Server的method */
  private void uploadFile()
  {
    String end = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    try
    {
      URL url =new URL(actionUrl);
      HttpURLConnection con=(HttpURLConnection)url.openConnection();
      /* 允许Input、Output，不使用Cache */
      con.setDoInput(true);
      con.setDoOutput(true);
      con.setUseCaches(false);
      /* 设定传送的method=POST */
      con.setRequestMethod("POST");
      /* setRequestProperty */
      con.setRequestProperty("Connection", "Keep-Alive");
      con.setRequestProperty("Charset", "UTF-8");
      con.setRequestProperty("Content-Type",
                         "multipart/form-data;boundary="+boundary);
      /* 设定DataOutputStream */
      DataOutputStream ds = 
        new DataOutputStream(con.getOutputStream());
      ds.writeBytes(twoHyphens + boundary + end);
      ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\"file1\";filename=\"" +
                    newName +"\"" + end);
      ds.writeBytes(end);   

      /* 取得文件的FileInputStream */
      FileInputStream fStream = new FileInputStream(uploadFile);
      /* 设定每次写入1024bytes */
      int bufferSize = 1024;
      byte[] buffer = new byte[bufferSize];

      int length = -1;
      /* 从文件读取数据到缓冲区 */
      while((length = fStream.read(buffer)) != -1)
      {
        /* 将数据写入DataOutputStream中 */
        ds.write(buffer, 0, length);
      }
      ds.writeBytes(end);
      ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

      /* close streams */
      fStream.close();
      ds.flush();
      
      /* 取得Response内容 */
      InputStream is = con.getInputStream();
      int ch;
      StringBuffer b =new StringBuffer();
      while( ( ch = is.read() ) != -1 )
      {
        b.append( (char)ch );
      }
      /* 将Response显示于Dialog */
      showDialog(b.toString().trim());
      /* 关闭DataOutputStream */
      ds.close();
    }
    catch(Exception e)
    {
      showDialog(""+e);
    }
  }
  
  /* 显示Dialog的method */
  private void showDialog(String mess)
  {
    new AlertDialog.Builder(EX08_11.this).setTitle("Message")
     .setMessage(mess)
     .setNegativeButton("确定",new DialogInterface.OnClickListener()
     {
       public void onClick(DialogInterface dialog, int which)
       {          
       }
     })
     .show();
  }
}

