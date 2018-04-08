package irdc.ex07_06;
import java.io.FileNotFoundException;
import android.app.Activity;
import android.content.ContentResolver; 
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button;
import android.widget.ImageView;
public class EX07_06 extends Activity
{
  private Button myButton01;
  private ImageView myImageView01;
  /** Called when the activity is first created. */
  @Override 
  public void onCreate(Bundle savedInstanceState)
  { 
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    myImageView01 = (ImageView) findViewById(R.id.myImageView01);
    myButton01 = (Button) findViewById(R.id.myButton01);
    myButton01.setOnClickListener(new Button.OnClickListener()
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        Intent intent = new Intent(); 
        /* 开启Pictures画面Type设定为image */
        intent.setType("image/*"); 
        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT); 
        /* 取得相片后返回本画面 */ 
        startActivityForResult(intent, 1);
        }
      }); 
    } 
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    if (resultCode == RESULT_OK)
    { 
      Uri uri = data.getData();
      ContentResolver cr = this.getContentResolver(); 
      try 
      { 
        Bitmap bitmap = BitmapFactory.decodeStream(cr .openInputStream(uri));
        /* 将Bitmap设定到ImageView */
        myImageView01.setImageBitmap(bitmap);
        } 
      catch (FileNotFoundException e) 
      {
        e.printStackTrace();
        } 
      }
    super.onActivityResult(requestCode, resultCode, data);
    } 
  }