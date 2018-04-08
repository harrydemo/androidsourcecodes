package cn.com.hh.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.hh.domian.AsyncImageLoader;
import cn.com.hh.domian.AsyncImageLoader.ImageCallback;
import cn.com.hh.domian.UserInfo;
import cn.com.hh.domian.WeiBoInfo;
import cn.com.hh.oauth.OAuth;
import cn.com.hh.utils.DateUtilsDef;
import cn.com.hh.view.LoginActivity.ConfigHelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends Activity {
	private List<WeiBoInfo> wbList;
	private UserInfo user;
	private LinearLayout loadingLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		loadingLayout = (LinearLayout)findViewById(R.id.loadingLayout);
		loadList();
	}

	public class WeiBoHolder {
		public ImageView wbimage;
		public ImageView wbicon;
		public TextView wbuser;
		public TextView wbtime;
		public TextView wbtext;
		}

//微博列表Adapater
  public class WeiBoAdapater extends BaseAdapter{

      private AsyncImageLoader asyncImageLoader;
      
      public int getCount() {
          return wbList.size();
      }

      public Object getItem(int position) {
          return wbList.get(position);
      }

      public long getItemId(int position) {
          return position;
      }

      public View getView(int position, View convertView, ViewGroup parent) {
          asyncImageLoader = new AsyncImageLoader();
          convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.weibo, null);
          WeiBoHolder wh = new WeiBoHolder();
          wh.wbicon = (ImageView) convertView.findViewById(R.id.wbicon);
          wh.wbtext = (TextView) convertView.findViewById(R.id.wbtext);
          wh.wbtime = (TextView) convertView.findViewById(R.id.wbtime);
          wh.wbuser = (TextView) convertView.findViewById(R.id.wbuser);
          wh.wbimage=(ImageView) convertView.findViewById(R.id.wbimage);
          WeiBoInfo wb = wbList.get(position);
          if(wb!=null){
              convertView.setTag(wb.getId());
              wh.wbuser.setText(wb.getUserName());
              wh.wbtime.setText(wb.getTime());
              wh.wbtext.setText(wb.getText(), TextView.BufferType.SPANNABLE);
//              textHighlight(wh.wbtext,new char[]{'#'},new char[]{'#'});
//              textHighlight(wh.wbtext,new char[]{'@'},new char[]{':',' '});
//              textHighlight2(wh.wbtext,"http://"," ");
              
              if(wb.getHaveImage()){
//                  wh.wbimage.setImageResource(R.drawable.images);
              }
              Drawable cachedImage = asyncImageLoader.loadDrawable(wb.getUserIcon(),wh.wbicon, new ImageCallback(){

                  public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
                      imageView.setImageDrawable(imageDrawable);
                  }
                  
              });
               if (cachedImage == null) {
                   wh.wbicon.setImageResource(R.drawable.usericon);
                  }else{
                      wh.wbicon.setImageDrawable(cachedImage);
                  }
          }
          
          return convertView;
      }
  }
  
  
  private void loadList(){
      if(ConfigHelper.nowUser==null)
      {
          
      }
      else
      {
          user=ConfigHelper.nowUser;
          //显示当前用户名称
          TextView showName=(TextView)findViewById(R.id.showName);
          showName.setText(user.getUserName());
          
          OAuth auth=new OAuth();
          String url = "http://api.t.sina.com.cn/statuses/friends_timeline.json";
//          String url = "http://api.t.sina.com.cn/statuses/public_timeline.json";
          List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
          params.add(new BasicNameValuePair("source", auth.consumerKey)); 
          HttpResponse response = auth.SignRequest(user.getToken(), user.getTokenSecret(), url, params);
          if (200 == response.getStatusLine().getStatusCode()){
              try {
                  InputStream is = response.getEntity().getContent();
                  Reader reader = new BufferedReader(new InputStreamReader(is), 4000);
                  StringBuilder buffer = new StringBuilder((int) response.getEntity().getContentLength());
                  try {
                      char[] tmp = new char[1024];
                      int l;
                      while ((l = reader.read(tmp)) != -1) {
                          buffer.append(tmp, 0, l);
                      }
                  } finally {
                      reader.close();
                  }
                  String string = buffer.toString();
                  //Log.e("json", "rs:" + string);
                  ((org.apache.http.HttpResponse) response).getEntity().consumeContent();
                  JSONArray data=new JSONArray(string);
                  for(int i=0;i<data.length();i++)
                  {
                      JSONObject d=data.getJSONObject(i);
                      //Log.e("json", "rs:" + d.getString("created_at"));
                      if(d!=null){
                          JSONObject u=d.getJSONObject("user");
                          if(d.has("retweeted_status")){
                              JSONObject r=d.getJSONObject("retweeted_status");
                          }
                          
                          //微博id
                          String id=d.getString("id");
                          String userId=u.getString("id");
                          String userName=u.getString("screen_name");
                          String userIcon=u.getString("profile_image_url");
//                          Log.e("userIcon", userIcon);
                          String time=d.getString("created_at");
                          String text=d.getString("text");
                          Boolean haveImg=false;
                          if(d.has("thumbnail_pic")){
                              haveImg=true;
                              //String thumbnail_pic=d.getString("thumbnail_pic");
                              //Log.e("thumbnail_pic", thumbnail_pic);
                          }
                          
                          Date startDate=new Date(time);
                          Date nowDate = Calendar.getInstance().getTime();
                          time=new DateUtilsDef().twoDateDistance(startDate,nowDate);
                          if(wbList==null){
                              wbList=new ArrayList<WeiBoInfo>();
                          }
                          WeiBoInfo w=new WeiBoInfo();
                          w.setId(id);
                          w.setUserId(userId);
                          w.setUserName(userName);
                          w.setTime(time +" 前");
                          w.setText(text);
                          
                          w.setHaveImage(haveImg);
                          w.setUserIcon(userIcon);
                          wbList.add(w);
                      }
                  }
                  
              }catch (IllegalStateException e) {
                  e.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              } catch (JSONException e) {
                  e.printStackTrace();
              } 
          }
          
          if(wbList!=null)
          {
              WeiBoAdapater adapater = new WeiBoAdapater();
              ListView Msglist=(ListView)findViewById(R.id.Msglist);
              Msglist.setOnItemClickListener(new OnItemClickListener(){
                 
            	  public void onItemClick(AdapterView<?> arg0, View view,int arg2, long arg3) {
                      Object obj=view.getTag();
                      if(obj!=null){
                          String id=obj.toString();
                          Intent intent = new Intent(HomeActivity.this,ViewActivity.class);
                          Bundle b=new Bundle();
                          b.putString("key", id);
                          intent.putExtras(b);
                          startActivity(intent);
                      }
                  }
                  
              });
              Msglist.setAdapter(adapater);
          }
      }
      loadingLayout.setVisibility(View.GONE);
  }
}