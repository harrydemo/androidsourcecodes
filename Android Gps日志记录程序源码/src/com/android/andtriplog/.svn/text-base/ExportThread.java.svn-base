package com.android.andtriplog;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

class ExportThread extends Thread {

	private ProgressDialog mProgress;
	private AndTripLog aThis;
	private long tripId = -1;
	private boolean email = false;

	public ExportThread(AndTripLog a,long t,ProgressDialog m,boolean b) {
		super();
		mProgress = m;
		tripId = t;
		aThis = a;
		email = b;
	}
	
	public void run() {
        try {
        	String fname;
        	Cursor ltrip;
        	int cntProgress;
        	if (tripId == -1) {
        		fname = "/sdcard/all-andtriplog.gpx";
            	ltrip = aThis.mAndTripLogDB.getAllTripId();
            	cntProgress = aThis.mAndTripLogDB.getAllTripTrkCount();
        	} else {
        		fname = "/sdcard/" + tripId + "-andtriplog.gpx";
            	ltrip = aThis.mAndTripLogDB.getTripId(tripId);
            	cntProgress = aThis.mAndTripLogDB.getTripTrkCount(tripId);
        	}
        	GpxFileWriter fw = new GpxFileWriter(fname,false);
            int cnt = ltrip.getCount();
            mProgress.setProgress(0);
            mProgress.setMax(cntProgress);
            for(int i=0;i<cnt;i++) {
            	if (!isInterrupted()) {
            		ltrip.moveToPosition(i);
            		long id = ltrip.getLong(0);
            		fw.startTrk("Trip "+id);
            		Cursor l = aThis.mAndTripLogDB.getTripData(id);
            		fw.fromCursorString(l,mProgress,this);
            		fw.stopTrk();
            	} else {
            	 Log.w("AndTripLog","Interruped");
            	}
        	}
			fw.close();
			if (email && !isInterrupted()) {
				Intent i= new Intent(android.content.Intent.ACTION_SEND);
				i.setType("image/jpeg");
				i.putExtra(android.content.Intent.EXTRA_SUBJECT,"The gpx file");
				i.putExtra(android.content.Intent.EXTRA_STREAM,Uri.parse("file://"+fname));
				aThis.startActivity(Intent.createChooser(i,"Send gps data..."));
			}
        } catch (IOException e) {
			e.printStackTrace();
            Log.w("AndTripLog","Unable to save");
		}
        mProgress.dismiss();
        mProgress.setProgress(0);
	}
}