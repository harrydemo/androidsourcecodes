package cn.itcast.html;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.itcast.domain.Contact;
import cn.itcast.service.ContactService;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends Activity {
    private WebView webview;
    private ContactService contactService;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        contactService = new ContactService();

        webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new ContactPlugin(), "itcast");
      //  webview.loadUrl("file:///android_asset/index.html");
        webview.loadUrl("http://192.168.1.10:8080/videoweb/index.html");
    }
    
    private class ContactPlugin{
    	public void getContacts(){
    		List<Contact> contacts = contactService.getContacts();//得到联系人数据
    		try {
				JSONArray array = new JSONArray();
				for(Contact contact : contacts){
					JSONObject item = new JSONObject();
					item.put("id", contact.getId());
					item.put("name", contact.getName());
					item.put("mobile", contact.getMobile());
					array.put(item);
				}
				String json = array.toString();//转成json字符串
				webview.loadUrl("javascript:show('"+ json +"')");
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	}
    	
    	public void call(String mobile){
    		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ mobile));
    		startActivity(intent);
    	}
    }
}