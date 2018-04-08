package com.smart.dh;

import java.util.Iterator;
import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.smart.domain.Person;
import com.smart.service.PersonService;

public class PersonServiceTest extends AndroidTestCase {

	private static final String TAG="PersonServiceTest";
	
	//�������ݡ�
	public void testSave() throws Exception{
		PersonService  personService=new PersonService(this.getContext());
//		personService.save(new Person("����",(short)23));
		for (int i = 0; i < 10; i++) {
			personService.save(new Person("llb"+i,(short)(i+1)));
		}
		
		
	}
	
	
//��ѯ
	public void testFind() throws Exception{
		PersonService  personService=new PersonService(this.getContext());
		Person person=personService.find(1);
		Log.i(TAG, person.toString());
		
//		personService.save(new Person("����",(short)23));
	}
	//�������

	public void testUpdate() throws Exception{
		PersonService  personService=new PersonService(this.getContext());
		Person person=personService.find(1);
		person.setName("smart");
		personService.update(person);
		
		Log.i(TAG, person.toString());
		
	}
	//������е�����
	public void testGetCount() throws Exception{
		PersonService  personService=new PersonService(this.getContext());
		Log.i(TAG, String.valueOf(personService.getCount()));
		
	}
	
	//��ҳ����
	public void testGetScrollData() throws Exception{
		PersonService  personService=new PersonService(this.getContext());
		List<Person> persons=personService.getScrollData(0, 20);//��0����20��������
		for(Person person:persons){
			Log.i(TAG, person.toString());
		}
		
		
	}
	
	public void testDelete() throws Exception{
		PersonService  personService=new PersonService(this.getContext());
		personService.delete(1,2,3);//ɾ��1.2.3������¼
	}
	
	
	
	
	
	
	
}
