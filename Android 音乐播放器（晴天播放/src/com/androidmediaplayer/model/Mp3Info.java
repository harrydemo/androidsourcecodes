package com.androidmediaplayer.model;

import java.io.Serializable;

public class Mp3Info implements Serializable{

	private static final long serialVersionUID = 1L;
	private String audio_id = "";
	private String mp3Name = "";
	private String mp3Size = "";
	private String lrcName = "";
	private String duration = "";
	private String path = "";
	private String artist = "";
	private Integer playList_id = null;
	private String timeStr = "";

	public Mp3Info() {
	}

	public Mp3Info(String filename_id, String mp3Name, String mp3Size,
			String lrcName, String duration, String path, String artist,
			Integer playList_id, String timeStr) {
		this.audio_id = filename_id;
		this.mp3Name = mp3Name;
		this.mp3Size = mp3Size;
		this.lrcName = lrcName;
		this.duration = duration;
		this.path = path;
		this.artist = artist;
		this.playList_id = playList_id;
		this.timeStr = timeStr;
	}
	
	public String getAudio_id() {
		return audio_id;
	}

	public void setAudio_id(String audio_id) {
		this.audio_id = audio_id;
	}

	public String getMp3Name() {
		return mp3Name;
	}

	public void setMp3Name(String mp3Name) {
		this.mp3Name = mp3Name;
	}

	public String getMp3Size() {
		return mp3Size;
	}

	public void setMp3Size(String mp3Size) {
		this.mp3Size = mp3Size;
	}

	public String getLrcName() {
		return lrcName;
	}

	public void setLrcName(String lrcName) {
		this.lrcName = lrcName;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public Integer getPlayList_id() {
		return playList_id;
	}

	public void setPlayList_id(Integer playList_id) {
		this.playList_id = playList_id;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	@Override
	public String toString() {
		return "Mp3Info [audio_id=" + audio_id + ", mp3Name=" + mp3Name
				+ ", mp3Size=" + mp3Size + ", lrcName=" + lrcName
				+ ", duration=" + duration + ", path=" + path + ", artist="
				+ artist + ", playList_id=" + playList_id + ", timeStr="
				+ timeStr + "]";
	}

}
