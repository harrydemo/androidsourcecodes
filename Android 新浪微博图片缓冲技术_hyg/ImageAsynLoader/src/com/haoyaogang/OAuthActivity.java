package com.haoyaogang;

import java.util.List;

import weibo4android.Paging;
import weibo4android.Status;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class OAuthActivity extends Activity {
	
	private static final int  SHOW_LIST_VIEW_MSG = 1;
	
	private ListView listView;
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			//handle the message of showing the data of the list view
			//SHOW_LIST_VIEW_MSG
			if(SHOW_LIST_VIEW_MSG == msg.what)
			{
				List<Status> data = (List<Status>)msg.obj;
				UserInfoAdapter userInfoAdapter = new UserInfoAdapter(OAuthActivity.this,data);					
				
				listView.setAdapter(userInfoAdapter);
			}
			
		};
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		Uri uri=this.getIntent().getData();
		try {
			RequestToken requestToken= OAuthConstant.getInstance().getRequestToken();
			AccessToken accessToken=requestToken.getAccessToken(uri.getQueryParameter("oauth_verifier"));
			OAuthConstant.getInstance().setAccessToken(accessToken);
			//TextView textView = (TextView) findViewById(R.id.TextView01);
			//textView.setText("已经获取到Access token:\n"+accessToken.getToken()+"\n Access token secret:\n"+accessToken.getTokenSecret());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		//list view to show detail infomation
		
		listView = (ListView) this.findViewById(R.id.list_view_id);
		
		//init button 
		Button button=  (Button) findViewById(R.id.Button01);
		button.setText("显示列表");
		button.setOnClickListener(new Button.OnClickListener()
        {

            public void onClick( View v )
            {
    				Weibo weibo=OAuthConstant.getInstance().getWeibo();
    				weibo.setToken(OAuthConstant.getInstance().getToken(), OAuthConstant.getInstance().getTokenSecret());
    				List<Status> friendsTimeline = null;
//    					try {
//							//friendsTimeline = weibo.getTrendStatus("seaeast", new Paging(1,20));
//    						friendsTimeline = weibo.getFriendsTimeline();
//    						StringBuilder stringBuilder = new StringBuilder("");
//	    					for (Status status : friendsTimeline) {
//	    						stringBuilder.append(status.getUser().getScreenName() + "\n"
//	    								+ status.getText() + "\n--------------------------------------------------\n");
//	    					}
//	    					TextView textView = (TextView) findViewById(R.id.TextView01);
//	    					textView.setText(stringBuilder.toString());
//						} catch (WeiboException e) {
//							e.printStackTrace();
//						}
    					
    				//get the time line of the friends
    				try {
						friendsTimeline = weibo.getFriendsTimeline();
					} catch (WeiboException e) {
						e.printStackTrace();
					}
    				//set the adapter for the list view
					if(null != friendsTimeline)
					{
	    				Message msg = new Message();
	    				msg.obj = friendsTimeline;
	    				msg.what = SHOW_LIST_VIEW_MSG;
	    				handler.sendMessage(msg);
					}
					if(R.id.Button01 == v.getId())
					{
						v.setVisibility(View.GONE);
					}
    				
            }
        } );
//		
	}
}
