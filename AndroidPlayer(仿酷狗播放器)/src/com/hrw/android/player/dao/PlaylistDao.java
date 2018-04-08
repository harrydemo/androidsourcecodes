package com.hrw.android.player.dao;

import java.util.List;

import com.hrw.android.player.domain.Playlist;

public interface PlaylistDao {
	void createPlaylist(String name);

	void removePlaylist(String id);

	List<Playlist> getAllPlaylist();

}
