package weibo4andriod.andriodexamples;

import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AndriodExample extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	Button beginOuathBtn=  (Button) findViewById(R.id.Button01);
    	

    	beginOuathBtn.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
            	Weibo weibo = OAuthConstant.getInstance().getWeibo();
            	RequestToken requestToken;
				try {
					requestToken =weibo.getOAuthRequestToken("weibo4andriod://OAuthActivity");
	    			Uri uri = Uri.parse(requestToken.getAuthenticationURL()+ "&from=xweibo");
	    			OAuthConstant.getInstance().setRequestToken(requestToken);
	    			startActivity(new Intent(Intent.ACTION_VIEW, uri));
				} catch (WeiboException e) {
					e.printStackTrace();
				}
    			
            }
        } );
	}
}