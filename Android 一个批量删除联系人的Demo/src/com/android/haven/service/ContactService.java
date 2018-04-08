package com.android.haven.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.provider.Contacts.People;

import com.android.haven.adapter.ContactEntity;
import com.android.haven.contact.ContactManagerActvity;

/**
 * @auther hujh
 * @version 2010-10-31 ÏÂÎç06:01:56
 */

public class ContactService {

	public static ContactService service;
	
	private ContactService(){
		
	}
	
	public static ContactService instance(){
		if(service == null)
			service = new ContactService();
		return service;
	}
	
	
	public List<ContactEntity> loadInfoFromPhone(ContactManagerActvity activity,int type) {
		List<ContactEntity> list = new ArrayList<ContactEntity>();
		Cursor cursor;
		Uri uri;
		if (type == 1) {
			Intent intent = new Intent();
			intent.setData(Uri.parse("content://icc/adn"));
			uri = intent.getData();
		} else
			uri = People.CONTENT_URI;

		cursor = activity.getContentResolver().query(uri, null, null, null, null);
		ContactEntity entity;
		while (cursor.moveToNext()) {
			entity = new ContactEntity();
			int peopleId = cursor.getColumnIndex(People._ID);
			int nameId = cursor.getColumnIndex(People.NAME);
			int phoneId = cursor.getColumnIndex(People.NUMBER);
			
			entity.setId(cursor.getLong(peopleId));
			entity.setName(cursor.getString(nameId));
			entity.setPhoneNum(cursor.getString(phoneId));
			list.add(entity);
		}
		return list;
	}
	
	public int deleteSelectedItem(final ContactManagerActvity activity,int position){
		Uri uri = null;
		ContactEntity entity = null;
		List<ContactEntity> list = null;
		int tab = activity.myTabHost.getCurrentTab();
		if(tab == 0)
			list = activity.phoneList;
		else
			list = activity.simList;
		entity = list.get(position);
		Message msg;
		uri = ContentUris.withAppendedId(People.CONTENT_URI, entity.getId());
		int count = activity.getContentResolver().delete(uri, null, null);
		list.remove(position);
		msg = new Message();
		msg.what = tab;
		activity.myHandler.sendMessage(msg);
		return count;
	}
	
	public void deleteSelectedItems(final ContactManagerActvity activity){
		new Thread(new Runnable(){
			public void run() {
				// TODO Auto-generated method stub
				Uri uri = null;
				ContactEntity entity;
				Message msg;
				if(activity.myTabHost.getCurrentTab() == 0)
				for(int i =0,j=activity.phoneList.size();i<j;i++){
					if(!activity.canDelete)
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					entity = activity.phoneList.get(i);
					if(entity.isChecked()){				
						uri = ContentUris.withAppendedId(People.CONTENT_URI, entity.getId());
						activity.getContentResolver().delete(uri, null, null);
						activity.phoneList.remove(i);
						i--;
						j--;
						msg = new Message();
						msg.what = 0;
						activity.canDelete = false;
						activity.myHandler.sendMessage(msg);						
					}
				}
				else
					for(int i =0,j=activity.simList.size();i<j;i++){
						if(!activity.canDelete)
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						entity = activity.simList.get(i);
						if(entity.isChecked()){				
							uri = ContentUris.withAppendedId(People.CONTENT_URI, entity.getId());
							activity.getContentResolver().delete(uri, null, null);
							activity.simList.remove(i);
							i--;
							j--;
							msg = new Message();
							msg.what = 1;
							activity.canDelete = false;
							activity.myHandler.sendMessage(msg);							
						}
					}
				msg = new Message();
				msg.what = 2;
				activity.myHandler.sendMessage(msg);		
			}			
		}).start();	
	}
	

	public void checkAll(ContactManagerActvity activity) {
		int type =activity.myTabHost.getCurrentTab();
		if (type == 0) {
			for (int i = 0, j = activity.phoneList.size(); i < j; i++) {
				activity.phoneList.get(i).setChecked(true);
				activity.phoneAdapter.notifyDataSetChanged();
			}
		} else if (type == 1) {
			for (int i = 0, j = activity.simList.size(); i < j; i++) {
				activity.simList.get(i).setChecked(true);
				activity.simAdapter.notifyDataSetChanged();
			}
		}
	}

	public void cancelAll(ContactManagerActvity activity) {
		int type = activity.myTabHost.getCurrentTab();
		if (type == 0) {
			for (int i = 0, j = activity.phoneList.size(); i < j; i++) {
				activity.phoneList.get(i).setChecked(false);
				activity.phoneAdapter.notifyDataSetChanged();
			}
		} else if (type == 1) {
			for (int i = 0, j = activity.simList.size(); i < j; i++) {
				activity.simList.get(i).setChecked(false);
				activity.simAdapter.notifyDataSetChanged();
			}
		}
	}
	
}
