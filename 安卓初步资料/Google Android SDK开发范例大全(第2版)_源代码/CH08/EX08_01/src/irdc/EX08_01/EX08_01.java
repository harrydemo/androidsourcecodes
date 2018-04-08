package irdc.EX08_01;


/*必需引用apache.http相关类别来建立HTTP联机*/
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
/*必需引用java.io 与java.util相关类来读写文件*/
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
  /*声明两个Button对象,与一个TextView对象*/
  private Button mButton1,mButton2; 
  private TextView mTextView1; 
   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
     
    /*透过findViewById建构子建立TextView与Button对象*/ 
    mButton1 =(Button) findViewById(R.id.myButton1); 
    mButton2 =(Button) findViewById(R.id.myButton2);
    mTextView1 = (TextView) findViewById(R.id.myTextView1); 
     
    /*设定OnClickListener来聆听OnClick事件*/
    mButton1.setOnClickListener(new Button.OnClickListener() 
    { 
      /*重写onClick事件*/
      @Override 
      public void onClick(View v) 
      { 
        /*声明网址字符串*/
        String uriAPI = "http://www.dubblogs.cc:8751/Android/Test/API/Post/index.php";
        /*建立HTTP Post联机*/
        HttpPost httpRequest = new HttpPost(uriAPI); 
        /*
         * Post运作传送变量必须用NameValuePair[]数组储存
        */
        List <NameValuePair> params = new ArrayList <NameValuePair>(); 
        params.add(new BasicNameValuePair("str", "I am Post String")); 
        try 
        { 
          /*发出HTTP request*/
          httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
          /*取得HTTP response*/
          HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
          /*若状态码为200 ok*/
          if(httpResponse.getStatusLine().getStatusCode() == 200)  
          { 
            /*取出响应字符串*/
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
        /*声明网址字符串*/
        String uriAPI = "http://www.dubblogs.cc:8751/Android/Test/API/Get/index.php?str=I+am+Get+String"; 
        /*建立HTTP Get联机*/
        HttpGet httpRequest = new HttpGet(uriAPI); 
        try 
        { 
          /*发出HTTP request*/
          HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
          /*若状态码为200 ok*/
          if(httpResponse.getStatusLine().getStatusCode() == 200)  
          { 
            /*取出响应字符串*/
            String strResult = EntityUtils.toString(httpResponse.getEntity());
            /*删除多余字符*/
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
    /* 自定义字符串取代函数 */
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

