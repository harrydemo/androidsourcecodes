package cn.com.hh.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.hh.domian.AsyncImageLoader;
import cn.com.hh.domian.AsyncImageLoader.ImageCallback;
import cn.com.hh.domian.UserInfo;
import cn.com.hh.oauth.OAuth;
import cn.com.hh.view.LoginActivity.ConfigHelper;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ViewActivity extends Activity {
	private UserInfo user;
    private String key="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);
		
		//获取上一个页面传递过来的key，key为某一条微博的id
        Intent i=this.getIntent();
        if(!i.equals(null)){
            Bundle b=i.getExtras();
            if(b!=null){
                if(b.containsKey("key")){
                    key = b.getString("key");
                    view(key);
                }
            }
        }
	}

	private void view(String id){
        user=ConfigHelper.nowUser;
        OAuth auth=new OAuth();
        String url = "http://api.t.sina.com.cn/statuses/show/:id.json";
        List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("source", auth.consumerKey)); 
        params.add(new BasicNameValuePair("id", id));
        HttpResponse response =auth.SignRequest(user.getToken(), user.getTokenSecret(), url, params);
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
                response.getEntity().consumeContent();
                JSONObject data=new JSONObject(string);
                if(data!=null){
                    JSONObject u=data.getJSONObject("user");
                    String userName=u.getString("screen_name");
                    String userIcon=u.getString("profile_image_url");
                    Log.e("userIcon", userIcon);
                    String time=data.getString("created_at");
                    String text=data.getString("text");
                    
                    TextView utv=(TextView)findViewById(R.id.user_name);
                    utv.setText(userName);
                    TextView ttv=(TextView)findViewById(R.id.text);
                    ttv.setText(text);
                    
                    ImageView iv=(ImageView)findViewById(R.id.user_icon);
                    AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
                    Drawable cachedImage = asyncImageLoader.loadDrawable(userIcon,iv, new ImageCallback(){
                      
                    	public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
                            
                            imageView.setImageDrawable(imageDrawable);
                        }
                    });
                    if (cachedImage == null) 
                    {
                        iv.setImageResource(R.drawable.usericon);
                    }
                    else
                    {
                        iv.setImageDrawable(cachedImage);
                    }
                    if(data.has("bmiddle_pic")){
                        String picurl=data.getString("bmiddle_pic");
                        String picurl2=data.getString("original_pic");

                        ImageView pic=(ImageView)findViewById(R.id.pic);
                        pic.setTag(picurl2);
                        pic.setOnClickListener(new OnClickListener() {

                        	public void onClick(View v) {
                                Object obj=v.getTag();
                                Intent intent = new Intent(ViewActivity.this,ImageActivity.class);
                                Bundle b=new Bundle();
                                b.putString("url", obj.toString());
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        });
                        Drawable cachedImage2 = asyncImageLoader.loadDrawable(picurl,pic, new ImageCallback(){
                            public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
                                showImg(imageView,imageDrawable);
                            }
                        });
                        if (cachedImage2 == null) 
                        {
                            //pic.setImageResource(R.drawable.usericon);
                        }
                        else
                        {
                            showImg(pic,cachedImage2);
                        }
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
        url = "http://api.t.sina.com.cn/statuses/counts.json";
        params=new ArrayList();
        params.add(new BasicNameValuePair("source", auth.consumerKey)); 
        params.add(new BasicNameValuePair("ids", id));
        response =auth.SignRequest(user.getToken(), user.getTokenSecret(), url, params);
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
                response.getEntity().consumeContent();
                JSONArray data=new JSONArray(string);
                if(data!=null){
                    if(data.length()>0){
                        JSONObject d=data.getJSONObject(0);
                        String comments=d.getString("comments");
                        String rt=d.getString("rt");
                        Button btn_gz=(Button)findViewById(R.id.btn_gz);
                        btn_gz.setText("        转发("+rt+")");
                        Button btn_pl=(Button)findViewById(R.id.btn_pl);
                        btn_pl.setText("        评论("+comments+")");
                    }
                }
            }
            catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } 
        }
    }
	
	private void showImg(ImageView view,Drawable img){
        int w=img.getIntrinsicWidth();
        int h=img.getIntrinsicHeight();
        Log.e("w", w+"/"+h);
        if(w>300)
        {
            int hh=300*h/w;
            Log.e("hh", hh+"");
            LayoutParams para=(LayoutParams) view.getLayoutParams();
            para.width=300;
            para.height=hh;
            view.setLayoutParams(para);
        }
        view.setImageDrawable(img);
    }
}
