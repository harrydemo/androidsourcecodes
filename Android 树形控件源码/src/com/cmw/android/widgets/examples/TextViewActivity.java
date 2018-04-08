package com.cmw.android.widgets.examples;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.cmw.android.widgets.R;
/**
 * Android 文本控件
 * 本示例将演示 TextView,EditText,AutoCompleteTextView,
 * 			   MultiCompleteTextView 等文本输入控件
 * @author chengmingwei
 *
 */
public class TextViewActivity extends Activity {
	private TextView txtDescription = null;
	private TextView txtLink = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textview_example);
		txtDescription = (TextView)findViewById(R.id.textview_desc1);
	    txtDescription.setTextSize(30);  
	    txtDescription.setHorizontallyScrolling(true);  
	    txtDescription.setFocusable(true);  
	    txtDescription.requestFocus();
	    //------------
	    txtLink = (TextView)findViewById(R.id.textview_desc2);
	    txtLink.setText("点击网址可访问开源中国[http://www.oschina.net/]" +
	    		"点击可访问我的QQ邮箱：340360491@qq.com " +
	    		"访问我的手机：13416358721");
	    Linkify.addLinks(txtLink, Linkify.ALL);
	    
	}
	
}
