package com.android.andtriplog;


import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import android.app.ProgressDialog;
import android.database.Cursor;

public class GpxFileWriter extends FileWriter {
	protected SimpleDateFormat formatter;
	private boolean startT = false;
	
	public GpxFileWriter(String string, boolean start_trk) throws IOException {
		super(string);
		formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><gpx version=\"1.1\" creator=\"AndTripLog\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.topografix.com/GPX/1/1\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">");
		startT = start_trk;
		if (startT) {
			startTrk("default");
		}
	}
	
	
	public GpxFileWriter(String string) throws IOException {
		this(string,true);
	}

	public void startTrk(String name) throws IOException {
		write("<trk><name>"+ name +"</name> <trkseg>");
	}
	
	public void stopTrk() throws IOException {
		write("</trkseg></trk>");
	}
	
	public void write(float lat,float lon,float alt,String timestamp) throws IOException {
		write("<trkpt lat=\""+lat+"\" lon=\""+lon+"\"><ele>"+alt+"</ele><time>"+timestamp+"</time></trkpt>\n");
	}
	
	public void write(float lat,float lon,float alt,long timestamp) throws IOException {
		Date t = new Date(timestamp);
		write(lat,lon,alt,formatter.format(t));
	}

	public void fromCursorTimeStamp(Cursor l,ProgressDialog pDlg,Thread current) throws IOException,NoSuchElementException {
		int lat_pos = l.getColumnIndex("latitude");
		int lon_pos = l.getColumnIndex("longitude");
		int alt_pos = l.getColumnIndex("altitude");
		int stamp_pos = l.getColumnIndex("stamp");
		int cnt = l.getCount();
		for(int z=0;z<cnt;z++){
			if (!current.isInterrupted()) {
				pDlg.incrementProgressBy(1);
				l.moveToPosition(z);
				write(l.getFloat(lat_pos),l.getFloat(lon_pos),l.getFloat(alt_pos),l.getLong(stamp_pos));
			}
		}
	}
	
	public void fromCursorString(Cursor l,ProgressDialog pDlg,Thread current) throws IOException,NoSuchElementException {
		int lat_pos = l.getColumnIndex("latitude");
		int lon_pos = l.getColumnIndex("longitude");
		int alt_pos = l.getColumnIndex("altitude");
		int stamp_pos = l.getColumnIndex("stamp");
		int cnt = l.getCount();
		for(int z=0;z<cnt;z++){
			if (!current.isInterrupted()) {
				pDlg.incrementProgressBy(1);
				l.moveToPosition(z);
				write(l.getFloat(lat_pos),l.getFloat(lon_pos),l.getFloat(alt_pos),l.getString(stamp_pos));
			}
		}
	}
	
	public void close() throws IOException {
		if (startT) {
			stopTrk();
		}
		write("</gpx>");
		super.close();
	}
	
}