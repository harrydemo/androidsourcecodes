package z.s.test;

import java.util.List;

import android.content.Intent;

public class MyApplication extends android.app.Application {

	
	private List<ContactInfo> ContactInfo;
	
	public List<ContactInfo> getContactInfo() {
		return ContactInfo;
	}

	public void setContactInfo(List<ContactInfo> contactInfo) {
		ContactInfo = contactInfo;
	}

	public void onCreate() {
		
		System.out.println("¿ªÊ¼ÁË");
		
		Intent startService = new Intent(MyApplication.this, T9Service.class);
		startService(startService);
	}
}
