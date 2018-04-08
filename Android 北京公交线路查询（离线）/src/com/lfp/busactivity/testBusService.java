package com.lfp.busactivity;

import com.lfp.service.BusService;

import com.lfp.domain.Bus;
import android.test.AndroidTestCase;
import android.util.Log;

public class testBusService extends AndroidTestCase {
	//���Ա���
	public void testSave() throws Throwable{
		BusService busService = new BusService(this.getContext());
		//����������
		for(int i=0;i<10;i++){
			Bus bus = new Bus();
			busService.save(bus);
		}
	}
	//���Բ���
	private static final String TAG = "BusServiceTest";
	public void testFind() throws Throwable{
		BusService busService = new BusService(this.getContext());
		//����������
		Bus bus = busService.find("2·");
		Log.i(TAG, bus.toString());
	}

}
