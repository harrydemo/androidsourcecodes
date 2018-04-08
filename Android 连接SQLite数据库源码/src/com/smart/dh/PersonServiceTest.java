package com.smart.dh;

import java.util.Iterator;
import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.smart.domain.Person;
import com.smart.service.PersonService;

public class PersonServiceTest extends AndroidTestCase {

	private static final String TAG="PersonServiceTest";
	
	//保存数据。
	public void testSave() throws Exception{
		PersonService  personService=new PersonService(this.getContext());
//		personService.save(new Person("老梁",(short)23));
		for (int i = 0; i < 10; i++) {
			personService.save(new Person("llb"+i,(short)(i+1)));
		}
		
		
	}
	
	
//查询
	public void testFind() throws Exception{
		PersonService  personService=new PersonService(this.getContext());
		Person person=personService.find(1);
		Log.i(TAG, person.toString());
		
//		personService.save(new Person("老梁",(short)23));
	}
	//更新语句

	public void testUpdate() throws Exception{
		PersonService  personService=new PersonService(this.getContext());
		Person person=personService.find(1);
		person.setName("smart");
		personService.update(person);
		
		Log.i(TAG, person.toString());
		
	}
	//获得所有的条数
	public void testGetCount() throws Exception{
		PersonService  personService=new PersonService(this.getContext());
		Log.i(TAG, String.valueOf(personService.getCount()));
		
	}
	
	//分页功能
	public void testGetScrollData() throws Exception{
		PersonService  personService=new PersonService(this.getContext());
		List<Person> persons=personService.getScrollData(0, 20);//从0条到20条的数据
		for(Person person:persons){
			Log.i(TAG, person.toString());
		}
		
		
	}
	
	public void testDelete() throws Exception{
		PersonService  personService=new PersonService(this.getContext());
		personService.delete(1,2,3);//删除1.2.3三条记录
	}
	
	
	
	
	
	
	
}
