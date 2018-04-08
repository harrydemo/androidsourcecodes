package irdc.ex06_08;
import java.io.File;
import java.text.DecimalFormat;
import android.app.Activity;
import android.os.Bundle; 
import android.os.Environment; 
import android.os.StatFs; 
import android.view.View; 
import android.widget.Button; 
import android.widget.ProgressBar; 
import android.widget.TextView; 
public class EX06_08 extends Activity 
{ 
  private Button myButton; 
  private ProgressBar myProgressBar;
  private TextView myTextView; 
  /** Called when the activity is first created. */
  @Override 
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    myButton = (Button) findViewById(R.id.myButton); 
    myProgressBar = (ProgressBar) findViewById(R.id.myProgressBar); 
    myTextView = (TextView) findViewById(R.id.myTextView);
    myButton.setOnClickListener(new Button.OnClickListener() 
    {
      @Override 
      public void onClick(View arg0) 
      {
        showSize(); 
        }
      }); 
    }
  private void showSize() 
  { 
    /* 将TextView及ProgressBar设定为空值及0 */
    myTextView.setText(""); myProgressBar.setProgress(0);
    /* 判断记忆卡是否插入 */
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    { 
      /* 取得SD CARD档案路径一般是/sdcard */
      File path = Environment.getExternalStorageDirectory(); 
      /* StatFs看文件系统空间使用状况 */
      StatFs statFs = new StatFs(path.getPath());
      /* Block的size */
      long blockSize = statFs.getBlockSize(); 
      /* 总Block数量 */ 
      long totalBlocks = statFs.getBlockCount(); 
      /* 已使用的Block数量 */
      long availableBlocks = statFs.getAvailableBlocks(); 
      String[] total = fileSize(totalBlocks * blockSize); 
      String[] available = fileSize(availableBlocks * blockSize);
      /* getMax取得在main.xml里ProgressBar设定的最大值 */
      int ss = Integer.parseInt(available[0]) * myProgressBar.getMax() / Integer.parseInt(total[0]); 
      myProgressBar.setProgress(ss); 
      String text = "总共" + total[0] + total[1] + "\n"; text += "可用" + available[0] + available[1]; 
      myTextView.setText(text); } 
    else if (Environment.getExternalStorageState().equals( Environment.MEDIA_REMOVED))
    { 
      String text = "SD CARD已移除"; myTextView.setText(text); 
      }
    } 
  /* 回传为字符串数组[0]为大小[1]为单位KB或MB */ 
  private String[] fileSize(long size) 
  {
    String str = ""; 
    if (size >= 1024)
    {
      str = "KB"; 
      size /= 1024; 
      if (size >= 1024)
      {
        str = "MB"; 
        size /= 1024;
        } 
      } 
    DecimalFormat formatter = new DecimalFormat();
    /* 每3个数字用,分隔如：1,000 */
    formatter.setGroupingSize(3); 
    String result[] = new String[2];
    result[0] = formatter.format(size);
    result[1] = str; return result;
    } 
  }