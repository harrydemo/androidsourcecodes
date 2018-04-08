package irdc.ex08_09; 

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream; 
import java.net.URL; 
import java.net.URLConnection; 

import android.app.Activity; 
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle; 
import android.util.Log;
import android.view.View; 
import android.webkit.URLUtil;
import android.widget.Button; 
import android.widget.EditText;
import android.widget.ImageView; 
import android.widget.TextView; 
import android.widget.Toast;

public class EX08_09 extends Activity 
{ 
  protected static final String APP_TAG = "DOWNLOAD_RINGTONE";
  private Button mButton1; 
  private TextView mTextView1;
  private EditText mEditText1; 
  private String strURL = "";
  public static final int RINGTONE_PICKED = 0x108;
  private String currentFilePath = "";
  private String currentTempFilePath = "";
  private String fileEx="";
  private String fileNa="";
  private String strRingtoneFolder = "/sdcard/music/ringtones";
  
  
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
     
    mButton1 =(Button) findViewById(R.id.myButton1); 
    mTextView1 = (TextView) findViewById(R.id.myTextView1); 
    mEditText1 = (EditText) findViewById(R.id.myEditText1);
    
    /*�ж��Ƿ���/sdcard/music/ringtones�ļ���*/ 
    if(bIfExistRingtoneFolder(strRingtoneFolder))
    {
      Log.i(APP_TAG, "Ringtone Folder exists.");
    }
    
    mButton1.setOnClickListener(new Button.OnClickListener() 
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        strURL = mEditText1.getText().toString(); 
        
         Toast.makeText(EX08_09.this, getString(R.string.str_msg)
                  ,Toast.LENGTH_SHORT).show();         
       
        /*ȡ���ļ�����*/
        fileEx = strURL.substring(strURL.lastIndexOf(".")+1,strURL.
                     length()).toLowerCase();
        fileNa = strURL.substring(strURL.lastIndexOf("/")+1,strURL.
                     lastIndexOf("."));
        getFile(strURL);
      }
     });
  }       

  /* �жϵ���MimeType��method */
  private String getMIMEType(File f) 
  { 
    String type="";
    String fName=f.getName();
    /* ȡ����չ�� */
    String end=fName.substring(fName.lastIndexOf(".")+1,
                            fName.length()).toLowerCase(); 
    
    /* ����չ�������;���MimeType */
    if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
        end.equals("xmf")||end.equals("ogg")||end.equals("wav"))
    {
      type = "audio"; 
    }
    else if(end.equals("3gp")||end.equals("mp4"))
    {
      type = "video";
    }
    else if(end.equals("jpg")||end.equals("gif")||
             end.equals("png")||end.equals("jpeg")||
              end.equals("bmp"))
    {
      type = "image";
    }
    else
    {
      type="*";
    }
    /*����޷�ֱ�ӿ��򿪣��͵�������嵥���û�ѡ�� */
    if(end.equals("image")) 
    { 
    } 
    else 
    { 
      type += "/*";  
    } 
    return type;  
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
            Log.e(APP_TAG, e.getMessage(), e); 
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
      mTextView1.setText("�����URL"); 
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
      
      /*�����ļ���ַ*/ 
      File myTempFile = new File("/sdcard/music/ringtones/",
                        fileNa+"."+fileEx);
      /*ȡ��վ���̰�·��*/
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
      
      /* ����RingtonManager��������ѡ�� */
      String uri = null;
      if(bIfExistRingtoneFolder(strRingtoneFolder))
      {
        /*�趨����*/
        Intent intent = new Intent( RingtoneManager.
                    ACTION_RINGTONE_PICKER);
        /*�趨��ʾ�������ļ���*/
        intent.putExtra( RingtoneManager.EXTRA_RINGTONE_TYPE,
                    RingtoneManager.TYPE_RINGTONE);
        /*�趨��ʾ������ͷ*/
        intent.putExtra( RingtoneManager.EXTRA_RINGTONE_TITLE,
                    "�趨����");
        if( uri != null)
        {
          intent.putExtra( RingtoneManager.
                EXTRA_RINGTONE_EXISTING_URI, Uri.parse( uri));
        }
        else
        {
          intent.putExtra( RingtoneManager.
                EXTRA_RINGTONE_EXISTING_URI, (Uri)null); 
        }
        startActivityForResult(intent, RINGTONE_PICKED);
      }
      
      try 
      { 
        is.close(); 
      } 
      catch (Exception ex) 
      { 
        Log.e(APP_TAG, "error: " + ex.getMessage(), ex); 
      } 
    }
  } 
  
  @Override
  protected void onActivityResult(int requestCode, 
                   int resultCode, Intent data)
  {
    // TODO Auto-generated method stub
    if (resultCode != RESULT_OK)
    {
      return;
    }
    switch (requestCode)
    { 
      case (RINGTONE_PICKED):
        try
        {
          Uri pickedUri = data.getParcelableExtra
          (RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
          if(pickedUri!=null)
          {
            RingtoneManager.setActualDefaultRingtoneUri
            (EX08_09.this,RingtoneManager.TYPE_RINGTONE,
                pickedUri);
          }
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
        break;
      default:
        break;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
  
  /*�ж��Ƿ�/sdcard/music/ringtones�ļ���*/
  private boolean bIfExistRingtoneFolder(String strFolder)
  {
    boolean bReturn = false;
    
    File f = new File(strFolder);
    if(!f.exists())
    {
      /*����/sdcard/music/ringtones�ļ���*/
      if(f.mkdirs())
      {
        bReturn = true;
      }
      else
      {
        bReturn = false;
      }
    }
    else
    {
      bReturn = true;
    }
    return bReturn;
  }
}       

