package irdc.EX08_01;


/*��������apache.http������������HTTP����*/
import org.apache.http.HttpResponse; 
import org.apache.http.NameValuePair; 
import org.apache.http.client.ClientProtocolException; 
import org.apache.http.client.entity.UrlEncodedFormEntity; 
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost; 
import org.apache.http.impl.client.DefaultHttpClient; 
import org.apache.http.message.BasicNameValuePair; 
import org.apache.http.protocol.HTTP; 
import org.apache.http.util.EntityUtils; 
/*��������java.io ��java.util���������д�ļ�*/
import java.io.IOException; 
import java.util.ArrayList; 
import java.util.List; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 
import android.widget.TextView; 

public class EX08_01 extends Activity 
{ 
  /*��������Button����,��һ��TextView����*/
  private Button mButton1,mButton2; 
  private TextView mTextView1; 
   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
     
    /*͸��findViewById�����ӽ���TextView��Button����*/ 
    mButton1 =(Button) findViewById(R.id.myButton1); 
    mButton2 =(Button) findViewById(R.id.myButton2);
    mTextView1 = (TextView) findViewById(R.id.myTextView1); 
     
    /*�趨OnClickListener������OnClick�¼�*/
    mButton1.setOnClickListener(new Button.OnClickListener() 
    { 
      /*��дonClick�¼�*/
      @Override 
      public void onClick(View v) 
      { 
        /*������ַ�ַ���*/
        String uriAPI = "http://www.dubblogs.cc:8751/Android/Test/API/Post/index.php";
        /*����HTTP Post����*/
        HttpPost httpRequest = new HttpPost(uriAPI); 
        /*
         * Post�������ͱ���������NameValuePair[]���鴢��
        */
        List <NameValuePair> params = new ArrayList <NameValuePair>(); 
        params.add(new BasicNameValuePair("str", "I am Post String")); 
        try 
        { 
          /*����HTTP request*/
          httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
          /*ȡ��HTTP response*/
          HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
          /*��״̬��Ϊ200 ok*/
          if(httpResponse.getStatusLine().getStatusCode() == 200)  
          { 
            /*ȡ����Ӧ�ַ���*/
            String strResult = EntityUtils.toString(httpResponse.getEntity()); 
            mTextView1.setText(strResult); 
          } 
          else 
          { 
            mTextView1.setText("Error Response: "+httpResponse.getStatusLine().toString()); 
          } 
        } 
        catch (ClientProtocolException e) 
        {  
          mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace(); 
        } 
        catch (IOException e) 
        {  
          mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace(); 
        } 
        catch (Exception e) 
        {  
          mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace();  
        }  
         
      } 
    }); 
    mButton2.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        /*������ַ�ַ���*/
        String uriAPI = "http://www.dubblogs.cc:8751/Android/Test/API/Get/index.php?str=I+am+Get+String"; 
        /*����HTTP Get����*/
        HttpGet httpRequest = new HttpGet(uriAPI); 
        try 
        { 
          /*����HTTP request*/
          HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
          /*��״̬��Ϊ200 ok*/
          if(httpResponse.getStatusLine().getStatusCode() == 200)  
          { 
            /*ȡ����Ӧ�ַ���*/
            String strResult = EntityUtils.toString(httpResponse.getEntity());
            /*ɾ�������ַ�*/
            strResult = eregi_replace("(\r\n|\r|\n|\n\r)","",strResult);
            mTextView1.setText(strResult); 
          } 
          else 
          { 
            mTextView1.setText("Error Response: "+httpResponse.getStatusLine().toString()); 
          } 
        } 
        catch (ClientProtocolException e) 
        {  
          mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace(); 
        } 
        catch (IOException e) 
        {  
          mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace(); 
        } 
        catch (Exception e) 
        {  
          mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace();  
        }  
      } 
    }); 
  }
    /* �Զ����ַ���ȡ������ */
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
} 

