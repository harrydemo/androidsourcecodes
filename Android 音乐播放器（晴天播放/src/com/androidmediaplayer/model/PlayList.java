package com.androidmediaplayer.model;


public class PlayList {

	private Integer playList_id;
	private String playList_name;
	
	public PlayList() {
		super();
	}

	public PlayList(String playList_name) {
		this.playList_name = playList_name;
	}

	public PlayList(Integer playList_id, String playList_name) {
		this.playList_id = playList_id;
		this.playList_name = playList_name;
	}

	public Integer getPlayList_id() {
		return playList_id;
	}

	public void setPlayList_id(Integer playList_id) {
		this.playList_id = playList_id;
	}

	public String getPlayList_name() {
		return playList_name;
	}

	public void setPlayList_name(String playList_name) {
		this.playList_name = playList_name;
	}

}
