package com.yarin.android.MusicPlayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MusicInfoController
{
	private static MusicInfoController	mInstance	= null;

	private MusicPlayerApp					pApp		= null;

	public static MusicInfoController getInstance(MusicPlayerApp app)
	{
		if (mInstance == null)
		{
			mInstance = new MusicInfoController(app);
		}
		return mInstance;
	}


	private MusicInfoController(MusicPlayerApp app)
	{
		pApp = app;
	}

	public MusicPlayerApp getMusicPlayer()
	{
		return pApp;
	}

	private Cursor query(Uri uri, String[] prjs, String selections, String[] selectArgs, String order){
		ContentResolver resolver = pApp.getContentResolver();
		if (resolver == null){
			return null;
		}
		return resolver.query(uri, prjs, selections, selectArgs, order);
	}
	public Cursor getAllSongs(){
		return query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
	}
}

