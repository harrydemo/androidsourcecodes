package irdc.ex08_18;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class EX08_18 extends Activity
{
  public static String TAG = "EX08_18_DEBUG";
  private TextView TextView01;
  private Spinner Spinner01;
  private Button Button01;
  private List<String> List01;
  private ArrayAdapter<String> ArrayAdapter01;
  private int intSelection = 1;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    TextView01 = (TextView) findViewById(R.id.TextView01);
    Spinner01 = (Spinner) findViewById(R.id.Spinner01);
    Button01 = (Button) findViewById(R.id.Button01);

    /* 更新Spinner里的下拉菜单 */
    updateSpinner(intSelection);

    Button01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        if (intSelection == 1)
        {
          intSelection = 2;
          /* 更新Spinner理的数据 */
          updateSpinner(intSelection);
        } else
        {
          intSelection = 1;
          /* 更新Spinner理的数据 */
          updateSpinner(intSelection);
        }
      }
    });

    Spinner01.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view,
          int position, long id)
      {
        // TODO Auto-generated method stub
        TextView01.setText(parent.getSelectedItem().toString());
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent)
      {
        // TODO Auto-generated method stub
      }
    });
  }

  public String getMethod(String strGetURL, String strEncoding)
  {
    String strReturn = "";
    try
    {
      HttpURLConnection urlConnection = null;

      URL url = new URL(strGetURL);

      urlConnection = (HttpURLConnection) url.openConnection();

      urlConnection.connect();

      InputStream htmlBODY = urlConnection.getInputStream();

      if (htmlBODY != null)
      {
        int leng = 0;
        byte[] Data = new byte[100];
        byte[] totalData = new byte[0];
        int totalLeg = 0;

        do
        {
          leng = htmlBODY.read(Data);
          if (leng > 0)
          {
            totalLeg += leng;
            byte[] temp = new byte[totalLeg];
            System.arraycopy(totalData, 0, temp, 0, totalData.length);
            System.arraycopy(Data, 0, temp, totalData.length, leng);
            totalData = temp;
          }
        } while (leng > 0);

        strReturn = new String(totalData, strEncoding);
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
    return strReturn;
  }

  public void updateSpinner(int intSelection)
  {
    String strRet = "";
    String url = "http://www.dubblogs.cc:8751/Android/Test/API/TestSpinner/spinner"
        + intSelection + ".php";
    try
    {
      /* 使用API取得并传入字符串 */
      strRet = getMethod(url, "utf-8");
    } catch (Exception e)
    {
      e.printStackTrace();
    }
    try
    {
      if (strRet.length() > 0 && eregi("<delimiter1>", strRet))
      {
        String[] aryTemp01 = strRet.split("<delimiter1>");
        String[] aryTemp02 = aryTemp01[1].split("<delimiter2>");

        List01 = new ArrayList<String>();
        for (int i = 0; i < aryTemp02.length; i++)
        {
          List01.add(aryTemp02[i].trim());
        }
        /* 使用ArrayAdapter放置数据 */
        ArrayAdapter01 = new ArrayAdapter<String>(this,
            R.layout.simple_spinner_dropdown_item, List01);
        Spinner01.setAdapter(ArrayAdapter01);
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * eregi method
   * 
   * @param String
   *          strPat Pattern
   * @param String
   *          strUnknow source String
   * @return boolean true or false
   */
  public static boolean eregi(String strPat, String strUnknow)
  {
    String strPattern = "(?i)" + strPat;
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strUnknow);
    return m.find();
  }
}

