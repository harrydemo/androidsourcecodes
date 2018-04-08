package irdc.ex05_12; 
import java.io.IOException; 
import android.app.Activity;
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 
import android.widget.Toast; 
public class EX05_12 extends Activity 
{
  private Button mButton1; 
  /** Called when the activity is first created. */ 
  @Override
public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    mButton1 =(Button) findViewById(R.id.myButton1); 
    /*设定Button用OnClickListener来启动事件*/
    mButton1.setOnClickListener(new Button.OnClickListener()
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        // TODO Auto-generated catch block 
        try 
        {
          /*清除背景图案*/
          clearWallpaper();
          Toast.makeText(EX05_12.this, getString(R.string.str_done) ,Toast.LENGTH_SHORT).show(); 
          } 
        catch (IOException e)
        { 
          e.printStackTrace(); 
          }
        } 
      }); 
    } 
  @Override 
  public void clearWallpaper() throws IOException 
  { 
    // TODO Auto-generated method stub 
    super.clearWallpaper();
    } 
  }



       
    
   
 
