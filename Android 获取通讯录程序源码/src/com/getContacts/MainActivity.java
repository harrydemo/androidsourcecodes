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
		Uri uri=ContactsContract.Data.CONTENT_URI;//2.0����ϵͳʹ��ContactsContract.Data������ϵ��
		Cursor cursor=getContentResolver().query(uri, null, null, null, "display_name");//��ʾ��ϵ��ʱ����ʾ��������    	
        cursor.moveToFirst();
        List<Person> list=new ArrayList<Person>();
        
        int Index_CONTACT_ID = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);//���CONTACT_ID��ContactsContract.Data�е�����
        int Index_DATA1 = cursor.getColumnIndex(ContactsContract.Data.DATA1);//���DATA1��ContactsContract.Data�е�����
        int Index_MIMETYPE = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);//���MIMETYPE��ContactsContract.Data�е�����
        
        while(cursor.getCount()>cursor.getPosition())
        {
     	    Person person = null;
        	String id=cursor.getString(Index_CONTACT_ID);//���CONTACT_ID�е�����
        	String info=cursor.getString(Index_DATA1);//���DATA1�е�����
            String mimeType=cursor.getString(Index_MIMETYPE);//���MIMETYPE�е�����
            
            //������ѯ��ǰ�ж�Ӧ����ϵ����Ϣ�Ƿ�����ӵ�list��
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
        	if(mimeType.equals("vnd.android.cursor.item/email_v2"))//��������Ϊ����
        	{
        		person.setEmail(info);
        	}
        	else if(mimeType.equals("vnd.android.cursor.item/postal-address_v2"))//��������Ϊ��ַ
        	{
        		person.setAddress(info);
        	}
        	else if(mimeType.equals("vnd.android.cursor.item/phone_v2"))//��������Ϊ�绰����
        	{
        		person.addPhone(info);
        	}
        	else if(mimeType.equals("vnd.android.cursor.item/name"))//��������Ϊ����
        	{
        		person.setName(info);
        	}
            cursor.moveToNext();
        }
		return list;
	}
}