package cn.itcast.db;

import java.util.List;

import cn.itcast.domain.Person;
import cn.itcast.service.OtherPersonService;
import android.test.AndroidTestCase;
import android.util.Log;

public class OtherPersonServiceTest extends AndroidTestCase {
	private static final String TAG = "PersonServiceTest";
	
	public void testSave() throws Throwable{
		OtherPersonService service = new OtherPersonService(this.getContext());
		Person person = new Person();
		person.setName("zhangxiaoxiao");
		person.setPhone("13671555567");
		service.save(person);
		
		Person person2 = new Person();
		person2.setName("laobi");
		person2.setPhone("13679993567");
		service.save(person2);
		
		Person person3 = new Person();
		person3.setName("lili");
		person3.setPhone("13888323567");
		service.save(person3);
		
		Person person4 = new Person();
		person4.setName("zhaoxiaogang");
		person4.setPhone("1367132300");
		service.save(person4);
	}
	
	public void testFind() throws Throwable{
		OtherPersonService service = new OtherPersonService(this.getContext());
		Person person = service.find(1);
		Log.i(TAG, person.toString());
	}
	
	public void testUpdate() throws Throwable{
		OtherPersonService service = new OtherPersonService(this.getContext());
		Person person = service.find(1);
		person.setName("liming");
		service.update(person);
	}
	
	public void testCount() throws Throwable{
		OtherPersonService service = new OtherPersonService(this.getContext());
		Log.i(TAG, service.getCount()+"");
	}
	
	public void testScrollData() throws Throwable{
		OtherPersonService service = new OtherPersonService(this.getContext());
		List<Person> persons = service.getScrollData(0, 3);
		for(Person person : persons){
			Log.i(TAG, person.toString());
		}
	}
	
	public void testDelete() throws Throwable{
		OtherPersonService service = new OtherPersonService(this.getContext());
		service.delete(1);
	}
}
