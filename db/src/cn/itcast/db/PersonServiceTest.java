package cn.itcast.db;

import java.util.List;

import cn.itcast.domain.Person;
import cn.itcast.service.DBOpenHelper;
import cn.itcast.service.PersonService;
import android.test.AndroidTestCase;
import android.util.Log;

public class PersonServiceTest extends AndroidTestCase {
	private static final String TAG = "PersonServiceTest";
	
	public void testDBCreate() throws Throwable{
		DBOpenHelper dbOpenHelper = new DBOpenHelper(this.getContext());
		dbOpenHelper.getWritableDatabase();
	}
	
	public void testSave() throws Throwable{
		PersonService service = new PersonService(this.getContext());
		Person person = new Person();
		person.setName("zhangxiaoxiao");
		person.setPhone("13671555567");
		person.setAmount(100);
		service.save(person);
		
		Person person2 = new Person();
		person2.setName("laobi");
		person2.setPhone("13679993567");
		person2.setAmount(50);
		service.save(person2);
		
		Person person3 = new Person();
		person3.setName("lili");
		person3.setPhone("13888323567");
		person3.setAmount(80);
		service.save(person3);
		
		Person person4 = new Person();
		person4.setName("zhaoxiaogang");
		person4.setPhone("1367132300");
		person4.setAmount(90);
		service.save(person4);
	}
	
	public void testFind() throws Throwable{
		PersonService service = new PersonService(this.getContext());
		Person person = service.find(1);
		Log.i(TAG, person.toString());
	}
	
	public void testUpdate() throws Throwable{
		PersonService service = new PersonService(this.getContext());
		Person person = service.find(1);
		person.setName("liming");
		service.update(person);
	}
	
	public void testCount() throws Throwable{
		PersonService service = new PersonService(this.getContext());
		Log.i(TAG, service.getCount()+"");
	}
	
	public void testScrollData() throws Throwable{
		PersonService service = new PersonService(this.getContext());
		List<Person> persons = service.getScrollData(0, 3);
		for(Person person : persons){
			Log.i(TAG, person.toString());
		}
	}
	
	public void testDelete() throws Throwable{
		PersonService service = new PersonService(this.getContext());
		service.delete(1);
	}
	
	public void testPayment() throws Throwable{
		PersonService service = new PersonService(this.getContext());
		service.payment();
	}
	
}
