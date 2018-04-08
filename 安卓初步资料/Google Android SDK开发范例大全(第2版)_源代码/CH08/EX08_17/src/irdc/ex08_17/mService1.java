package irdc.ex08_17;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class mService1 extends Service
{
  /* �߳� */
  private Handler objHandler = new Handler();
  private int intCounter=0;
  
  /* ���µ�����Ϣ�ĺ��� */
  public int intUpdateSecond = 5000;
  private String TAG = "HIPPO";
  private String strRet = "";
  
  /* ������־Log�ļ� */
  private static String strEarthLog = "/sdcard/earthquakelist.log";
  
  /* ǰһ�εĵ����¼�ַ��� */
  private String strLastRecord;
  
  /* �����ַ����õķָ��ַ��� */
  public static String strDelimiter1 = "<delimiter1>";
  public static String strDelimiter2 = "<delimiter2>";
  
  private Runnable mTasks = new Runnable() 
  { 
    public void run() 
    { 
      if(checkSDCard())
      {
        intCounter++;
        
        /* �����Զ������������API������ */
        cwbEarthQuake();
        Log.i(TAG, "Counter:"+Integer.toString(intCounter));
        objHandler.postDelayed(mTasks, intUpdateSecond);
      }
      else
      {
        mMakeTextToast
        (
          getResources().getText(R.string.str_err_nosd).toString(),
          true
        );
      }
    }
  };
  
  /* �Զ��������������ֵĵ�����Ϣ���� */
  private void cwbEarthQuake()
  {
    /* ��������ֵ��𷢲���ҳ */
    //String uriAPI = "http://www.cwb.gov.tw/V5/seismic/quake_index.htm";
    String uriAPI = "http://www.cwb.gov.tw/V6/seismic/quake_index.htm";
    
    try
    {
      strLastRecord = "";
      
      /* �жϴ洢�����Ƿ��е���Log */
      File myFile = new File(strEarthLog);
      if(myFile.exists())
      {
        /* ��ȡǰ��Log��־ */
        StringBuilder contents = new StringBuilder();
        BufferedReader input =  new BufferedReader(new FileReader(myFile));
        String line = null;
        while((line=input.readLine()) != null)
        {
          contents.append(line);
          contents.append(System.getProperty("line.separator"));
        }
        strLastRecord = contents.toString();
      }
      else
      {
        /* first time or no log found */
        strLastRecord = "";
      }
      strLastRecord = strLastRecord.trim();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    
    strRet = parseCWHTML_V6(getMethod(uriAPI, "big5"));
    //strRet = parseCWHTML_V6(getMethod(uriAPI, "big5"));
    
    if(strRet!="")
    {
      if(strLastRecord.equals(strRet))
      {
        /* δ�����µĵ�����Ϣ */
        Log.i(TAG, "Not Found.");
      }
      else
      {
        /* д��������API���ʺ�ļ�¼ */
        if(writeCWLog(strRet))
        {
          /* ��ȷ�浵 */
        }
        else
        {
          Log.i
          (
            TAG, getResources().getText(R.string.str_err_writefile).toString()
          );
        }
        
        /* �����µ��𷢲���Ϣ */
        String strOpt = getNewEarthquake(strRet);
        if(strOpt!="")
        {
          /* ��Toast��ʾ��ַ��Ϣ��*/
          mMakeTextToast(strOpt, true);
        }
      }
    }
  }
  
  /* ȡ������һ�ʵ�����Ϣ */
  private String getNewEarthquake(String strLog)
  {
    String strReturn = "";
    try
    {
      if(eregi(strDelimiter1, strLog) && eregi(strDelimiter2, strLog))
      {
        String[] aryTemp001 = strLog.split(strDelimiter1);
        String[] aryTemp002 = aryTemp001[0].split(strDelimiter2);
        for(int i=0;i<aryTemp002.length;i++)
        {
          if(i==(aryTemp002.length-1))
          {
            strReturn += aryTemp002[i];
          }
          else
          {
            if(i==1)
            {
              strReturn += getResources().getText(R.string.str_quake_level).toString()+aryTemp002[i]+"\n";
            }
            else
            {
              strReturn += aryTemp002[i]+"\n";
            }
          }
        }
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return strReturn;
  }
  
  /* �Զ���д�����Log��־�ļ� */
  private boolean writeCWLog(String strCWLog)
  {
    try
    {
      if(strCWLog!="")
      {
        BufferedWriter bw;
        bw = new BufferedWriter (new FileWriter(strEarthLog));
        bw.write( strCWLog, 0, strCWLog.length() );
        bw.flush();
        return true;
      }
      else
      {
        return false;
      }
    }
    catch(Exception e)
    {
      return false;
    }
  }
  
  /* �����������ҳ������������ */
  /*
  private String parseCWHTML(String strCWHTML)
  {
    String strReturn = "";
    if(strCWHTML.trim()!="")
    {
      if(eregi("NewsTable",strCWHTML) && eregi("</table>",strCWHTML))
      {
        try
        {
          String[] aryTemp01 = strCWHTML.split("NewsTable");
          String[] aryTemp02 = aryTemp01[1].split("</table>");
          String[] aryTemp03;
          String[] aryTemp04;
          aryTemp01 = aryTemp02[0].split("<tr>");
          
          for(int i=2;i<aryTemp01.length;i++)
          {
            aryTemp02 = aryTemp01[i].split("href=");
            for(int j=1;j<aryTemp02.length;j++)
            {
              if(j==(aryTemp02.length-1))
              {
                aryTemp03 = aryTemp02[j].split("</a>");
                aryTemp04 = aryTemp03[0].split(">");
                strReturn += eregi_replace("(\r\n|\r|\n|\n\r)","",aryTemp04[1]);
              }
              else
              {
                aryTemp03 = aryTemp02[j].split("</a>");
                aryTemp04 = aryTemp03[0].split(">");
                strReturn += eregi_replace("(\r\n|\r|\n|\n\r)","",aryTemp04[1])+strDelimiter2;
              }
            }
            if(i==(aryTemp01.length-1)){}
            else
            {
              strReturn += strDelimiter1;
            }
          }
          strReturn = strReturn.trim();
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }
    }
    return strReturn;
  }
  */
  
  private String parseCWHTML_V6(String strCWHTML)
  {
    String strReturn = "";
    if(strCWHTML.trim()!="")
    {
      if(eregi("Form00",strCWHTML) && eregi("</table>",strCWHTML))
      {
        try
        {
          String[] aryTemp01 = strCWHTML.split("Form00");
          String[] aryTemp02 = aryTemp01[1].split("</table>");
          String[] aryTemp03;
          String[] aryTemp04;
          aryTemp01 = aryTemp02[0].split("<tr>");
          
          for(int i=2;i<aryTemp01.length;i++)
          {
            aryTemp02 = aryTemp01[i].split("href=\"");
            for(int j=1;j<aryTemp02.length;j++)
            {
              if(j==(aryTemp02.length-1))
              {
                aryTemp03 = aryTemp02[j].split("</a>");
                aryTemp04 = aryTemp03[0].split(">");
                strReturn += eregi_replace("(\r\n|\r|\n|\n\r)","",aryTemp04[1]);
              }
              else
              {
                aryTemp03 = aryTemp02[j].split("</a>");
                aryTemp04 = aryTemp03[0].split(">");
                strReturn += eregi_replace("(\r\n|\r|\n|\n\r)","",aryTemp04[1])+strDelimiter2;
              }
            }
            if(i==(aryTemp01.length-1)){}
            else
            {
              strReturn += strDelimiter1;
            }
          }
          strReturn = strReturn.trim();
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }
    }
    return strReturn;
  }
  
  /* �жϴ洢���Ƿ���� */
  private boolean checkSDCard()
  {
    /* android.os.Environment����״̬���� */
    if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  /* �Զ���GET���� */
  public String getMethod(String strGetURL, String strEncoding)
  {
    String strReturn="";
    try
    {
      HttpURLConnection urlConnection= null;
      URL url=new URL(strGetURL);
      urlConnection=(HttpURLConnection)url.openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.setDoOutput(true);
      urlConnection.setDoInput(true);
      urlConnection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
      urlConnection.setRequestProperty("Content-type","text/html; charset="+strEncoding);
      urlConnection.connect();
      InputStream htmlBODY = urlConnection.getInputStream();
      
      if(htmlBODY!=null)
      {
        int leng =0;
        byte[] Data = new byte[100];
        byte[] totalData = new byte[0];
        int totalLeg =0;

        do
        {
          leng = htmlBODY.read(Data);
          if(leng>0)
          {
            totalLeg += leng;
            byte[] temp = new byte[totalLeg];
            System.arraycopy(totalData, 0, temp, 0, totalData.length);
            System.arraycopy(Data, 0, temp, totalData.length, leng);
            totalData = temp;
          }
        }while(leng>0);

        //strReturn = new String(totalData,"UTF-8");
        strReturn = new String(totalData, strEncoding);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return strReturn;
  }
  
  /* �Զ���ȶ��ַ������� */
  public static boolean eregi(String strPat, String strUnknow)
  {
    String strPattern = "(?i)"+strPat;
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strUnknow);
    return m.find();
  }
  
  /* �Զ����ַ���ȡռ���� */
  public String eregi_replace(String strFrom, String strTo, String strTarget)
  {
    String strPattern = "(?i)"+strFrom;
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strTarget);
    if(m.find())
    {
      return strTarget.replaceAll(strFrom, strTo);
    }
    else
    {
      return strTarget;
    }
  }
  
  /* �Զ���Toast���� */
  public void mMakeTextToast(String str, boolean isLong)
  {
    if(isLong==true)
    {
      Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
    else
    {
      Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
  }
  
  @Override
  public void onStart(Intent intent, int startId)
  {
    // TODO Auto-generated method stub
    
    objHandler.postDelayed(mTasks, intUpdateSecond);
    super.onStart(intent, startId);
  }

  @Override
  public void onCreate()
  {
    // TODO Auto-generated method stub
    super.onCreate();
  }
  
  @Override
  public IBinder onBind(Intent intent)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void onDestroy()
  {
    // TODO Auto-generated method stub
    
    objHandler.removeCallbacks(mTasks);
    super.onDestroy();
  }
}

