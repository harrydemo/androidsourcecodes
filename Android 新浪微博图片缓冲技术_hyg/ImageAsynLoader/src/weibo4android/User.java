/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package weibo4android;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import weibo4android.http.Response;
import weibo4android.org.json.JSONArray;
import weibo4android.org.json.JSONException;
import weibo4android.org.json.JSONObject;

/**
 * A data class representing Basic user information element
 */
public class User extends WeiboResponse implements java.io.Serializable {

    static final String[] POSSIBLE_ROOT_NAMES = new String[]{"user", "sender", "recipient", "retweeting_user"};
    private Weibo weibo;
    private long id;
    private String name;
    private String screenName;
    private String location;
    private String description;
    private String profileImageUrl;
    private String url;
    private boolean isProtected;
    private int followersCount;

    private Date statusCreatedAt;
    private long statusId = -1;
    private String statusText = null;
    private String statusSource = null;
    private boolean statusTruncated = false;
    private long statusInReplyToStatusId = -1;
    private long statusInReplyToUserId = -1;
    private boolean statusFavorited = false;
    private String statusInReplyToScreenName = null;

    private String profileBackgroundColor;
    private String profileTextColor;
    private String profileLinkColor;
    private String profileSidebarFillColor;
    private String profileSidebarBorderColor;
    private int friendsCount;
    private Date createdAt;
    private int favouritesCount;
    private int utcOffset;
    private String timeZone;
    private String profileBackgroundImageUrl;
    private String profileBackgroundTile;
    private boolean following;
    private boolean notificationEnabled;
    private int statusesCount;
    private boolean geoEnabled;
    private boolean verified;
    private String userDomain;
    private String gender;
    private static final long serialVersionUID = -6345893237975349030L;


    /*package*/User(Response res, Weibo weibo) throws WeiboException {
        super(res);
        Element elem = res.asDocument().getDocumentElement();
        init(elem, weibo);
    }

