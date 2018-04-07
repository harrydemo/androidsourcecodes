package com.hrw.android.player.domain;

import java.util.Date;

import com.hrw.android.player.db.DataBaseHelper;
import com.hrw.android.player.orm.annotation.Column;
import com.hrw.android.player.orm.annotation.Table;

@Table(name = DataBaseHelper.AUDIO_LIST_TABLE_NAME)
public class Audio extends BaseDomain {
	private Long id;
	@Column(name = "audio_name")
	private String name;
	@Column(name = "audio_path")
	private String path;
	@Column(name = "add_date")
	private Date addDate;
	@Column(name = "modified_date")
	private Date updateDate;
	@Column(name = "playlist_id")
	private String playlistId;

	public String getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(String playlistId) {
		this.playlistId = playlistId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
