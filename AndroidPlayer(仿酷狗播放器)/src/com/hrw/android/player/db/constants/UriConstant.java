package com.hrw.android.player.db.constants;

public class UriConstant {
	public static final String AUTHORITY = "com.hrw.android.player.db.databaseprovider";
	public static final String AUDIO_LIST_PATH = "audiolist";
	public static final String PLAYLIST_PATH = "playlist";
	public static final String AUDIO_LIST_URI = "content://" + AUTHORITY + "/"
			+ AUDIO_LIST_PATH;
	public static final String PLAYLIST_URI = "content://" + AUTHORITY + "/"
			+ PLAYLIST_PATH;

}
