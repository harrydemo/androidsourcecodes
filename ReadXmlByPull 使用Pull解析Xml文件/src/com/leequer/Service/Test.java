package com.leequer.Service;


import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import com.leequer.Doem.Person;


import android.test.AndroidTestCase;
import android.util.Log;

public class Test extends AndroidTestCase {
	private String PERSONSTRING = "ObjectPerson";
	public void testReadXml() throws Exception
	{//Àà×°ÔØÆ÷
		InputStream inputStream = Test.class.getClassLoader().getResourceAsStream("NewFile.xml");
		
		List <Person> personsList = ReadXmlByPullService.ReadXmlByPull(inputStream);
		Log.e("abc", personsList.get(0).getName());
		
		for (Iterator iterator = personsList.iterator(); iterator.hasNext();) {
			Person person = (Person) iterator.next();
			Log.i(PERSONSTRING, person.toString());
		}
		
	}
}
