package com.ch_linghu.fanfoudroid.data2;

import java.util.Date;

public class Status {

	private Date created_at;
	private String id;
	private String text;
	private String source;
	private boolean truncated;
	private String in_reply_to_status_id;
	private String in_reply_to_user_id;
	private String repost_status_id;
	private String repost_user_id;
	private boolean favorited;
	private String in_reply_to_screen_name;
	private Photo photo_url;
	private User user;

	private boolean isUnRead = false;
	private int type = -1;
	private String owner_id;

	public Status() {
	}

	public Date getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(Date created_at) {
		this.created_at = created_at;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public String getInReplyToStatusId() {
		return in_reply_to_status_id;
	}

	public void setInReplyToStatusId(String in_reply_to_status_id) {
		this.in_reply_to_status_id = in_reply_to_status_id;
	}

	public String getInReplyToUserId() {
		return in_reply_to_user_id;
	}

	public void setInReplyToUserId(String in_reply_to_user_id) {
		this.in_reply_to_user_id = in_reply_to_user_id;
	}

	public String getRepostStatusId() {
		return repost_status_id;
	}

	public void setRepostStatusId(String repost_status_id) {
		this.repost_status_id = repost_status_id;
	}

	public String getRepostUserId() {
		return repost_user_id;
	}

	public void setRepostUserId(String repost_user_id) {
		this.repost_user_id = repost_user_id;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public String getInReplyToScreenName() {
		return in_reply_to_screen_name;
	}

	public void setInReplyToScreenName(String in_reply_to_screen_name) {
		this.in_reply_to_screen_name = in_reply_to_screen_name;
	}

	public Photo getPhotoUrl() {
		return photo_url;
	}

	public void setPhotoUrl(Photo photo_url) {
		this.photo_url = photo_url;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isUnRead() {
		return isUnRead;
	}

	public void setUnRead(boolean isUnRead) {
		this.isUnRead = isUnRead;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOwnerId() {
		return owner_id;
	}

	public void setOwnerId(String owner_id) {
		this.owner_id = owner_id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Status other = (Status) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (owner_id == null) {
			if (other.owner_id != null)
				return false;
		} else if (!owner_id.equals(other.owner_id))
			return false;
		if (type != other.type)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Status [created_at=" + created_at + ", id=" + id + ", text="
				+ text + ", source=" + source + ", truncated=" + truncated
				+ ", in_reply_to_status_id=" + in_reply_to_status_id
				+ ", in_reply_to_user_id=" + in_reply_to_user_id
				+ ", favorited=" + favorited + ", in_reply_to_screen_name="
				+ in_reply_to_screen_name + ", photo_url=" + photo_url
				+ ", user=" + user + "]";
	}
}
