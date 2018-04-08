package com.hrw.android.player.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class MediaScannerClient implements MediaScannerConnectionClient {
	private Context context;
	private MediaScannerConnection conection;
	private String path;
	private String mimeType;

	public MediaScannerClient(Context c, String p, String mt) {
		this.context = c;
		this.path = p;
		this.mimeType = mt;
		conection = new MediaScannerConnection(context, this);
		conection.connect();
	}

	@Override
	public void onMediaScannerConnected() {
		conection.scanFile(path, mimeType);
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		conection.disconnect();
		context = null;

	}

}
