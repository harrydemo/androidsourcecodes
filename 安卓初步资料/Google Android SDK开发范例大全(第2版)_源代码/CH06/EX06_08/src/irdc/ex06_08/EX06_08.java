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
    /* ��TextView��ProgressBar�趨Ϊ��ֵ��0 */
    myTextView.setText(""); myProgressBar.setProgress(0);
    /* �жϼ��俨�Ƿ���� */
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    { 
      /* ȡ��SD CARD����·��һ����/sdcard */
      File path = Environment.getExternalStorageDirectory(); 
      /* StatFs���ļ�ϵͳ�ռ�ʹ��״�� */
      StatFs statFs = new StatFs(path.getPath());
      /* Block��size */
      long blockSize = statFs.getBlockSize(); 
      /* ��Block���� */ 
      long totalBlocks = statFs.getBlockCount(); 
      /* ��ʹ�õ�Block���� */
      long availableBlocks = statFs.getAvailableBlocks(); 
      String[] total = fileSize(totalBlocks * blockSize); 
      String[] available = fileSize(availableBlocks * blockSize);
      /* getMaxȡ����main.xml��ProgressBar�趨�����ֵ */
      int ss = Integer.parseInt(available[0]) * myProgressBar.getMax() / Integer.parseInt(total[0]); 
      myProgressBar.setProgress(ss); 
      String text = "�ܹ�" + total[0] + total[1] + "\n"; text += "����" + available[0] + available[1]; 
      myTextView.setText(text); } 
    else if (Environment.getExternalStorageState().equals( Environment.MEDIA_REMOVED))
    { 
      String text = "SD CARD���Ƴ�"; myTextView.setText(text); 
      }
    } 
  /* �ش�Ϊ�ַ�������[0]Ϊ��С[1]Ϊ��λKB��MB */ 
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
    /* ÿ3��������,�ָ��磺1,000 */
    formatter.setGroupingSize(3); 
    String result[] = new String[2];
    result[0] = formatter.format(size);
    result[1] = str; return result;
    } 
  }