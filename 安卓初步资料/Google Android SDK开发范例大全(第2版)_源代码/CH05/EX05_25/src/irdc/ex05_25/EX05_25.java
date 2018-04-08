package irdc.ex05_25; 

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity; 
import android.gesture.Gesture; 
import android.gesture.GestureLibraries; 
import android.gesture.GestureLibrary; 
import android.gesture.GestureOverlayView; 
import android.os.Bundle; 
import android.os.Environment;  
import android.view.KeyEvent;
import android.view.MotionEvent; 
import android.view.View; 
import android.widget.Button; 
import android.widget.EditText; 
import android.widget.Toast; 

public class EX05_25 extends Activity 
{
  private Gesture ges;
  private GestureLibrary lib; 
  private GestureOverlayView overlay; 
  private Button button01,button02; 
  private EditText et;
  private String gesPath;
  
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState);
    /* 加载main.xml Layout */
    setContentView(R.layout.main); 
     
    /* 查看SDCard是否存在 */ 
    if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) 
    { 
      /* SD卡不存在，显示Toast信息 */
      Toast.makeText(EX05_25.this,"SD卡不存在!程序无法运行",
                     Toast.LENGTH_LONG).show(); 
      finish(); 
    }
    /* 以findViewById()取得对象 */
    et = (EditText)this.findViewById(R.id.myEditText1); 
    button01 = (Button)this.findViewById(R.id.myButton1); 
    button02 = (Button)this.findViewById(R.id.myButton2); 
    overlay = (GestureOverlayView) findViewById(R.id.myGestures1); 
    
    /* 取得GestureLibrary的文件路径 */
    gesPath = new File
    (
      Environment.getExternalStorageDirectory(),"gestures" 
    ).getAbsolutePath();

    /* 设置EditText的OnKeyListener */
    et.setOnKeyListener(new EditText.OnKeyListener(){
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event)
      {
        /* 名称与手写都已设置时将新增的Button enable */
        if(ges!=null&&et.getText().length()!=0)
        {
          button01.setEnabled(true);
        }
        else
        {
          button01.setEnabled(false);
        }
        return false;
      }
    });
    
    /* 设定Overlay的OnGestureListener */
    overlay.addOnGestureListener(new GestureOverlayView.OnGestureListener()
    {
      @Override
      public void onGesture(GestureOverlayView overlay,MotionEvent event) 
      {
      }
      /* 开始画手势时将新增的Button disable，并清除Gesture */
      @Override
      public void onGestureStarted(GestureOverlayView overlay,MotionEvent event) 
      { 
        button01.setEnabled(false); 
        ges = null; 
      }
      /* 手势画完时判断名称与手写是否完整建立 */
      @Override
      public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) 
      { 
        ges = overlay.getGesture(); 
        if (ges!=null&&et.getText().length()!=0) 
        { 
          button01.setEnabled(true);  
        }
      } 
      @Override
      public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) 
      { 
      }
    });
        
    /* 设定button01的OnClickListener */
    button01.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        String gesName=et.getText().toString();       
        try 
        { 
          File file = new File(gesPath);
          lib = GestureLibraries.fromFile(gesPath); 
          
          if(!file.exists())
          {
            /* 文件不存在就直接写入 */
            lib.addGesture(gesName,ges);
            if(lib.save()) 
            { 
              /* 将设定画面数据清除 */
              et.setText(""); 
              button01.setEnabled(false);
              overlay.clear(true); 
              /* 保存成功，显示Toast信息 */
              Toast.makeText(EX05_25.this,getString(R.string.save_success)+":"+gesPath,
                             Toast.LENGTH_LONG).show(); 
            }
            else 
            {
              /* 保存失败，显示Toast信息 */
              Toast.makeText(EX05_25.this, getString(R.string.save_failed)+":"+gesPath,
                             Toast.LENGTH_LONG).show(); 
            } 
          }
          else
          {
            /* 文件存在时因读取保存的Gesture */
            if (!lib.load()) 
            { 
              /* Library读取失败，显示Toast讯息 */
              Toast.makeText(EX05_25.this, getString(R.string.load_failed)+":"+gesPath,
                             Toast.LENGTH_LONG).show(); 
            }
            else
            {
              /* 如果Library中存在相同名称，则因将其移除再写入 */
              Set<String> en=lib.getGestureEntries();
              if(en.contains(gesName))
              {
                ArrayList<Gesture> al=lib.getGestures(gesName);
                for(int i=0;i<al.size();i++){
                  lib.removeGesture(gesName,al.get(i)); 
                }
              } 
              lib.addGesture(gesName,ges);
              if(lib.save()) 
              { 
                /* 将设定画面数据清除 */
                et.setText(""); 
                button01.setEnabled(false);
                overlay.clear(true);
                /* 保存成功，显示Toast信息 */
                Toast.makeText(EX05_25.this,getString(R.string.save_success)+":"+gesPath,
                               Toast.LENGTH_LONG).show(); 
              }
              else 
              {  
                /* 保存失败，显示Toast信息 */
                Toast.makeText(EX05_25.this, getString(R.string.save_failed)+":"+gesPath,
                               Toast.LENGTH_LONG).show(); 
              } 
            }
          }
        }
        catch(Exception e) 
        { 
          e.printStackTrace(); 
        } 
      } 
    });
    /* 设置button02的OnClickListener */
    button02.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        et.setText(""); 
        button01.setEnabled(false); 
        overlay.clear(true); 
      } 
    }); 
  }
} 

