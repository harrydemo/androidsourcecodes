/*******************************************************************************
 * Copyright (c) 2009 Ferenc Hechler - ferenc_hechler@users.sourceforge.net
 * 
 * This file is part of the Android Battery Dog
 *
 * The Android Battery Dog is free software;
 * you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version.
 * 
 * The Android Battery Dog is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Android Battery Dog;
 * if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *  
 *******************************************************************************/
package net.sf.andbatdog.batterydog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BatteryDog extends Activity {

	private static final int OUTPUT_LINES = 100;
	private static final int LINE_LENGTH = 50;

	private static final String TAG = "BATDOG";
	
	private Button btStart;
	private Button btStop;
	private Button btRawFormat;
	private Button btShowFormated;
	private Button btGraph;
	private EditText mOutput;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_dog);
        mOutput= (EditText) findViewById(R.id.output);
        
        // find buttons in view
        btStart = ((Button) findViewById(R.id.btStart));
        btStop = ((Button) findViewById(R.id.btStop));
        btRawFormat= ((Button) findViewById(R.id.btRawFormat));
        btShowFormated= ((Button) findViewById(R.id.btShowFormated));
        btGraph = ((Button) findViewById(R.id.btGraph));

        // set actions for buttons
        btStart.setOnClickListener(StartServiceListener);
        btStop.setOnClickListener(StopServiceListener);
        btRawFormat.setOnClickListener(RawFormatListener);
        btShowFormated.setOnClickListener(ShowFormatedListener);
        btGraph.setOnClickListener(GraphListener);
	}

    OnClickListener StartServiceListener = new OnClickListener() {
        public void onClick(View v) {
            try {
	            startService(new Intent(BatteryDog.this, BatteryDog_Service.class));
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
                Toast.makeText(BatteryDog.this, "Start Service failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
			}
        }
    };

    
    
    OnClickListener StopServiceListener = new OnClickListener() {
        public void onClick(View v) {
        	try {
	            stopService(new Intent(BatteryDog.this, BatteryDog_Service.class));
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
	            Toast.makeText(BatteryDog.this, "Stop Service failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
			}
       	}
    };

    OnClickListener RawFormatListener = new OnClickListener() {
        public void onClick(View v) {
        	updateLog(false);
       	}
    };

    OnClickListener ShowFormatedListener = new OnClickListener() {
        public void onClick(View v) {
        	updateLog(true);
       	}
    };

    OnClickListener GraphListener = new OnClickListener() {
        public void onClick(View v) {
        	startActivity(new Intent(BatteryDog.this, BatteryGraph.class));
       	}
    };


    private void updateLog(boolean doFormat) {
		try {
			File root = Environment.getExternalStorageDirectory();
			if (root == null)
		    	throw new Exception("external storage dir not found");
			File batteryLogFile = new File(root,BatteryDog_Service.LOGFILEPATH);
			if (!batteryLogFile.exists())
				throw new Exception("logfile '"+batteryLogFile+"' not found");
			if (!batteryLogFile.canRead())
				throw new Exception("logfile '"+batteryLogFile+"' not readable");
			long len = batteryLogFile.length();
			int size = (int)Math.min((long)OUTPUT_LINES*LINE_LENGTH, len);
			StringBuffer text = new StringBuffer(size);
			FileReader reader = new FileReader(batteryLogFile);
			BufferedReader in = new BufferedReader(reader);
			if (doFormat) {
				text.append(in.readLine()).append("\n");
			}
			if (len > OUTPUT_LINES*LINE_LENGTH) {
				in.skip(len-OUTPUT_LINES*LINE_LENGTH);
				// skip incomplete line
				in.readLine();
			}
			String line = in.readLine();
			while (line != null) {
				if (doFormat) {
					line = parseLine(line);
				}
				if (line != null)
					text.append(line).append("\n");
				line = in.readLine();
			}
			in.close();
			mOutput.setText(text.toString());
		} 
		catch (Exception e) {
			Log.e(TAG,e.getMessage(),e);
	    	mOutput.setText(e.getMessage());
		}
    }

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private DecimalFormat dfT = new DecimalFormat("###.#");
	private DecimalFormat dfV = new DecimalFormat("##.###");

	private String parseLine(String line) {
		if (line == null)
			return line;
		String[] split = line.split("[;]");
		if (split.length < 6)
			return line;
		if (split[0].equals("Nr"))
			return line;
		try {
			int count = Integer.parseInt(split[0]);
			long time = Long.parseLong(split[1]);
			int level = Integer.parseInt(split[2]);
			int scale = Integer.parseInt(split[3]);
			int percent = level*100/scale;
			int voltage = Integer.parseInt(split[4]);
			int temperature = Integer.parseInt(split[5]);
			double v = 0.001*voltage;
			double t = 0.1*temperature;
			String timestamp = sdf.format(new Date(time));
			StringBuffer result = new StringBuffer();
			result.append(Integer.toString(count)).append(". ")
					.append(timestamp).append(" ")
					.append(percent).append("% ")
					.append(dfV.format(v)).append("V ")
					.append(dfT.format(t)).append("° ")
					;
//			for (int i = 6; i < split.length; i++) {
//				result.append(" ").append(split[i]);
//			}
			return result.toString();
		}
		catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return line;
		}
	}
    
}