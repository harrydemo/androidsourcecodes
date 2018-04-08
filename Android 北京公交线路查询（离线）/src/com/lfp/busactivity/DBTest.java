package com.lfp.busactivity;

import com.lfp.busactivity.Database;

import android.test.AndroidTestCase;

public class DBTest extends AndroidTestCase {
	public void testcreadDB() throws Throwable{
		Database database = new Database(this.getContext());
		database.getWritableDatabase();
	}

}
