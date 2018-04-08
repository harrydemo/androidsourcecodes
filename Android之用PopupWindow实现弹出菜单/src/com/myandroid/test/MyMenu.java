package com.myandroid.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MyMenu extends Activity {
	private List<String> titles;	//������
	private List<List<String>> item_names;	//ѡ������
	private List<List<Integer>> item_images;	//ѡ��ͼ��
	private MyDefinedMenu myMenu;	//�����˵�
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //�����˵�������
        titles = addItems(new String[]{"�˵�һ", "�˵���", "�˵���"});
        //ѡ��ͼ��
        item_images = new ArrayList<List<Integer>>();
        item_images.add(addItems(new Integer[]{R.drawable.bag,
        	R.drawable.bluetooth, R.drawable.earth, R.drawable.email}));
        item_images.add(addItems(new Integer[]{R.drawable.map,
            	R.drawable.news, R.drawable.reader, R.drawable.sound, R.drawable.tape}));
        item_images.add( addItems(new Integer[]{R.drawable.telephone,
            	R.drawable.bluetooth, R.drawable.earth, R.drawable.email}));
        //ѡ������
        item_names = new ArrayList<List<String>>();
        item_names.add(addItems(new String[]{"����", "����", "������", "�ʼ�"}));
        item_names.add(addItems(new String[]{"��ͼ", "����", "�Ķ���", "����", "¼��"}));
        item_names.add(addItems(new String[]{"�绰", "����", "�Ķ���", "����"}));
        //���������˵�����
		myMenu = new MyDefinedMenu(this, titles, item_names, 
				item_images, new ItemClickEvent());
        
    }
    
    /**
     * ת��ΪList<String>
     * @param values
     * @return
     */
    private List<String> addItems(String[] values) {
    	
    	List<String> list = new ArrayList<String>();
    	for (String var : values) {
			list.add(var);
		}
    	
    	return list;
    }
    
    /**
     * ת��ΪList<Integer>
     * @param values
     * @return
     */
    private List<Integer> addItems(Integer[] values) {
    	
    	List<Integer> list = new ArrayList<Integer>();
    	for (Integer var : values) {
			list.add(var);
		}
    	
    	return list;
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		
		if(0 == myMenu.currentState && myMenu.isShowing()) {	
			Log.e("isShowing:", "" + myMenu.isShowing());
			myMenu.dismiss();			//�Ի�����ʧ
			myMenu.currentState = 1;	//���״̬������ʧ
			Log.e("MenuState:", "dismissing");
		} else {
			myMenu.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0,0);
			Log.e("isShowing:", "" + myMenu.isShowing()); 
			myMenu.currentState = 0;	//���״̬����ʾ��
			Log.e("MenuState:", "showing");
		}
		
		return false;	// true--��ʾϵͳ�Դ��˵���false--����ʾ��
	}
	
	@Override
	public void closeOptionsMenu() {
		// TODO Auto-generated method stub
		super.closeOptionsMenu();
		Log.e("MenuState:", "closeOptionsMenu");
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onOptionsMenuClosed(menu);
		Log.e("MenuState:", "onOptionsMenuClosed");
	}


	/**
	 * �˵�ѡ�����¼�
	 * @author Kobi
	 *
	 */
	class ItemClickEvent implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			//��ʾ��������ĸ��˵��ĸ�ѡ�
			Toast.makeText(MyMenu.this, "Menu: " + 
					titles.get(myMenu.getTitleIndex()) + 
					" Item: " + item_names.get(myMenu.getTitleIndex()).get(arg2),
					Toast.LENGTH_SHORT).show();
			myMenu.dismiss();	//�˵���ʧ
			myMenu.currentState = 1;	//���״̬������ʧ
			Log.e("MenuState:", "dismissing");
		}
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("Activity:", "onPause");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.e("Activity:", "onRestart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("Activity:", "onResume");
	}
	
	
	
	
	
	
	

    
    
}