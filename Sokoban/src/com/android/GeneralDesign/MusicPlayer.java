package com.android.GeneralDesign;

import java.io.IOException;

import com.android.Sokoban.R;
import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlayer
{
	public static class PlayerType
	{
		public static final int BG_MUSIC_PLAYER = 			0;
		public static final int SYS_MUSIC_PLAYER = 			1;
		public static final int KEY_BOARD_MUSIC_PLAYER = 	2;
		public static final int PLAYER_NUM = 				3;
	}
	public static class MusicSettingState
	{
		public static final int ON = 0;
		public static final int OFF = 1;
	}
	public static class MusicID
	{
		public static final int NULL = 				-1;
		public static final int SN_BG_MUSIC_1 = 		0;
		public static final int SN_BG_MUSIC_2 = 		1;
		public static final int SN_BG_MUSIC_3 = 		2;
		public static final int SN_BG_MUSIC_4 = 		3;
		public static final int SN_KB_1 = 				4;
		public static final int SN_KB_2 = 				5;
		public static final int SN_KB_3 = 				6;
		public static final int SN_KB_4 = 				7;
		public static final int SN_ERROR = 			8;
		public static final int SN_SAVE = 			9;
		public static final int SN_STATE_SWITCH = 	10;
		public static final int SN_TOUTCH =		 	11;
	}
	private static final int[] resIds =
	{
		R.raw.sn_bg_music_1,
		R.raw.sn_bg_music_2,
		R.raw.sn_bg_music_3,
		R.raw.sn_bg_music_4,
		R.raw.sn_button_1,
		R.raw.sn_button_2,
		R.raw.sn_button_3,
		R.raw.sn_button_4,
		R.raw.sn_error,
		R.raw.sn_save,
		R.raw.sn_state_switch,
		R.raw.sn_touch
	};
	/*
	private static String[] playerName =
	{
		"BG_MUSIC_PLAYER",
		"SYS_MUSIC_PLAYER",
		"KEY_BOARD_MUSIC_PLAYER"
	};
	private static String[] musicStateName =
	{
		"ON",
		"OFF",
	};
	private static String[] musicName =
	{
		"NULL",
		"BG_MUSIC_1",
		"BG_MUSIC_2",
		"BG_MUSIC_3",
		"BG_MUSIC_4",
		"KB_1",
		"KB_2",
		"KB_3",
		"KB_4",
		"SN_ERROR",
		"SN_SAVE",
		"SN_SWITCH",
		"SN_TOUTCH",
	};
	*/
	
	private static MediaPlayer[] musicPlayers;
	private static int[] playList;
	private static Context	mContext = null;
	private static MusicPlayer instance;
	public static void create(Context context)
	{
		if(null == instance)
		{
			instance = new MusicPlayer(context);
		}
	}
	private MusicPlayer(Context context)
	{
		mContext = context;
		musicPlayers = new MediaPlayer[PlayerType.PLAYER_NUM];
		playList = new int[PlayerType.PLAYER_NUM];
		for(int i = 0; i < playList.length; i++)
		{
			playList[i] = MusicID.NULL;
		}
	}
	private static int getResId(int musicId)
	{
		musicId = musicId % resIds.length;
		return resIds[musicId];
	}
	public static void playMusic(int player, int musicId)
	{
		playMusic(player, musicId, false);
	}
	public static void playMusic(int playerId, int musicId, boolean isLooping)
	{
		if(MusicID.NULL == musicId)
			return;
		playerId = playerId % musicPlayers.length;
		
		int i;
		for(i = 0; i < playList.length; i++)
		{
			if(musicId == playList[i] && i != playerId)
			{
				//Log.i(clienDB.LOG_TAG, "player music resurce conflict! free music now ...");
				freeMusic(i);
			}
		}
		
		//Log.i(clienDB.LOG_TAG, "free music before playing ...");
		freeMusic(playerId);
		
		/*
		Log.i(clienDB.LOG_TAG, "Play music: "
				+ "player(" + playerName[playerId]
				+ "music(" + musicName[musicId + 1]
				+ "Loop(" +  isLooping + ")");
		*/	
		
		musicPlayers[playerId] = MediaPlayer.create(mContext, getResId(musicId));
		musicPlayers[playerId].setLooping(isLooping);
		
		try
		{
			musicPlayers[playerId].prepare();
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		musicPlayers[playerId].start();
		playList[playerId] = musicId;
	}
	public static void freeMusic()
	{
		int playerId;
		for(playerId = 0; playerId < PlayerType.PLAYER_NUM; playerId++)
		{
			freeMusic(playerId);
		}
	}
	public static void freeMusic(int playerId)
	{
		/*
		Log.i(clienDB.LOG_TAG, "== freeMusic: "
				+ "player(" + playerName[playerId] + ")" + " ==");
		*/
		playerId = playerId % musicPlayers.length;
		if(null != musicPlayers[playerId])
		{
			musicPlayers[playerId].stop();
			musicPlayers[playerId].release();
			musicPlayers[playerId] = null;
			playList[playerId] = MusicID.NULL;
		}
	}
	public static void stopMusic(int playerId)
	{
		/*
		Log.i(clienDB.LOG_TAG, "== stopMusic: "
				+ "player(" + playerName[playerId] + ")" + " ==");
		*/
		playerId = playerId % musicPlayers.length;
		if(null != musicPlayers[playerId])
		{
			musicPlayers[playerId].stop();
		}
	}
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		freeMusic();
		musicPlayers = null;
		playList = null;
	}
}
