package com.android.haven.contact;

import java.util.List;

import com.android.haven.adapter.ContactAdapter;
import com.android.haven.adapter.ContactEntity;
import com.android.haven.listener.ContactListener;
import com.android.haven.service.ContactService;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

public class ContactManagerActvity extends TabActivity {
	public TabHost myTabHost;
	private ListView phoneListView;
	private ListView simListView;
	public ContactAdapter phoneAdapter;
	public ContactAdapter simAdapter;
	public List<ContactEntity> phoneList;
	public List<ContactEntity> simList;
	public boolean canDelete = true;
	private ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_manager);

		myTabHost = getTabHost();
		myTabHost.addTab(myTabHost
				.newTabSpec("0")
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						return ContactManagerActvity.this
								.findViewById(R.id.phone_contacts);
					}
				})
				.setIndicator(
						"手机联系人",
						ContactManagerActvity.this.getResources().getDrawable(
								R.drawable.phone)));

		myTabHost.addTab(myTabHost
				.newTabSpec("1")
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						return ContactManagerActvity.this
								.findViewById(R.id.sim_contacts);
					}
				})
				.setIndicator(
						"SIM联系人",
						ContactManagerActvity.this.getResources().getDrawable(
								R.drawable.sim)));
		

		TabWidget widget = getTabWidget();

		View v0 = widget.getChildAt(0);
		View v1 = widget.getChildAt(1);

		v0.getLayoutParams().height = 58;
		v1.getLayoutParams().height = 58;

		phoneListView = (ListView) findViewById(R.id.phone_contacts);
		simListView = (ListView) findViewById(R.id.sim_contacts);
		
		phoneListView.setOnItemClickListener(ContactListener.instance().new MyListItemOnClickListener(this));
		simListView.setOnItemClickListener(ContactListener.instance().new MyListItemOnClickListener(this));
		
		phoneList = ContactService.instance().loadInfoFromPhone(this,0);
		phoneAdapter = new ContactAdapter(ContactManagerActvity.this, phoneList);
		phoneListView.setAdapter(phoneAdapter);

		simList = ContactService.instance().loadInfoFromPhone(this,1);
		simAdapter = new ContactAdapter(ContactManagerActvity.this, simList);
		simListView.setAdapter(simAdapter);
		
		myTabHost.setCurrentTab(0);
		myTabHost.setCurrentTab(1);
		myTabHost.setCurrentTab(0);
		dialog = new ProgressDialog(this);
		dialog.setMessage("删除中！");		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "选择全部").setIcon(
				getResources().getDrawable(R.drawable.choose_all));
		menu.add(0, 2, 0, "取消选中").setIcon(
				getResources().getDrawable(R.drawable.cancel_all));
		menu.add(0, 3, 0, "删除选中").setIcon(
				getResources().getDrawable(R.drawable.delete));
		menu.add(0, 4, 0, "推出系统").setIcon(
				getResources().getDrawable(R.drawable.exit));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			ContactService.instance().checkAll(this);
			break;
		case 2:
			ContactService.instance().cancelAll(this);
			break;
		case 3:
			dialog.show();
			ContactService.instance().deleteSelectedItems(this);
			break;
		case 4:
			this.finish();
			System.exit(0);
			break;
		default:
			break;
		}
		return true;
	}

	public Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			ContactEntity entity = null;
			Intent intent = null;
			Uri uri = null;
			switch(msg.what){
			case 0:
				phoneAdapter.notifyDataSetChanged();
				canDelete = true;
				break;
			case 1:
				simAdapter.notifyDataSetChanged();
				canDelete = true;
				break;
			case 2:
				dialog.cancel();
				Toast.makeText(getApplicationContext(), "删除操作结束！", Toast.LENGTH_LONG).show();
				break;
			case 10:
				
				if(myTabHost.getCurrentTab() == 0)
					entity = phoneList.get(msg.arg1);
				else 
					entity = simList.get(msg.arg1);
				if(entity.getPhoneNum() == null || entity.getPhoneNum().equals("") ){
					Toast.makeText(ContactManagerActvity.this, "号码格式错误！", Toast.LENGTH_LONG).show();
					ContactListener.instance().dialog.cancel();
					return;
				}
				uri = Uri.parse("tel:"+entity.getPhoneNum());
				intent = new Intent(Intent.ACTION_CALL,uri);
				startActivity(intent);
				ContactListener.instance().dialog.cancel();
				break;
			case 11:
				if(myTabHost.getCurrentTab() == 0)
					entity = phoneList.get(msg.arg1);
				else 
					entity = simList.get(msg.arg1);
				if(entity.getPhoneNum() == null || entity.getPhoneNum().equals("")){
					Toast.makeText(ContactManagerActvity.this, "号码格式错误！", Toast.LENGTH_LONG).show();
					ContactListener.instance().dialog.cancel();
					return;
				}
				uri = Uri.parse("smsto:"+entity.getPhoneNum());
				intent = new Intent(Intent.ACTION_SENDTO,uri);
				startActivity(intent);
				ContactListener.instance().dialog.cancel();
				break;
			case 12:
				if(ContactService.instance().deleteSelectedItem(ContactManagerActvity.this, msg.arg1) == 1)
					Toast.makeText(ContactManagerActvity.this, "删除成功！", Toast.LENGTH_LONG).show();
				ContactListener.instance().dialog.cancel();
				break;
			case 13:
				ContactListener.instance().dialog.cancel();
				break;
			default:
				break;
			}
		}
	};
}