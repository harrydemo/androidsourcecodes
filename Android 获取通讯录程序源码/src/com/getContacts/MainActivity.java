package com.getContacts;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;

public class MainActivity extends Activity 
{
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        ListView listView=new ListView(this);
        PersonList list2=new PersonList(this,getPerson());
        listView.setAdapter(list2);
        setContentView(listView);
    }
    
	public List<Person> getPerson() 
	{
		Uri uri=ContactsContract.Data.CONTENT_URI;//2.0以上系统使用ContactsContract.Data访问联系人
		Cursor cursor=getContentResolver().query(uri, null, null, null, "display_name");//显示联系人时按显示名字排序    	
        cursor.moveToFirst();
        List<Person> list=new ArrayList<Person>();
        
        int Index_CONTACT_ID = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);//获得CONTACT_ID在ContactsContract.Data中的列数
        int Index_DATA1 = cursor.getColumnIndex(ContactsContract.Data.DATA1);//获得DATA1在ContactsContract.Data中的列数
        int Index_MIMETYPE = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);//获得MIMETYPE在ContactsContract.Data中的列数
        
        while(cursor.getCount()>cursor.getPosition())
        {
     	    Person person = null;
        	String id=cursor.getString(Index_CONTACT_ID);//获得CONTACT_ID列的内容
        	String info=cursor.getString(Index_DATA1);//获得DATA1列的内容
            String mimeType=cursor.getString(Index_MIMETYPE);//获得MIMETYPE列的内容
            
            //遍历查询当前行对应的联系人信息是否已添加到list中
        	for(int n = 0; n<list.size(); n++)
        	{
        		if(list.get(n).getID() != null)
        		{
        			if(list.get(n).getID().equals(id))
        			{
        				person = list.get(n);
        				break;
        			}
        		}
        	}
        	
        	if(person == null)
        	{
        		person=new Person();
    			person.setID(id);
    			list.add(person);
        	}
        	if(mimeType.equals("vnd.android.cursor.item/email_v2"))//该行数据为邮箱
        	{
        		person.setEmail(info);
        	}
        	else if(mimeType.equals("vnd.android.cursor.item/postal-address_v2"))//该行数据为地址
        	{
        		person.setAddress(info);
        	}
        	else if(mimeType.equals("vnd.android.cursor.item/phone_v2"))//该行数据为电话号码
        	{
        		person.addPhone(info);
        	}
        	else if(mimeType.equals("vnd.android.cursor.item/name"))//该行数据为名字
        	{
        		person.setName(info);
        	}
            cursor.moveToNext();
        }
		return list;
	}
}