    /*package*/User(Response res, Element elem, Weibo weibo) throws WeiboException {
        super(res);
        init(elem, weibo);
    }
    /*package*/User(JSONObject json) throws WeiboException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws WeiboException {
    	if(json!=null){
        try {
            id = json.getLong("id");
            name = json.getString("name");
            screenName = json.getString("screen_name");
            location = json.getString("location");
            description = json.getString("description");
            profileImageUrl = json.getString("profile_image_url");
            url = json.getString("url");
            isProtected = json.getBoolean("protected");
            followersCount = json.getInt("followers_count");

            profileBackgroundColor = json.getString("profile_background_color");
            profileTextColor = json.getString("profile_text_color");
            profileLinkColor = json.getString("profile_link_color");
            profileSidebarFillColor = json.getString("profile_sidebar_fill_color");
            profileSidebarBorderColor = json.getString("profile_sidebar_border_color");
            friendsCount = json.getInt("friends_count");
            createdAt = parseDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
            favouritesCount = json.getInt("favourites_count");
            utcOffset = getInt("utc_offset", json);
            timeZone = json.getString("time_zone");
            profileBackgroundImageUrl = json.getString("profile_background_image_url");
            profileBackgroundTile = json.getString("profile_background_tile");
            following = getBoolean("following", json);
            notificationEnabled = getBoolean("notifications", json);
            verified=getBoolean("verified", json);
            statusesCount = json.getInt("statuses_count");
            userDomain=json.getString("domain");
            gender = json.getString("gender");
            if (!json.isNull("status")) {
                JSONObject status = json.getJSONObject("status");
                statusCreatedAt = parseDate(status.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
                statusId = status.getLong("id");
                statusText = status.getString("text");
                statusSource = status.getString("source");
                statusTruncated = status.getBoolean("truncated");
                statusInReplyToStatusId = status.getLong("in_reply_to_status_id");
                statusInReplyToUserId = status.getLong("in_reply_to_user_id");
                statusFavorited = status.getBoolean("favorited");
                statusInReplyToScreenName = status.getString("in_reply_to_screen_name");
            }
        } catch (JSONException jsone) {
            throw new WeiboException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    	}
    }

    private void init(Element elem, Weibo weibo) throws WeiboException {
        this.weibo = weibo;
        ensureRootNodeNameIs(POSSIBLE_ROOT_NAMES, elem);
        id = getChildLong("id", elem);
        name = getChildText("name", elem);
        screenName = getChildText("screen_name", elem);
        location = getChildText("location", elem);
        description = getChildText("description", elem);
        profileImageUrl = getChildText("profile_image_url", elem);
        url = getChildText("url", elem);
        isProtected = getChildBoolean("protected", elem);
        followersCount = getChildInt("followers_count", elem);

        profileBackgroundColor = getChildText("profile_background_color", elem);
        profileTextColor = getChildText("profile_text_color", elem);
        profileLinkColor = getChildText("profile_link_color", elem);
        profileSidebarFillColor = getChildText("profile_sidebar_fill_color", elem);
        profileSidebarBorderColor = getChildText("profile_sidebar_border_color", elem);
        friendsCount = getChildInt("friends_count", elem);
        createdAt = getChildDate("created_at", elem);
        favouritesCount = getChildInt("favourites_count", elem);
        utcOffset = getChildInt("utc_offset", elem);
        timeZone = getChildText("time_zone", elem);
        profileBackgroundImageUrl = getChildText("profile_background_image_url", elem);
        profileBackgroundTile = getChildText("profile_background_tile", elem);
        following = getChildBoolean("following", elem);
        notificationEnabled = getChildBoolean("notifications", elem);
        statusesCount = getChildInt("statuses_count", elem);
        geoEnabled = getChildBoolean("geo_enabled", elem);
        verified = getChildBoolean("verified", elem);
        userDomain = getChildText("domain", elem);
        gender = getChildText("gender", elem);

        NodeList statuses = elem.getElementsByTagName("status");
        if (statuses.getLength() != 0) {
            Element status = (Element) statuses.item(0);
            statusCreatedAt = getChildDate("created_at", status);
            statusId = getChildLong("id", status);
            statusText = getChildText("text", status);
            statusSource = getChildText("source", status);
            statusTruncated = getChildBoolean("truncated", status);
            statusInReplyToStatusId = getChildLong("in_reply_to_status_id", status);
            statusInReplyToUserId = getChildLong("in_reply_to_user_id", status);
            statusFavorited = getChildBoolean("favorited", status);
            statusInReplyToScreenName = getChildText("in_reply_to_screen_name", status);
        }
    }

    /**
     * Returns the id of the user
     *
     * @return the id of the user
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the name of the user
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the screen name of the user
     *
     * @return the screen name of the user
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * Returns the location of the user
     *
     * @return the location of the user
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the description of the user
     *
     * @return the description of the user
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the profile image url of the user
     *
     * @return the profile image url of the user
     */
    public URL getProfileImageURL() {
        try {
            return new URL(profileImageUrl);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    /**
     * Returns the url of the user
     *
     * @return the url of the user
     */
    public URL getURL() {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    /**
     * Test if the user status is protected
     *
     * @return true if the user status is protected
     */
    public boolean isProtected() {
        return isProtected;
    }


    public String getUserDomain() {
		return userDomain;
	}

	/**
     * Returns the number of followers
     *
     * @return the number of followers
     * @since Weibo4J 1.2.0
     */
    public int getFollowersCount() {
        return followersCount;
    }

    public static List<User> constructUser(Response res)throws WeiboException{

    	JSONObject json=res.asJSONObject();
    	try {
    		//			int next_cursor = json.getInt("next_cursor");
    		//			int previous_cursor = json.getInt("previous_cursor");



    		JSONArray list = json.getJSONArray("users");
    		int size=list.length();
    		List<User> users=new ArrayList<User>(size);
    		for(int i=0;i<size;i++){
    			users.add(new User(list.getJSONObject(i)));
    		}
    		return users;


    	}
		catch (JSONException je) {
			throw new WeiboException(je);
		}
        
    }

    public static List<User> constructUsers(Response res, Weibo weibo) throws WeiboException {
        Document doc = res.asDocument();
        if (isRootNodeNilClasses(doc)) {
            return new ArrayList<User>(0);
        } else {
            try {
                ensureRootNodeNameIs("users", doc);
//                NodeList list = doc.getDocumentElement().getElementsByTagName(
//                        "user");
//                int size = list.getLength();
//                List<User> users = new ArrayList<User>(size);
//                for (int i = 0; i < size; i++) {
//                    users.add(new User(res, (Element) list.item(i), weibo));
//                }
                
                //去除掉嵌套的bug
                NodeList list=doc.getDocumentElement().getChildNodes();
                List<User> users = new ArrayList<User>(list.getLength());
                Node node;
                for(int i=0;i<list.getLength();i++){
                	node=list.item(i);
                	if(node.getNodeName().equals("user")){
                		users.add(new User(res, (Element) node, weibo));
                	}
                }

                return users;
            } catch (WeiboException te) {
                if (isRootNodeNilClasses(doc)) {
                    return new ArrayList<User>(0);
                } else {
                    throw te;
                }
            }
        }
    }

    public static UserWapper constructWapperUsers(Response res, Weibo weibo) throws WeiboException {
		Document doc = res.asDocument();
		if (isRootNodeNilClasses(doc)) {
			return new UserWapper(new ArrayList<User>(0), 0, 0);
		} else {
			try {
				ensureRootNodeNameIs("users_list", doc);
				Element root = doc.getDocumentElement();
				NodeList user = root.getElementsByTagName("users");
				int length = user.getLength();
				if (length == 0) {
					return new UserWapper(new ArrayList<User>(0), 0, 0);
				}
				// 
				Element listsRoot = (Element) user.item(0);
                NodeList list=listsRoot.getChildNodes();
                List<User> users = new ArrayList<User>(list.getLength());
                Node node;
                for(int i=0;i<list.getLength();i++){
                	node=list.item(i);
                	if(node.getNodeName().equals("user")){
                		users.add(new User(res, (Element) node, weibo));
                	}
                }
				//
				long previousCursor = getChildLong("previous_curosr", root);
				long nextCursor = getChildLong("next_curosr", root);
				if (nextCursor == -1) { // 兼容不同标签名称
					nextCursor = getChildLong("nextCurosr", root);
				}
				return new UserWapper(users, previousCursor, nextCursor);
			} catch (WeiboException te) {
				if (isRootNodeNilClasses(doc)) {
					return new UserWapper(new ArrayList<User>(0), 0, 0);
				} else {
					throw te;
				}
			}
		}
	}

    public static List<User> constructUsers(Response res) throws WeiboException {
    	try {
            JSONArray list = res.asJSONArray();
            int size = list.length();
            List<User> users = new ArrayList<User>(size);
            for (int i = 0; i < size; i++) {
                users.add(new User(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException jsone) {
            throw new WeiboException(jsone);
        } catch (WeiboException te) {
            throw te;
        }  
    }
    
    /**
     * 
     * @param res
     * @return
     * @throws WeiboException
     */
    public static UserWapper constructWapperUsers(Response res) throws WeiboException {
		JSONObject jsonUsers = res.asJSONObject(); //asJSONArray();
		try {
			JSONArray user = jsonUsers.getJSONArray("users");
			int size = user.length();
			List<User> users = new ArrayList<User>(size);
			for (int i = 0; i < size; i++) {
				users.add(new User(user.getJSONObject(i)));
			}
			long previousCursor = jsonUsers.getLong("previous_curosr");
			long nextCursor = jsonUsers.getLong("next_cursor");
			if (nextCursor == -1) { // 兼容不同标签名称
				nextCursor = jsonUsers.getLong("nextCursor");
			}
			return new UserWapper(users, previousCursor, nextCursor);
		} catch (JSONException jsone) {
			throw new WeiboException(jsone);
		}
    }
    
    /**
     * @param res 
     * @return 
     * @throws WeiboException
     */
    static List<User> constructResult(Response res) throws WeiboException {
    	JSONArray list = res.asJSONArray();
    		try {
	             int size = list.length();
	             List<User> users = new ArrayList<User>(size);
	             for (int i = 0; i < size; i++) {
	                 users.add(new User(list.getJSONObject(i)));
	             }
	             return users;
			} catch (JSONException e) {
			}
      return null;
    }
    
    /**
     * @return created_at or null if the user is protected
     * @since Weibo4J 1.2.0
     */
    public Date getStatusCreatedAt() {
        return statusCreatedAt;
    }

    /**
     *
     * @return status id or -1 if the user is protected
     */
    public long getStatusId() {
        return statusId;
    }

    /**
     *
     * @return status text or null if the user is protected
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     *
     * @return source or null if the user is protected
     * @since Weibo4J 1.2.0
     */
    public String getStatusSource() {
        return statusSource;
    }

    /**
     *
     * @return truncated or false if the user is protected
     * @since Weibo4J 1.2.0
     */
    public boolean isStatusTruncated() {
        return statusTruncated;
    }

    /**
     *
     * @return in_reply_to_status_id or -1 if the user is protected
     * @since Weibo4J 1.2.0
     */
    public long getStatusInReplyToStatusId() {
        return statusInReplyToStatusId;
    }

    /**
     *
     * @return in_reply_to_user_id or -1 if the user is protected
     * @since Weibo4J 1.2.0
     */
    public long getStatusInReplyToUserId() {
        return statusInReplyToUserId;
    }

    /**
     *
     * @return favorited or false if the user is protected
     * @since Weibo4J 1.2.0
     */
    public boolean isStatusFavorited() {
        return statusFavorited;
    }

    /**
     *
     * @return in_reply_to_screen_name or null if the user is protected
     * @since Weibo4J 1.2.0
     */

    public String getStatusInReplyToScreenName() {
        return -1 != statusInReplyToUserId ? statusInReplyToScreenName : null;
    }

        public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public String getProfileTextColor() {
        return profileTextColor;
    }

    public String getProfileLinkColor() {
        return profileLinkColor;
    }

    public String getProfileSidebarFillColor() {
        return profileSidebarFillColor;
    }

    public String getProfileSidebarBorderColor() {
        return profileSidebarBorderColor;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public int getUtcOffset() {
        return utcOffset;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public String getProfileBackgroundTile() {
        return profileBackgroundTile;
    }

    /**
     *
     * @deprecated
     */
    public boolean isFollowing() {
        return following;
    }

    /**
     * @deprecated use isNotificationsEnabled() instead
     */

    public boolean isNotifications() {
        return notificationEnabled;
    }

    /**
     *
     * @since Weibo4J 1.2.0
     * @deprecated
     */
    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    /**
     * @return the user is enabling geo location
     * @since Weibo4J 1.2.0
     */
    public boolean isGeoEnabled() {
        return geoEnabled;
    }

    /**
     * @return returns true if the user is a verified celebrity
     * @since Weibo4J 1.2.0
     */
    public boolean isVerified() {
        return verified;
    }

    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
    public String toString() {
        return "User{" +
                "weibo=" + weibo +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", domain ='" + userDomain+ '\'' +
                ", gender ='" + gender + '\'' +
                ", url='" + url + '\'' +
                ", isProtected=" + isProtected +
                ", followersCount=" + followersCount +
                ", statusCreatedAt=" + statusCreatedAt +
                ", statusId=" + statusId +
                ", statusText='" + statusText + '\'' +
                ", statusSource='" + statusSource + '\'' +
                ", statusTruncated=" + statusTruncated +
                ", statusInReplyToStatusId=" + statusInReplyToStatusId +
                ", statusInReplyToUserId=" + statusInReplyToUserId +
                ", statusFavorited=" + statusFavorited +
                ", statusInReplyToScreenName='" + statusInReplyToScreenName + '\'' +
                ", profileBackgroundColor='" + profileBackgroundColor + '\'' +
                ", profileTextColor='" + profileTextColor + '\'' +
                ", profileLinkColor='" + profileLinkColor + '\'' +
                ", profileSidebarFillColor='" + profileSidebarFillColor + '\'' +
                ", profileSidebarBorderColor='" + profileSidebarBorderColor + '\'' +
                ", friendsCount=" + friendsCount +
                ", createdAt=" + createdAt +
                ", favouritesCount=" + favouritesCount +
                ", utcOffset=" + utcOffset +
                ", timeZone='" + timeZone + '\'' +
                ", profileBackgroundImageUrl='" + profileBackgroundImageUrl + '\'' +
                ", profileBackgroundTile='" + profileBackgroundTile + '\'' +
                ", following=" + following +
                ", notificationEnabled=" + notificationEnabled +
                ", statusesCount=" + statusesCount +
                ", geoEnabled=" + geoEnabled +
                ", verified=" + verified +
                '}';
    }
}
