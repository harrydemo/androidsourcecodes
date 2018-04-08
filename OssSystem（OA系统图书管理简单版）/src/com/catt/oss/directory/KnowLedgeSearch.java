package com.catt.oss.directory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.catt.oss.R;
import com.catt.oss.common.app.BaseActivity;
import com.catt.oss.common.view.TextViewEdit;

public class KnowLedgeSearch extends BaseActivity {
	private LinearLayout registerPerson;
	private LinearLayout systemName;
	private LinearLayout systemName_short;
	private LinearLayout registerTime;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.knowledge_search);
	}
	@Override
	protected void initialComponent(BaseActivity context) {
		super.initialComponent(context);
		registerPerson=(LinearLayout) this.findViewById(R.id.linearlayout_register_person);
		systemName=(LinearLayout) this.findViewById(R.id.linearlayout_system_name);
		systemName_short=(LinearLayout) this.findViewById(R.id.linearlayout_systemname_short);
		registerTime=(LinearLayout) this.findViewById(R.id.linearlayout_register_time);
	}
	@Override
	protected void initialSetup(BaseActivity context) {
		super.initialSetup(context);
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int textwidth=dm.widthPixels/3;
		int textheight=dm.heightPixels/6;
		int editwidth=dm.widthPixels/2;
		int editheight=dm.heightPixels/6;
		TextViewEdit view1=new TextViewEdit(context, "主册人", null, dm);
		TextViewEdit view2=new TextViewEdit(context, "系统名称", null, dm);
		TextViewEdit view3=new TextViewEdit(context, "系统名字简称", null, dm);
		TextViewEdit view4=new TextViewEdit(context, "注册时间", null, dm);
		registerPerson.addView(view1);
		systemName.addView(view2);
		systemName_short.addView(view3);
		registerTime.addView(view4);
	}
	@Override
	protected void setComponentListener(BaseActivity context) {
		super.setComponentListener(context);
	}

}
