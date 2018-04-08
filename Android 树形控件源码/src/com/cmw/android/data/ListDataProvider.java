package com.cmw.android.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;

import com.cmw.android.widgets.examples.EditTextViewActivity;
import com.cmw.android.widgets.examples.TextViewActivity;

/**
 * ��������ṩ����
 * @author chengmingwei
 *
 */
public class ListDataProvider implements DataProvider {
	private Context context=null;
	private List<Map<String,String>> dataSource = new ArrayList<Map<String,String>>();
	private Map<String,FowardAction> actionMap = new HashMap<String, FowardAction>();
	private static final String[] CAPTIONS = {"�ı��ؼ�","EditTextView ʾ��"};
	private static final String[] SRC = {"TextViewActivity","EditTextViewActivity"};
	@SuppressWarnings("unchecked")
	private static final Class[] CLS = {TextViewActivity.class,EditTextViewActivity.class};
	
	
	public ListDataProvider(Context context) {
		this.context = context;
	}

	public void loadData(){
		int i=0;
		for(String caption : CAPTIONS){
			Map<String,String> map = new HashMap<String, String>();
			map.put("caption", caption);
			String src = SRC[i];
			map.put("src", src);
			loadDataSource(map);
			loadActionMap(caption,i);
			i++;
		}
	}
	
	/**
	 * ����ListView��ѡ�е� �б� caption ��ת��ָ����Activity
	 * @param caption	ѡ�е��б� caption
	 * @throws Exception
	 */
	public void foward(String caption) throws Exception{
		FowardAction action = actionMap.get(caption);
		if(null == action) throw new Exception("���棺"+caption+"����Ӧ�� Activity �����ڣ��޷���ת��");
		action.execute();
	}
	
	private void loadDataSource(Map<String,String> map){
		dataSource.add(map);
	}
	
	@SuppressWarnings("unchecked")
	private void loadActionMap(String key,int index){
		final Class tagCls = CLS[index];
		FowardAction action = new FowardAction(){
			public void execute() {
				Intent intent = new Intent();
				intent.setClass(context, tagCls);
				context.startActivity(intent);
			}
		};
		actionMap.put(key, action);
	}
	
	public List<Map<String, String>> getDataSource() {
		return dataSource;
	}

	public void setDataSource(List<Map<String, String>> dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * 
	 * @author chengmingwei
	 *
	 */
	public interface FowardAction{
		public void execute();
	}
}
