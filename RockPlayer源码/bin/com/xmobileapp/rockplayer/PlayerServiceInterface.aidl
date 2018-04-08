package com.xmobileapp.rockplayer;

interface PlayerServiceInterface {
	void	play(int albumCursorPosition, int songCursorPosition);
	boolean	isPlaying();
	boolean isPaused();
	void	resume();
	void	pause();
	void	playNext();
	void	stop();
	int		getPlayingPosition();
	int		getDuration();
	void	setShuffle(boolean shuffle);
	int		getAlbumCursorPosition();
	int		getSongCursorPosition();
	boolean	setAlbumCursorPosition(int position);
	boolean	setSongCursorPosition(int position);
	void	forward();
	void	reverse();
	void	seekTo(int msec);
	void	setPlaylist(long playlist);
	void	setRecentPeriod(int period);
	void	resetAlbumCursor();
	void	reloadCursors();
	void	destroy();
	void 	setScrobbleDroid(boolean val);

//	void	setSongCursor(Cursor songCursor);
//	void	setAlbumCursor(Cursor albumCursor);
//	Cursor	getSongCursor();
//	Cursor	getAlbumCursor();
}

