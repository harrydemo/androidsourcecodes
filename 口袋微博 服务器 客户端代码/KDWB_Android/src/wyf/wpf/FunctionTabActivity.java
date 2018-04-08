package wyf.wpf;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class FunctionTabActivity extends TabActivity{
	static final int MENU_SEARCH = 0;
	static final int MENU_EXIT = 1;
	String uno = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();		//���������Activity��Intent
		uno = intent.getStringExtra("uno");
		final TabHost tabHost = getTabHost();
		Intent intentPublish = new Intent(this,wyf.wpf.PublishActivity.class);
		intentPublish.putExtra("uno", uno);
		Intent intent1 = new Intent(this,wyf.wpf.ContactsActivity.class);
		intent1.putExtra("type", 0);			//typeΪ1��ʾ�����б�
		intent1.putExtra("uno", uno);
		Intent intent2 = new Intent(this,wyf.wpf.ContactsActivity.class);
		intent2.putExtra("type", 1);			//typeΪ2��ʾ����ÿ�
		intent2.putExtra("uno", uno);
		Intent iDiary = new Intent(this,wyf.wpf.MyDiaryActivity.class);
		iDiary.putExtra("uno", uno);		//Ϊ���û�id����ΪIntent��Extra
		Intent iAlbum = new Intent(this,wyf.wpf.MyAlbumListActivity.class);
		iAlbum.putExtra("uno", uno);
		
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator("���ٷ���", getResources().getDrawable(R.drawable.publish))
				.setContent(intentPublish)
				);
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("�ҵĺ���", getResources().getDrawable(R.drawable.friend))
				.setContent(intent1)
				);
		tabHost.addTab(tabHost.newTabSpec("tab3")
				.setIndicator("����ÿ�", getResources().getDrawable(R.drawable.visitor))
				.setContent(intent2)
				);	
		tabHost.addTab(tabHost.newTabSpec("tab4")
				.setIndicator("��־�б�",getResources().getDrawable(R.drawable.diary))
				.setContent(iDiary)
				);
		tabHost.addTab(tabHost.newTabSpec("tab5")
				.setIndicator("����б�", getResources().getDrawable(R.drawable.album))
				.setContent(iAlbum)
				);
		String tab = intent.getStringExtra("tab");
		if(tab != null){
			tabHost.setCurrentTabByTag(tab);
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_SEARCH, 0, "����")
			.setIcon(R.drawable.search);
		menu.add(0, MENU_EXIT, 0, "�˳�")
			.setIcon(R.drawable.exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case MENU_SEARCH:				//���������˵�ѡ��
			Intent intent = new Intent(this,SearchActivity.class);		//����Intent
			intent.putExtra("visitor", uno);
			startActivity(intent);
			break;
		case MENU_EXIT:					//�����˳��˵�ѡ��
			new AlertDialog.Builder(this)
			.setTitle("��ʾ")
			.setMessage("ȷ���˳���")
			.setIcon(R.drawable.alert_icon)
			.setPositiveButton(
					"ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							android.os.Process.killProcess(android.os.Process.myPid());		//��������
						}
					})
			.setNegativeButton(
					"ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					})
			.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}