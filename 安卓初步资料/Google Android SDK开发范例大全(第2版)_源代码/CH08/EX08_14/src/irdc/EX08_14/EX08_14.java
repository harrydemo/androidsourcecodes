package irdc.EX08_14;

import android.app.Activity; 
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle; 
import android.util.Log; 
import android.view.View; 
import android.webkit.URLUtil; 
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/*��������java.io��java.net*/
import java.io.File; 
import java.io.FileOutputStream; 
import java.io.InputStream; 
import java.net.URL; 
import java.net.URLConnection; 



public class EX08_14 extends Activity 
{ 
  private TextView mTextView01;
  private EditText mEditText01;
  private Button mButton01;
  private static final String TAG = "DOWNLOADAPK"; 
  private String currentFilePath = ""; 
  private String currentTempFilePath = ""; 
  private String strURL="";
  private String fileEx="";
  private String fileNa="";
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
     
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    mButton01 = (Button)findViewById(R.id.myButton1);
    mEditText01 =(EditText)findViewById(R.id.myEditText1);
 
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      public void onClick(View v) 
      {
        /* ������������local�� */ 
        mTextView01.setText("������...");
        strURL = mEditText01.getText().toString(); 
        /*ȡ������װ����֮�ļ�����*/
        fileEx = strURL.substring(strURL.lastIndexOf(".")+1,strURL.length()).toLowerCase();
        fileNa = strURL.substring(strURL.lastIndexOf("/")+1,strURL.lastIndexOf("."));
        getFile(strURL);
       }
     }
    );
    
    mEditText01.setOnClickListener(new EditText.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        mEditText01.setText("");
        mTextView01.setText("Զ�̰�װ����(������URL)");
      }
      
    });
  }

  private void getFile(final String strPath) 
  { 
    try 
    { 
      if (strPath.equals(currentFilePath) ) 
      { 
          
       getDataSource(strPath);  
      }        
      currentFilePath = strPath;      
      Runnable r = new Runnable() 
      {   
        public void run() 
        {   
          try 
          { 
             getDataSource(strPath); 
          } 
          catch (Exception e) 
          { 
            Log.e(TAG, e.getMessage(), e); 
          } 
        } 
      };   
      new Thread(r).start(); 
    } 
    catch(Exception e) 
    { 
      e.printStackTrace(); 
    }
  } 
  
   /*ȡ��Զ���ļ�*/ 
  private void getDataSource(String strPath) throws Exception 
  { 
    if (!URLUtil.isNetworkUrl(strPath)) 
    { 
      mTextView01.setText("�����URL"); 
    } 
    else 
    { 
        /*ȡ��URL*/
        URL myURL = new URL(strPath); 
        /*��������*/
        URLConnection conn = myURL.openConnection();   
        conn.connect();   
        /*InputStream �����ļ�*/
        InputStream is = conn.getInputStream();   
        if (is == null) 
        { 
          throw new RuntimeException("stream is null"); 
        } 
        /*������ʱ�ļ�*/ 
        File myTempFile = File.createTempFile(fileNa, "."+fileEx); 
        /*ȡ��վ�����ļ�·��*/
        currentTempFilePath = myTempFile.getAbsolutePath(); 
        /*���ļ�д����ʱ��*/ 
        FileOutputStream fos = new FileOutputStream(myTempFile); 
        byte buf[] = new byte[128];   
        do 
        {   
          int numread = is.read(buf);   
          if (numread <= 0) 
          { 
            break; 
          } 
          fos.write(buf, 0, numread);   
        }while (true);  
        
        /*���ļ����а�װ*/
        openFile(myTempFile);
        //openFile(c);
        try 
        { 
          is.close(); 
        } 
        catch (Exception ex) 
        { 
          Log.e(TAG, "error: " + ex.getMessage(), ex); 
        } 
      }
    }  
   
  /* ���ֻ��ϴ��ļ���method */
  private void openFile(File f) 
  {
    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setAction(android.content.Intent.ACTION_VIEW);
    
    /* ����getMIMEType()��ȡ��MimeType */
    String type = getMIMEType(f);
    /* �趨intent��file��MimeType */
    intent.setDataAndType(Uri.fromFile(f),type);
    startActivity(intent); 
  }

  /* �ж��ļ�MimeType��method */
  private String getMIMEType(File f) 
  { 
    String type="";
    String fName=f.getName();
    /* ȡ����չ�� */
    String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase(); 
    
    /* ����չ�������;���MimeType */
    if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||end.equals("xmf")||end.equals("ogg")||end.equals("wav"))
    {
      type = "audio"; 
    }
    else if(end.equals("3gp")||end.equals("mp4"))
    {
      type = "video";
    }
    else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||end.equals("jpeg")||end.equals("bmp"))
    {
      type = "image";
    }
    else if(end.equals("apk")) 
    { 
      /* android.permission.INSTALL_PACKAGES */ 
      type = "application/vnd.android.package-archive"; 
    } 
    else
    {
      type="*";
    }
    /*����޷�ֱ�Ӵ򿪣�����������嵥��ʹ����ѡ�� */
    if(end.equals("apk")) 
    { 
    } 
    else 
    { 
      type += "/*";  
    } 
    return type;  
  } 

  /*�Զ���ɾ���ļ�����*/
  private void delFile(String strFileName) 
  { 
    File myFile = new File(strFileName); 
    if(myFile.exists()) 
    { 
      myFile.delete(); 
    } 
  } 
  
  /*��Activity����onPause״̬ʱ,����TextView����״̬*/
  @Override 
  protected void onPause()
  {
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    mTextView01.setText("���سɹ�");
    super.onPause();
  }

  /*��Activity����onResume״̬ʱ,ɾ����ʱ�ļ�*/
  @Override 
  protected void onResume() 
  { 
    // TODO Auto-generated method stub   
    /* ɾ����ʱ�ļ� */ 
    delFile(currentTempFilePath); 
    super.onResume(); 
  }
} 

