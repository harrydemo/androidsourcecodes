package irdc.ex08_11;

/* import���class */
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
  /* ��������
   * newName���ϴ����ڷ������ϵ��ļ�����
   * uploadFile��Ҫ�ϴ����ļ�·��
   * actionUrl��֨�����׶�Ӧ�ĳ���·�� */
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
    mText1.setText("�ļ�·����\n"+uploadFile);
    mText2 = (TextView) findViewById(R.id.myText3);
    mText2.setText("�ϴ���ַ��\n"+actionUrl);
    /* �趨mButton��onClick�¼����� */    
    mButton = (Button) findViewById(R.id.myButton);
    mButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        uploadFile();
      }
    });
  }
  
  /* �ϴ��ļ���Server��method */
  private void uploadFile()
  {
    String end = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    try
    {
      URL url =new URL(actionUrl);
      HttpURLConnection con=(HttpURLConnection)url.openConnection();
      /* ����Input��Output����ʹ��Cache */
      con.setDoInput(true);
      con.setDoOutput(true);
      con.setUseCaches(false);
      /* �趨���͵�method=POST */
      con.setRequestMethod("POST");
      /* setRequestProperty */
      con.setRequestProperty("Connection", "Keep-Alive");
      con.setRequestProperty("Charset", "UTF-8");
      con.setRequestProperty("Content-Type",
                         "multipart/form-data;boundary="+boundary);
      /* �趨DataOutputStream */
      DataOutputStream ds = 
        new DataOutputStream(con.getOutputStream());
      ds.writeBytes(twoHyphens + boundary + end);
      ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\"file1\";filename=\"" +
                    newName +"\"" + end);
      ds.writeBytes(end);   

      /* ȡ���ļ���FileInputStream */
      FileInputStream fStream = new FileInputStream(uploadFile);
      /* �趨ÿ��д��1024bytes */
      int bufferSize = 1024;
      byte[] buffer = new byte[bufferSize];

      int length = -1;
      /* ���ļ���ȡ���ݵ������� */
      while((length = fStream.read(buffer)) != -1)
      {
        /* ������д��DataOutputStream�� */
        ds.write(buffer, 0, length);
      }
      ds.writeBytes(end);
      ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

      /* close streams */
      fStream.close();
      ds.flush();
      
      /* ȡ��Response���� */
      InputStream is = con.getInputStream();
      int ch;
      StringBuffer b =new StringBuffer();
      while( ( ch = is.read() ) != -1 )
      {
        b.append( (char)ch );
      }
      /* ��Response��ʾ��Dialog */
      showDialog(b.toString().trim());
      /* �ر�DataOutputStream */
      ds.close();
    }
    catch(Exception e)
    {
      showDialog(""+e);
    }
  }
  
  /* ��ʾDialog��method */
  private void showDialog(String mess)
  {
    new AlertDialog.Builder(EX08_11.this).setTitle("Message")
     .setMessage(mess)
     .setNegativeButton("ȷ��",new DialogInterface.OnClickListener()
     {
       public void onClick(DialogInterface dialog, int which)
       {          
       }
     })
     .show();
  }
}

