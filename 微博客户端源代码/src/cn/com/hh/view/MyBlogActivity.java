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
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.hh.domian.UserInfo;
import cn.com.hh.http.Download;
import cn.com.hh.oauth.OAuth;
import cn.com.hh.sqlite.DataHelper;
import cn.com.hh.view.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

public class MyBlogActivity extends Activity {
    /** Called when the activity is first created. */
	private DataHelper dbHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //��ȡ�˺��б�
        dbHelper=new DataHelper(this);
        List<UserInfo> userList= dbHelper.GetUserList(true);
        if(userList.isEmpty())
        {
        	//���Ϊ��˵����һ��ʹ������AuthorizeActivityҳ�����OAuth��֤
               Intent intent = new Intent();
               intent.setClass(MyBlogActivity.this, AuthorizeActivity.class);
               startActivity(intent);
        }
        else
        	//�����Ϊ�ն�ȡ��Щ��¼��UserID�š�Access Token��Access Secretֵ
            //Ȼ�������3��ֵ�������˵�api�ӿڻ�ȡ��Щ��¼��Ӧ���û��ǳƺ��û�ͷ��ͼ�����Ϣ��
        {
               for(UserInfo user:userList){
            	   UpdateUserInfo( this, userList);
               }
               Intent intent = new Intent();
               intent.setClass(MyBlogActivity.this, LoginActivity.class);
               startActivity(intent);
        }
        
    }
    //��ȡApi�õ�΢����ͷ��
	public void UpdateUserInfo(Context context, List<UserInfo> userList) {
//		DataHelper dbHelper = new DataHelper(context);
		OAuth auth = new OAuth();
		String url = "http://api.t.sina.com.cn/users/show.json";
		Log.e("userCount", userList.size() + "");
		for (UserInfo user : userList) {
			if (user != null) {
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("source", auth.consumerKey));
				params.add(new BasicNameValuePair("user_id", user.getUserId()));
				HttpResponse response = auth.SignRequest(user.getToken(),
						user.getTokenSecret(), url, params);
				if (200 == response.getStatusLine().getStatusCode()) {
					try {
						InputStream is = response.getEntity().getContent();
						Reader reader = new BufferedReader(
								new InputStreamReader(is), 4000);
						StringBuilder buffer = new StringBuilder((int) response
								.getEntity().getContentLength());
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
						JSONObject data = new JSONObject(string);
						String ImgPath = data.getString("profile_image_url");
						Bitmap userIcon = new Download().DownloadImg(ImgPath);
						String userName = data.getString("screen_name");
						dbHelper.UpdateUserInfo(userName, userIcon,
								user.getUserId());
						Log.e("ImgPath", ImgPath);

					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.Close();
    }
}