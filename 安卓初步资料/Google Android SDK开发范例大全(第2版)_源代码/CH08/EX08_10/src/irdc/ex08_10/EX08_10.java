package irdc.ex08_10; 

/* import相关class */
import java.net.URL; 
import java.net.URLConnection; 
import android.app.Activity; 
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap; 
import android.graphics.BitmapFactory; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 
import android.widget.EditText;
import android.widget.ImageView;  

public class EX08_10 extends Activity 
{ 
  /* 变量声明 */
  private Button mButton1;
  private Button mButton2;
  private EditText mEditText;
  private ImageView mImageView; 
  private Bitmap bm;
  
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 

    /* 初始化对象 */
    mButton1 =(Button) findViewById(R.id.myButton1);
    mButton2 =(Button) findViewById(R.id.myButton2);
    mEditText = (EditText) findViewById(R.id.myEdit);
    mImageView = (ImageView) findViewById(R.id.myImage);
    mButton2.setEnabled(false);
    
    /* 预览图片的Button */
    mButton1.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        String path=mEditText.getText().toString();
        if(path.equals(""))
        {
          showDialog("网址不可为空白!");
        }
        else
        {
          /* 传入type=1为预览图片 */
          setImage(path,1);
        }
      } 
    });
    
    /* 将图片设为桌面的Button */
    mButton2.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        try
        {
          String path=mEditText.getText().toString();
          if(path.equals(""))
          {
            showDialog("网址不可为空白!");
          }
          else
          {
            /* 传入type=2为设定桌面 */
            setImage(path,2);
          }
        }
        catch (Exception e)
        {
          showDialog("读取错误!网址可能不是图片或网址错误!");
          bm = null;
          mImageView.setImageBitmap(bm);
          mButton2.setEnabled(false);
          e.printStackTrace();
        }
      } 
    }); 
  }
  
  /* 将图片抓下来预览或并设定为桌面的method */
  private void setImage(String path,int type)
  {
    try 
    {  
      URL url = new URL(path); 
      URLConnection conn = url.openConnection(); 
      conn.connect();  
      if(type==1)
      {
        /* 预览图片 */
        bm = BitmapFactory.decodeStream(conn.getInputStream());
        mImageView.setImageBitmap(bm);
        mButton2.setEnabled(true);
      }
      else if(type==2)
      {
        /* 设定为桌面 */
        EX08_10.this.setWallpaper(conn.getInputStream());        
        bm = null;
        mImageView.setImageBitmap(bm);
        mButton2.setEnabled(false);
        showDialog("桌面背景设定完成!");
      }   
    } 
    catch (Exception e) 
    { 
      showDialog("读取错误!网址可能不是图片或网址错误!");
      bm = null;
      mImageView.setImageBitmap(bm);
      mButton2.setEnabled(false);
      e.printStackTrace(); 
    } 
  }
  
  /* 跳出Dialog的method */
  private void showDialog(String mess){
    new AlertDialog.Builder(EX08_10.this).setTitle("Message")
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

