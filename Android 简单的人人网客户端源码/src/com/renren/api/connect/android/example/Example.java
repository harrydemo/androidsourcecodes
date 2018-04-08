package com.renren.api.connect.android.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.bean.FeedParam;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.view.ConnectButton;
/**
 * 主要用于测试校内的API
 * @author Administrator
 *
 */

public class Example extends Activity implements OnCheckedChangeListener {
                                 //用户说有信息段
    public static final String USER_FULL_FIELDS = "name,email_hash, sex,star,birthday,tinyurl,headurl,mainurl,hometown_location,hs_history,university_history,work_history,contact_info";
                                  //用户基本数据段
    public static final String USER_COMMON_FIELDS = "name,email_hash,sex,star,birthday,tinyurl,headurl,mainurl";

    /** Called when the activity is first created. */
    TextView display;

    ConnectButton login;          //调用自定义的Button

    RadioGroup dataFormatGroup;   //单选按钮

    String dataFormat = "json";   //数据格式是JSON
                                  //API的KEY
    private String apiKey = "a3c29006929243e19547fba648581427";
                                  //API的Secret
    private String apiSecret ="7eb0e66e829b4c66975ee5c0141a87fc";

    Renren renren;                //人人实例

    AsyncRenren asyncRenren;      //同步人人

    SimpleRequestListener simpleRequestListener;   //监听

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (apiKey == null || apiSecret == null) {  
            Util.showAlert(this, "警告", "人人应用的apiKey和apiSecret必须提供！");
        }

 

        this.renren = new Renren(this, apiKey, apiSecret);
        this.asyncRenren = new AsyncRenren(renren);

        this.display = (TextView) findViewById(R.id.display);

        this.login = (ConnectButton) findViewById(R.id.login);

        this.dataFormatGroup = (RadioGroup) findViewById(R.id.dataFormatGroup);
        this.dataFormatGroup.check(R.id.JSON);
        this.dataFormatGroup.setOnCheckedChangeListener(this);

        this.login.init(renren);
        this.login.setConnectButtonListener(new TestConnectButtonListener(this));

        this.simpleRequestListener = new SimpleRequestListener(this);

    }

    public void onClick(View v) {
        Log.i("tag", "view.id:" + v.getId());

        if (v.getId() == R.id.display) {
            String text = "sessionKey:" + renren.getSessionKey();
            this.display.setText(text);
        } else if (v.getId() == R.id.getUser) {
            Bundle params = new Bundle();
            params.putString("method", "users.getInfo");
            String uids = 1+"";
            params.putString("uids", uids);
            String fields = USER_FULL_FIELDS;
            params.putString("fields", fields);
            simpleRequestListener.showProgress("获取用户信息");
            this.asyncRenren.request(params, simpleRequestListener, dataFormat);
        } else if (v.getId() == R.id.postFeed) {
            FeedParam feedParam = new FeedParam();
            feedParam.setTemplateId(1);
            feedParam.setUserMessage("手机客户端的测试");
            feedParam.setUserMessagePrompt("功能测试");
            feedParam.setBodyGeneral("此新鲜事由testMobile发布");
            feedParam.setTemplateData(this.getTemplateData());
            this.renren.feed(feedParam, new PostFeedListener(this));
        } else if (v.getId() == R.id.getFriend) {
            Bundle params = new Bundle();
            params.putString("method", "friends.getFriends");
            params.putString("page", "1");
            params.putString("count", "10");
            //simpleRequestListener.showProgress("获取好友");
            //this.asyncRenren.request(params, simpleRequestListener, dataFormat);
            FriendRequestListener listener = new FriendRequestListener(this, dataFormat);
            this.asyncRenren.request(params, listener, dataFormat);
        }
        ///////////////Test My Code bellow /////////////////////////
        else if(v.getId()==R.id.test)
        {
        	Bundle params=new Bundle();
        	params.putString("method","admin.getAllocation");
        	String resp = renren.request(params, dataFormat);
        	RenrenError rrError = Util.parseRenrenError(resp, dataFormat);
				
        }
         
        ////////////////Test My Code Above ////////////////////////////
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton rb = (RadioButton) findViewById(group.getCheckedRadioButtonId());
        this.dataFormat = rb.getText().toString();
        Toast.makeText(this, "Server return " + this.dataFormat + " string", Toast.LENGTH_SHORT)
                .show();
    }

    private String getTemplateData() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("feedtype", "testMobile");
            obj.put("content", "首长好，为人民服务！");
            obj.put("android", "milestone");
            JSONArray images = new JSONArray();
            JSONObject img = new JSONObject();
            img.put("src", "http://www.android.com/images/froyo.png");
            img.put("href", "http://www.android.com/");
            images.put(img);
            obj.put("images", images);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
