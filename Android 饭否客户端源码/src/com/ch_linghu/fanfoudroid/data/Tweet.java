/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ch_linghu.fanfoudroid.data;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.ch_linghu.fanfoudroid.R;
import com.ch_linghu.fanfoudroid.TwitterApplication;
import com.ch_linghu.fanfoudroid.fanfou.Status;
import com.ch_linghu.fanfoudroid.util.DateTimeHelper;
import com.ch_linghu.fanfoudroid.util.TextHelper;

public class Tweet extends Message implements Parcelable {
	private static final String TAG = "Tweet";

	public com.ch_linghu.fanfoudroid.fanfou.User user;
	public String source;
	public String prevId;
	private int statusType = -1; // @see StatusTable#TYPE_*

	public void setStatusType(int type) {
		statusType = type;
	}

	public int getStatusType() {
		return statusType;
	}

	public Tweet() {
	}

	public static Tweet create(Status status) {
		Tweet tweet = new Tweet();

		tweet.id = status.getId();
		// 转义符放到getSimpleTweetText里去处理，这里不做处理
		tweet.text = status.getText();
		tweet.createdAt = status.getCreatedAt();
		tweet.favorited = status.isFavorited() ? "true" : "false";
		tweet.truncated = status.isTruncated() ? "true" : "false";
		tweet.inReplyToStatusId = status.getInReplyToStatusId();
		tweet.inReplyToUserId = status.getInReplyToUserId();
		tweet.inReplyToScreenName = status.getInReplyToScreenName();
		tweet.repostStatusId = status.getRepostStatusId();
		tweet.repostUserId = status.getRepostUserId();

		tweet.screenName = TextHelper.getSimpleTweetText(status.getUser()
				.getScreenName());
		tweet.profileImageUrl = status.getUser().getProfileImageURL()
				.toString();
		tweet.userId = status.getUser().getId();
		tweet.user = status.getUser();

		tweet.thumbnail_pic = status.getThumbnail_pic();
		tweet.bmiddle_pic = status.getBmiddle_pic();
		tweet.original_pic = status.getOriginal_pic();

		tweet.source = TextHelper.getSimpleTweetText(status.getSource());

		return tweet;
	}

	public static Tweet createFromSearchApi(JSONObject jsonObject)
			throws JSONException {
		Tweet tweet = new Tweet();

		tweet.id = jsonObject.getString("id") + "";
		// 转义符放到getSimpleTweetText里去处理，这里不做处理
		tweet.text = jsonObject.getString("text");
		tweet.createdAt = DateTimeHelper.parseSearchApiDateTime(jsonObject
				.getString("created_at"));
		tweet.favorited = jsonObject.getString("favorited");
		tweet.truncated = jsonObject.getString("truncated");
		tweet.inReplyToStatusId = jsonObject.getString("in_reply_to_status_id");
		tweet.inReplyToUserId = jsonObject.getString("in_reply_to_user_id");
		tweet.inReplyToScreenName = jsonObject
				.getString("in_reply_to_screen_name");

		tweet.screenName = TextHelper.getSimpleTweetText(jsonObject
				.getString("from_user"));
		tweet.profileImageUrl = jsonObject.getString("profile_image_url");
		tweet.userId = jsonObject.getString("from_user_id");
		tweet.source = TextHelper.getSimpleTweetText(jsonObject
				.getString("source"));

		return tweet;
	}

	public static String buildMetaText(StringBuilder builder, Date createdAt,
			String source, String replyTo, String repostUserId) {
		builder.setLength(0);

		builder.append(DateTimeHelper.getRelativeDate(createdAt));
		builder.append(" ");

		builder.append(TwitterApplication.mContext
				.getString(R.string.tweet_source_prefix));
		builder.append(source);

		if (!TextUtils.isEmpty(replyTo)) {
			builder.append(" "
					+ TwitterApplication.mContext
							.getString(R.string.tweet_reply_to_prefix));
			builder.append(replyTo);
			builder.append(TwitterApplication.mContext
					.getString(R.string.tweet_reply_to_suffix));
		}
		
		if (!TextUtils.isEmpty(repostUserId)) {
			builder.append(" "
					+ TwitterApplication.mContext
							.getString(R.string.tweet_repost_prefix));
			builder.append(repostUserId);
			builder.append(TwitterApplication.mContext
					.getString(R.string.tweet_repost_suffix));
		}
		
		return builder.toString();
	}

	// For interface Parcelable

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(text);
		out.writeValue(createdAt); // Date
		out.writeString(screenName);
		out.writeString(favorited);
		out.writeString(inReplyToStatusId);
		out.writeString(inReplyToUserId);
		out.writeString(inReplyToScreenName);
		out.writeString(repostStatusId);
		out.writeString(repostUserId);
		out.writeString(screenName);
		out.writeString(profileImageUrl);
		out.writeString(thumbnail_pic);
		out.writeString(bmiddle_pic);
		out.writeString(original_pic);
		out.writeString(userId);
		out.writeString(source);
	}

	public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
		public Tweet createFromParcel(Parcel in) {
			return new Tweet(in);
		}

		public Tweet[] newArray(int size) {
			// return new Tweet[size];
			throw new UnsupportedOperationException();
		}
	};

	public Tweet(Parcel in) {
		id = in.readString();
		text = in.readString();
		createdAt = (Date) in.readValue(Date.class.getClassLoader());
		screenName = in.readString();
		favorited = in.readString();
		inReplyToStatusId = in.readString();
		inReplyToUserId = in.readString();
		inReplyToScreenName = in.readString();
		repostStatusId = in.readString();
		repostUserId = in.readString();
		screenName = in.readString();
		profileImageUrl = in.readString();
		thumbnail_pic = in.readString();
		bmiddle_pic = in.readString();
		original_pic = in.readString();
		userId = in.readString();
		source = in.readString();
	}

	@Override
	public String toString() {
		return "Tweet [source=" + source + ", id=" + id + ", screenName="
				+ screenName + ", text=" + text + ", profileImageUrl="
				+ profileImageUrl + ", createdAt=" + createdAt + ", userId="
				+ userId + ", favorited=" + favorited + ", inReplyToStatusId="
				+ inReplyToStatusId + ", inReplyToUserId=" + inReplyToUserId
				+ ", inReplyToScreenName=" + inReplyToScreenName + "]";
	}

}
