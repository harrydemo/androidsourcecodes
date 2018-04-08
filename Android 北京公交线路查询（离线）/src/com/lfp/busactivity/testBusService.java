package com.lfp.busactivity;

import com.lfp.service.BusService;

import com.lfp.domain.Bus;
import android.test.AndroidTestCase;
import android.util.Log;

public class testBusService extends AndroidTestCase {
	//测试保存
	public void testSave() throws Throwable{
		BusService busService = new BusService(this.getContext());
		//传入上下文
		for(int i=0;i<10;i++){
			Bus bus = new Bus();
			busService.save(bus);
		}
	}
	//测试查找
	private static final String TAG = "BusServiceTest";
	public void testFind() throws Throwable{
		BusService busService = new BusService(this.getContext());
		//传入上下文
		Bus bus = busService.find("2路");
		Log.i(TAG, bus.toString());
	}

}
