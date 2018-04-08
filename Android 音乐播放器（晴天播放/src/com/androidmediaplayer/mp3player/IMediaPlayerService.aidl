package com.androidmediaplayer.mp3player;

interface IMediaPlayerService {

	void start();
	void pause();
	void stop();
	void release();
	boolean isPlaying();
	boolean isPause();
	boolean isStop();
	int getCurrentPosition();
	int getDuration();
	void seekTo(int progress);
	void next();
	void previous();
	void setFileInfo(in List fileInfo);
	void setPlayMode(int PlayMode);
	
}
