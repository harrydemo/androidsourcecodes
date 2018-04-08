package weibo4andriod.andriodexamples;

import java.util.List;

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OAuthActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		Uri uri=this.getIntent().getData();
		try {
			RequestToken requestToken= OAuthConstant.getInstance().getRequestToken();
			AccessToken accessToken=requestToken.getAccessToken(uri.getQueryParameter("oauth_verifier"));
			OAuthConstant.getInstance().setAccessToken(accessToken);
			TextView textView = (TextView) findViewById(R.id.TextView01);
			textView.setText("得到AccessToken的key和Secret,可以使用这两个参数进行授权登录了.\n Access token:\n"+accessToken.getToken()+"\n Access token secret:\n"+accessToken.getTokenSecret());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
		Button button=  (Button) findViewById(R.id.Button01);
		button.setText("显示FriendTimeline");
		button.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
    				Weibo weibo=OAuthConstant.getInstance().getWeibo();
    				weibo.setToken(OAuthConstant.getInstance().getToken(), OAuthConstant.getInstance().getTokenSecret());
    				List<Status> friendsTimeline;
    					try {
							friendsTimeline = weibo.getFriendsTimeline();
							StringBuilder stringBuilder = new StringBuilder("1");
	    					for (Status status : friendsTimeline) {
	    						stringBuilder.append(status.getUser().getScreenName() + "说:"
	    								+ status.getText() + "\n");
	    					}
	    					TextView textView = (TextView) findViewById(R.id.TextView01);
	    					textView.setText(stringBuilder.toString());
						} catch (WeiboException e) {
							e.printStackTrace();
						}
    					
            }
        } );
		
	}
}
