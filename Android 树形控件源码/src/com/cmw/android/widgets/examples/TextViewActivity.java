package com.cmw.android.widgets.examples;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.cmw.android.widgets.R;
/**
 * Android �ı��ؼ�
 * ��ʾ������ʾ TextView,EditText,AutoCompleteTextView,
 * 			   MultiCompleteTextView ���ı�����ؼ�
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
	    txtLink.setText("�����ַ�ɷ��ʿ�Դ�й�[http://www.oschina.net/]" +
	    		"����ɷ����ҵ�QQ���䣺340360491@qq.com " +
	    		"�����ҵ��ֻ���13416358721");
	    Linkify.addLinks(txtLink, Linkify.ALL);
	    
	}
	
}
