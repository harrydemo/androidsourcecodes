/**
 * TorProxy - Anonymous data communication for Android devices
 * Copyright (C) 2009 Connell Gauld
 * 
 * Thanks to University of Cambridge,
 * 		Alastair Beresford and Andrew Rice
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */


package uk.ac.cam.cl.dtg.android.tor.TorProxy;

import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import uk.ac.cam.cl.dtg.android.tor.TorProxyTests.ControlServiceTestSuite;
import android.app.Activity;
import android.os.Bundle;
import android.test.AndroidTestRunner;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class UnitTests extends Activity implements OnClickListener {

	private ListView mTestResults = null;
	private Button mStartTests = null;
	private ArrayAdapter<String> mListAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Deal with the UI
		setContentView(R.layout.tests);
		
		mTestResults = (ListView)findViewById(R.id.testResults);
		mStartTests = (Button)findViewById(R.id.startTestsButton);
		
		mListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		mTestResults.setAdapter(mListAdapter);
		
		mStartTests.setOnClickListener(this);
	}
	
	
	
	
	private void addLogLine(final String line) {
		mTestResults.post(new Runnable() {
			@Override
			public void run() {
				mListAdapter.add(line);
			}			
		});
	}


	private class TestRunner implements Runnable, TestListener {

		@Override
		public void run() {

			AndroidTestRunner t = new AndroidTestRunner();
			t.setTest(new ControlServiceTestSuite());
			t.setContext(getApplicationContext());
			t.addTestListener(this);
			t.runTest();

		}
		
		@Override
		public void addError(Test arg0, Throwable arg1) {
			addLogLine("TEST ERROR: " + arg1.getMessage());
		}

		@Override
		public void addFailure(Test test, AssertionFailedError t) {
			StringWriter p = new StringWriter();
			t.printStackTrace(new PrintWriter(p));
			addLogLine("TEST FAILURE: " + p.toString());
		}

		@Override
		public void endTest(Test test) {
			addLogLine("Test ended: " + test.getClass().getName());
		}

		@Override
		public void startTest(Test test) {
			addLogLine("Test started: " + test.getClass().getName());
		}
		
	}


	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.startTestsButton) {
			Thread t = new Thread(new TestRunner());
			t.start();
		}
	}
	
}
