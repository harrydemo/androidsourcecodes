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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import weibo4android.http.AccessToken;
import weibo4android.http.HttpClient;
import weibo4android.http.ImageItem;
import weibo4android.http.PostParameter;
import weibo4android.http.RequestToken;
import weibo4android.http.Response;
import weibo4android.org.json.JSONException;
import weibo4android.org.json.JSONObject;
import weibo4android.util.URLEncodeUtils;

/**
 * A java reporesentation of the <a href="http://open.t.sina.com.cn/wiki/">Weibo API</a>
 * @editor sinaWeibo
 */
/**
 * @author sinaWeibo
 *
 */

public class Weibo extends WeiboSupport implements java.io.Serializable {
	//haoyaogang app token and key
	public static String CONSUMER_KEY = "2578116215";
	public static String CONSUMER_SECRET = "76fb782baf7588e023dff5ba2d0bc1b9";
    private String baseURL = Configuration.getScheme() + "api.t.sina.com.cn/";
    private String searchBaseURL = Configuration.getScheme() + "api.t.sina.com.cn/";
    private static final long serialVersionUID = -1486360080128882436L;
    
    //----------------------------寰崥鎼滅储 API------------------------------------
    /**
     * 鎼滅储寰崥鐢ㄦ埛 (浠呭鏂版氮鍚堜綔寮�彂鑰呭紑鏀�
     * @param query
     * @return a list of User
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Users/search">users/search </a>
     */
	public List<User> searchUser(Query query) throws WeiboException {
		return User.constructUsers(get(searchBaseURL + "users/search.json",
				query.getParameters(), false));
	}
    /**
     * 鎼滅储寰崥鏂囩珷 (浠呭鏂版氮鍚堜綔寮�彂鑰呭紑鏀�
     * @param query
     * @return
     * @throws WeiboException
     */
    
    public List<SearchResult> search(Query query) throws WeiboException {
        return SearchResult.constructResults(get(searchBaseURL + "search.json", query.getParameters(), true));
    }
    /**
     * 鎼滅储寰崥(澶氭潯浠剁粍鍚� (浠呭鍚堜綔寮�彂鑰呭紑鏀�
     * @param query
     * @return a list of statuses
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/search">Statuses/search
     */
    public List<Status> statussearch(Query query) throws WeiboException{
    	return Status.constructStatuses(get(searchBaseURL + "statuses/search.json", query.getParameters(), true));
    }
  
    //----------------------------鏀惰棌鎺ュ彛----------------------------------------
 
    /**
     * 鑾峰彇褰撳墠鐢ㄦ埛鐨勬敹钘忓垪琛�     * @param page the number of page
     * @return List<Status>
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites">favorites </a>
     * @since Weibo4J 1.2.0
     */
    public List<Status> getFavorites(int page) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "favorites.json", "page", String.valueOf(page), true));
    }
    


    /**
     * 鏀惰棌涓�潯寰崥娑堟伅
     * @param id the ID of the status to favorite
     * @return Status
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites/create">favorites/create </a>
     */
    public Status createFavorite(long id) throws WeiboException {
    	return new Status(http.post(getBaseURL() + "favorites/create/" + id + ".json", true));
    }

    /**
     * 鍒犻櫎寰崥鏀惰棌.娉ㄦ剰锛氬彧鑳藉垹闄よ嚜宸辨敹钘忕殑淇℃伅
     * @param id the ID of the status to un-favorite
     * @return Status
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites/destroy">favorites/destroy </a>
     */
    public Status destroyFavorite(long id) throws WeiboException {
    	return new Status(http.post(getBaseURL() + "favorites/destroy/" + id + ".json", true));
    }

    /**
     * 鎵归噺鍒犻櫎寰崥鏀惰棌
     * @param ids 瑕佸垹闄ょ殑涓�粍宸叉敹钘忕殑寰崥娑堟伅ID锛岀敤鍗婅閫楀彿闅斿紑锛屼竴娆℃彁浜ゆ渶澶氭彁渚�0涓狪D
     * @return Status
     * @throws WeiboException
     * @Ricky
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites/destroy_batch">favorites/destroy_batch</a>
     * @since Weibo4J 1.2.0
     */
    public List<Status> destroyFavorites(String ids)throws WeiboException{
    	return Status.constructStatuses(http.post(getBaseURL()+"favorites/destroy_batch.json",
    			new PostParameter[]{new PostParameter("ids",ids)},true));
    }

    public List<Status> destroyFavorites(String...ids)throws WeiboException{
    	 StringBuilder sb = new StringBuilder();
	  	 for(String id : ids) {
	  		 sb.append(id).append(',');
	  	 }
	  	 sb.deleteCharAt(sb.length() - 1);
    	return Status.constructStatuses(http.post(getBaseURL()+"favorites/destroy_batch.json",
    			new PostParameter[]{new PostParameter("ids",sb.toString())},true));
    }
    //----------------------------璐﹀彿鎺ュ彛 ----------------------------------------
    

    /**
     * 楠岃瘉褰撳墠鐢ㄦ埛韬唤鏄惁鍚堟硶
     * @return user
     * @since Weibo4J 1.2.0
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/verify_credentials">account/verify_credentials </a>
     */
    public User verifyCredentials() throws WeiboException {
        /*return new User(get(getBaseURL() + "account/verify_credentials.xml"
                , true), this);*/
    	return new User(get(getBaseURL() + "account/verify_credentials.json"
                , true).asJSONObject());
    }

    /**
     * 鏇存敼璧勬枡
     * @param name Optional. Maximum of 20 characters.
     * @param email Optional. Maximum of 40 characters. Must be a valid email address.
     * @param url Optional. Maximum of 100 characters. Will be prepended with "http://" if not present.
     * @param location Optional. Maximum of 30 characters. The contents are not normalized or geocoded in any way.
     * @param description Optional. Maximum of 160 characters.
     * @return the updated user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/update_profile">account/update_profile </a>
     */
    public User updateProfile(String name, String email, String url
            , String location, String description) throws WeiboException {
        List<PostParameter> profile = new ArrayList<PostParameter>(5);
        addParameterToList(profile, "name", name);
        addParameterToList(profile, "email", email);
        addParameterToList(profile, "url", url);
        addParameterToList(profile, "location", location);
        addParameterToList(profile, "description", description);
        return new User(http.post(getBaseURL() + "account/update_profile.json"
                , profile.toArray(new PostParameter[profile.size()]), true).asJSONObject());
    }

    /**
     * 鏇存敼澶村儚
     * @param image
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/update_profile_image">account/update_profile_image</a>
     */
    public User updateProfileImage(File image)throws WeiboException {
    	return new User(http.multPartURL("image",getBaseURL() + "account/update_profile_image.json",
    			new PostParameter[]{new PostParameter("source",CONSUMER_KEY)},image, true).asJSONObject());
    }

    /**
     *鑾峰彇褰撳墠鐢ㄦ埛API璁块棶棰戠巼闄愬埗<br>
     * @return the rate limit status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/rate_limit_status">account/rate_limit_status </a>
     */
    public RateLimitStatus rateLimitStatus() throws WeiboException {
    	 return new RateLimitStatus(http.get(getBaseURL() + "account/rate_limit_status.json" , true),this);
    }

    
    /**
     * 褰撳墠鐢ㄦ埛閫�嚭鐧诲綍
     * @return a user's object
     * @throws WeiboException when Weibo service or network is unavailable
     */
    public User endSession() throws WeiboException {
    	return new User(get(getBaseURL() + "account/end_session.json", true).asJSONObject());
    }
    //----------------------------Tags鎺ュ彛 ----------------------------------------
    
    /**
     * 杩斿洖鐢ㄦ埛鐨勬爣绛句俊鎭�     * @param user_id 鐢ㄦ埛id
     * @param paging 鍒嗛〉鏁版嵁
     * @return tags
     * @throws WeiboException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags">Tags </a>
     */
    public List<Tag> getTags(String userId,Paging paging) throws WeiboException{
    	return Tag.constructTags(get(getBaseURL()+"tags.json",
    			new PostParameter[]{new PostParameter("user_id",userId)},
    			paging,true));
    }
    /**
     * 涓哄綋鍓嶇櫥褰曠敤鎴锋坊鍔犳柊鐨勭敤鎴锋爣绛�     * @param tags
     * @return tagid
     * @throws WeiboException
     * @throws JSONException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/create">Tags/create </a>
     */
     public List<Tag> createTags(String ...tags)throws WeiboException{
    	 StringBuffer sb= new StringBuffer();
    	 for(String tag:tags){
    		 sb.append(tag).append(",");
    	 }
    	 sb.deleteCharAt(sb.length()-1);
    	 return createTags(sb.toString());
       }
    
    /**
    * 涓哄綋鍓嶇櫥褰曠敤鎴锋坊鍔犳柊鐨勭敤鎴锋爣绛�    * @param tags 瑕佸垱寤虹殑涓�粍鏍囩锛岀敤鍗婅閫楀彿闅斿紑銆�    * @return tagid
    * @throws WeiboException
    * @throws JSONException
    * @since Weibo4J 1.2.0
    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/create">Tags/create </a>
    */
    
    public List<Tag> createTags(String tags)throws WeiboException{
        return Tag.constructTags(http.post(getBaseURL()+"tags/create.json", 
        new PostParameter[]{new PostParameter("tags",tags)},true));
       
       }
    /**
     * 杩斿洖鐢ㄦ埛鎰熷叴瓒ｇ殑鏍囩
     * @return a list of tags
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/suggestions">Tags/suggestions </a>
     */
    
    public List<Tag> getSuggestionsTags()throws WeiboException{
    	return Tag.constructTags(get(getBaseURL()+"tags/suggestions.json",true));
    }
    /**
     * 鍒犻櫎鏍囩
     * @param tag_id
     * @return 
     * @throws WeiboException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/destroy">Tags/destroy </a>
     */
    
    public  boolean destoryTag(String tag_id)throws WeiboException{
        try {
			return  http.post(getBaseURL()+"tags/destroy.json",
			new PostParameter[]{new PostParameter("tag_id",tag_id)},true).asJSONObject().getInt("result") ==0;
		} catch (JSONException e) {
			throw new WeiboException(e);
		}
       }
    /**
     * 鍒犻櫎涓�粍鏍囩
     * @param ids
     * @return tagid
     * @throws WeiboException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/destroy_batch">Tags/destroy_batch </a>
     */
    
    public List<Tag> destory_batchTags(String ids)throws WeiboException{
        return Tag.constructTags(http.post(getBaseURL()+"tags/destroy_batch.json",
        new PostParameter[]{new PostParameter("ids",ids)},true));
       }
    
  
    
    //----------------------------榛戝悕鍗曟帴鍙�---------------------------------------

    /**
     * 灏嗘煇鐢ㄦ埛鍔犲叆鐧诲綍鐢ㄦ埛鐨勯粦鍚嶅崟
     * @param id 鐢ㄦ埛id
     * @return the blocked user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Blocks/create">Blocks/create</a>
     */
    public User createBlock(String userid) throws WeiboException {
        return new User(http.post(getBaseURL() + "blocks/create.json",
    			new PostParameter[]{new PostParameter("user_id", userid)}, true).asJSONObject());
    }
    /**
     * 灏嗘煇鐢ㄦ埛鍔犲叆鐧诲綍鐢ㄦ埛鐨勯粦鍚嶅崟
     * @param screenName 鐢ㄦ埛鏄电О
     * @return the blocked user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
      * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Blocks/create">Blocks/create</a>
     */
    public User createBlockByScreenName(String screenName) throws WeiboException {
    	return new User(http.post(getBaseURL() + "blocks/create.json",
    			new PostParameter[]{new PostParameter("screen_name", screenName)}, true).asJSONObject());
    }


    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     * @param id the ID or screen_name of the user to block
     * @return the unblocked user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public User destroyBlock(String id) throws WeiboException {
    	return new User(http.post(getBaseURL() + "blocks/destroy.json",
    			new PostParameter[]{new PostParameter("id",id)}, true).asJSONObject());
    }


    /**
     * Tests if a friendship exists between two users.
     * @param id The ID or screen_name of the potentially blocked user.
     * @return  if the authenticating user is blocking a target user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public boolean existsBlock(String id) throws WeiboException {
        try{
            return -1 == get(getBaseURL() + "blocks/exists.json?user_id="+id, true).
                    asString().indexOf("<error>You are not blocking this user.</error>");
        }catch(WeiboException te){
            if(te.getStatusCode() == 404){
                return false;
            }
            throw te;
        }
    }

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * @return a list of user objects that the authenticating user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<User> getBlockingUsers() throws
            WeiboException {
        /*return User.constructUsers(get(getBaseURL() +
                "blocks/blocking.xml", true), this);*/
    	return User.constructUsers(get(getBaseURL() +
                "blocks/blocking.json", true));
    }

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * @param page the number of page
     * @return a list of user objects that the authenticating user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<User> getBlockingUsers(int page) throws
            WeiboException {
        /*return User.constructUsers(get(getBaseURL() +
                "blocks/blocking.xml?page=" + page, true), this);*/
    	return User.constructUsers(get(getBaseURL() +
                "blocks/blocking.json?page=" + page, true));
    }

    /**
     * Returns an array of numeric user ids the authenticating user is blocking.
     * @return Returns an array of numeric user ids the authenticating user is blocking.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public IDs getBlockingUsersIDs() throws WeiboException {
//        return new IDs(get(getBaseURL() + "blocks/blocking/ids.xml", true));
    	return new IDs(get(getBaseURL() + "blocks/blocking/ids.json", true),this);
    }

    //----------------------------Social Graph鎺ュ彛-----------------------------------
    
    /**
     * 杩斿洖鐢ㄦ埛鍏虫敞鐨勪竴缁勭敤鎴风殑ID鍒楄〃
     * @param userid 鐢ㄦ埛id
     * @param paging
     * @return
     * @throws WeiboException 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">Friends/ids</a>
     */
    public IDs getFriendsIDSByUserId(String userid,Paging paging) throws WeiboException{
    	return new IDs(get(baseURL+"friends/ids.json",
    			new PostParameter[]{new PostParameter("user_id", userid)},paging, true),this);
    }
    /**
     * 杩斿洖鐢ㄦ埛鍏虫敞鐨勪竴缁勭敤鎴风殑ID鍒楄〃
     * @param userid 鐢ㄦ埛鏄电О
     * @param paging
     * @return
     * @throws WeiboException 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">Friends/ids</a>
     */
    public IDs getFriendsIDSByScreenName(String screenName,Paging paging) throws WeiboException{
    	return new IDs(get(baseURL+"friends/ids.json",
    			new PostParameter[]{new PostParameter("screen_name", screenName)},paging, true),this);
    }
    /**
     * 杩斿洖鐢ㄦ埛鐨勭矇涓濈敤鎴稩D鍒楄〃
     * @param userid 鐢ㄦ埛id
     * @param paging
     * @return
     * @throws WeiboException 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">Followers/ids</a>
     */
    public IDs getFollowersIDSByUserId(String userid,Paging paging) throws WeiboException{
    	return new IDs(get(baseURL+"followers/ids.json",
    			new PostParameter[]{new PostParameter("user_id", userid)},paging, true),this);
    }
    /**
     * 杩斿洖鐢ㄦ埛鐨勭矇涓濈敤鎴稩D鍒楄〃
     * @param userid 鐢ㄦ埛鏄电О
     * @param paging
     * @return
     * @throws WeiboException 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">Followers/ids</a>
     */
    public IDs getFollowersIDSByScreenName(String screenName,Paging paging) throws WeiboException{
    	return new IDs(get(baseURL+"followers/ids.json",
    			new PostParameter[]{new PostParameter("screen_name", screenName)},paging, true),this);
    }
    
    
    
    //----------------------------璇濋鎺ュ彛-------------------------------------------
    /**
     * 鑾峰彇鐢ㄦ埛鐨勮瘽棰樸�
     * @return the result
     * @throws WeiboException when Weibo service or network is unavailable
     * @since  Weibo4J 1.2.0 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends">Trends </a>
     */
    public List<UserTrend> getTrends(String userid,Paging paging) throws WeiboException {
        return UserTrend.constructTrendList(get(baseURL + "trends.json",
        		new PostParameter[]{new PostParameter("user_id", userid)},paging,true));
    }
    /**
     * 鑾峰彇鏌愪竴璇濋涓嬬殑寰崥
     * @param trendName 璇濋鍚嶇О
     * @return list status
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/statuses">Trends/statuses </a>
     */
    public List<Status> getTrendStatus(String trendName,Paging paging) throws WeiboException {
    	return Status.constructStatuses(get(baseURL + "trends/statuses.json",
    			new PostParameter[]{new PostParameter("trend_name", trendName)},
    			paging,true));
    }
    /**鍏虫敞鏌愪竴涓瘽棰�     * @param treandName 璇濋鍚嶇О
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/follow">trends/follow</a>
     */
    public UserTrend trendsFollow(String treandName) throws WeiboException {
			return  new UserTrend(http.post(baseURL+"trends/follow.json",new PostParameter[]{new PostParameter("trend_name", treandName)},true));
    }
    /**鍙栨秷瀵规煇璇濋鐨勫叧娉ㄣ�
     * @param trendId 璇濋id
     * @return result
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/destroy">Trends/destroy</a>
     */
    public boolean trendsDestroy(String trendId) throws WeiboException{
    	JSONObject obj= http.delete(baseURL+"trends/destroy.json?trend_id="+trendId+"&source="+Weibo.CONSUMER_KEY, true).asJSONObject();
    	try {
			return obj.getBoolean("result");
		} catch (JSONException e) {
			throw new WeiboException("e");
		}
    }
    /**
     * 鎸夊皬鏃惰繑鍥炵儹闂ㄨ瘽棰�     * @param baseApp 鏄惁鍩轰簬褰撳墠搴旂敤鏉ヨ幏鍙栨暟鎹�1琛ㄧず鍩轰簬褰撳墠搴旂敤鏉ヨ幏鍙栨暟鎹�
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/hourly">Trends/hourly</a>
     */
    public List<Trends> getTrendsHourly(Integer baseApp)throws WeiboException{
    	if(baseApp==null)
    		baseApp=0;
    	return Trends.constructTrendsList(get(baseURL+"trends/hourly.json","base_app",baseApp.toString() ,true));
    }
    /**
     * 杩斿洖鏈�繎涓�ぉ鍐呯殑鐑棬璇濋銆�     * @param baseApp 鏄惁鍩轰簬褰撳墠搴旂敤鏉ヨ幏鍙栨暟鎹�1琛ㄧず鍩轰簬褰撳墠搴旂敤鏉ヨ幏鍙栨暟鎹�
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/daily">Trends/daily</a>
     */
    public List<Trends> getTrendsDaily(Integer baseApp)throws WeiboException{
    	if(baseApp==null)
    		baseApp=0;
    	return Trends.constructTrendsList(get(baseURL+"trends/daily.json","base_app",baseApp.toString() ,true));
    }
    /**
     * 杩斿洖鏈�繎涓�懆鍐呯殑鐑棬璇濋銆�     * @param baseApp 鏄惁鍩轰簬褰撳墠搴旂敤鏉ヨ幏鍙栨暟鎹�1琛ㄧず鍩轰簬褰撳墠搴旂敤鏉ヨ幏鍙栨暟鎹�
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/weekly">Trends/weekly</a>
     */
    public List<Trends> getTrendsWeekly(Integer baseApp)throws WeiboException{
    	if(baseApp==null)
    		baseApp=0;
    	return Trends.constructTrendsList(get(baseURL+"trends/weekly.json","base_app",baseApp.toString() ,true));
    }
    
    
    private String toDateStr(Date date){
        if(null == date){
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

  
    //----------------------------鍏虫敞鎺ュ彛-------------------------------------------
    
    /**
     * 鍏虫敞涓�釜鐢ㄦ埛銆傚叧娉ㄦ垚鍔熷垯杩斿洖鍏虫敞浜虹殑璧勬枡
     * @param id 瑕佸叧娉ㄧ殑鐢ㄦ埛ID 鎴栬�寰崥鏄电О 
     * @return the befriended user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/create">friendships/create </a>
     */
    public User createFriendship(String id) throws WeiboException {
    	 return new User(get(getBaseURL() + "friendships/create.json", "id",id, true).asJSONObject());
    }
    /**
     * 鍏虫敞涓�釜鐢ㄦ埛銆傚叧娉ㄦ垚鍔熷垯杩斿洖鍏虫敞浜虹殑璧勬枡
     * @param id 寰崥鏄电О 
     * @return the befriended user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/create">friendships/create </a>
     */
    public User createFriendshipByScreenName(String screenName) throws WeiboException {
    	return new User(http.post(getBaseURL() + "friendships/create.json", 
    			new PostParameter[]{new PostParameter("screen_name", screenName)}
    			, true).asJSONObject());
    }
    /**
     * 鍏虫敞涓�釜鐢ㄦ埛銆傚叧娉ㄦ垚鍔熷垯杩斿洖鍏虫敞浜虹殑璧勬枡
     * @param id 瑕佸叧娉ㄧ殑鐢ㄦ埛ID 
     * @return the befriended user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/create">friendships/create </a>
     */
    public User createFriendshipByUserid(String userid) throws WeiboException {
    	return new User(http.post(getBaseURL() + "friendships/create.json",
    		new PostParameter[] {new PostParameter("user_id", userid)}
    		, true).asJSONObject());
    }
    /**
     * 鍙栨秷瀵规煇鐢ㄦ埛鐨勫叧娉�     * @param id 瑕佸彇娑堝叧娉ㄧ殑鐢ㄦ埛ID 鎴栬�寰崥鏄电О 
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/destroy">friendships/destroy </a>
     */
    public User destroyFriendship(String id) throws WeiboException {
    	 return new User(http.post(getBaseURL() + "friendships/destroy.json", "id",id, true).asJSONObject());
    }
    /**
     * 鍙栨秷瀵规煇鐢ㄦ埛鐨勫叧娉�     * @param id 瑕佸彇娑堝叧娉ㄧ殑鐢ㄦ埛ID 
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/destroy">friendships/destroy </a>
     */
    public User destroyFriendshipByUserid(int userid) throws WeiboException {
    	return new User(http.post(getBaseURL() + "friendships/destroy.json", "user_id",""+userid, true).asJSONObject());
    }
    /**
     * 鍙栨秷瀵规煇鐢ㄦ埛鐨勫叧娉�     * @param id 瑕佸彇娑堝叧娉ㄧ殑鐢ㄦ埛鏄电О
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/destroy">friendships/destroy </a>
     */
    public User destroyFriendshipByScreenName(String screenName) throws WeiboException {
    	return new User(http.post(getBaseURL() + "friendships/destroy.json", "screen_name",screenName, true).asJSONObject());
    }
    
    //----------------------------鐢ㄦ埛鎺ュ彛-------------------------------------------
    /**
     * 鎸夌敤鎴稩D鎴栨樀绉拌繑鍥炵敤鎴疯祫鏂欎互鍙婄敤鎴风殑鏈�柊鍙戝竷鐨勪竴鏉″井鍗氭秷鎭�
     * @param id 鐢ㄦ埛ID鎴栬�鏄电О(string)
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Users/show">users/show </a>
     * @since Weibo4J 1.2.0
     */
    public User showUser(String user_id) throws WeiboException {
    	 return new User(get(getBaseURL() + "users/show.json",new PostParameter[]{new PostParameter("id", user_id)}
                 ,http.isAuthenticationEnabled()).asJSONObject());
    }
    

    /**
     * 鑾峰彇褰撳墠鐢ㄦ埛鍏虫敞鍒楄〃鍙婃瘡涓叧娉ㄧ敤鎴风殑鏈�柊涓�潯寰崥锛岃繑鍥炵粨鏋滄寜鍏虫敞鏃堕棿鍊掑簭鎺掑垪锛屾渶鏂板叧娉ㄧ殑鐢ㄦ埛鎺掑湪鏈�墠闈�
     * @return the list of friends
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
     * @since Weibo4J 1.2.0
     */
    public List<User> getFriendsStatuses() throws WeiboException {
    	return User.constructResult(get(getBaseURL() + "statuses/friends.json", true));
    }
    /**
     * 鑾峰彇褰撳墠鐢ㄦ埛鍏虫敞鍒楄〃鍙婃瘡涓叧娉ㄧ敤鎴风殑鏈�柊涓�潯寰崥锛岃繑鍥炵粨鏋滄寜鍏虫敞鏃堕棿鍊掑簭鎺掑垪锛屾渶鏂板叧娉ㄧ殑鐢ㄦ埛鎺掑湪鏈�墠闈�
     * @return the list of friends
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
     * @since Weibo4J 1.2.0
     */
    public List<User>getFriendsStatuses(int cursor) throws WeiboException {
    	return User.constructUser(get(getBaseURL()+"statuses/friends.json"
    			,new PostParameter[]{new PostParameter("cursor", cursor)},true));
    }
    /**
     * 鑾峰彇鎸囧畾鐢ㄦ埛鍏虫敞鍒楄〃鍙婃瘡涓叧娉ㄧ敤鎴风殑鏈�柊涓�潯寰崥锛岃繑鍥炵粨鏋滄寜鍏虫敞鏃堕棿鍊掑簭鎺掑垪锛屾渶鏂板叧娉ㄧ殑鐢ㄦ埛鎺掑湪鏈�墠闈�
     * @param id 鐢ㄦ埛ID 鎴栬�鏄电О 
     * @param paging 鍒嗛〉鏁版嵁
     * @return the list of friends
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
     */
    public List<User> getFriendsStatuses(String id, Paging paging) throws WeiboException {
    	return User.constructUsers(get(getBaseURL() + "statuses/friends.json",
    			new PostParameter[]{new PostParameter("id", id)},paging,
                 true));
    }
    /**
     * 鑾峰彇褰撳墠鐢ㄦ埛鍏虫敞鍒楄〃鍙婃瘡涓叧娉ㄧ敤鎴风殑鏈�柊涓�潯寰崥锛岃繑鍥炵粨鏋滄寜鍏虫敞鏃堕棿鍊掑簭鎺掑垪锛屾渶鏂板叧娉ㄧ殑鐢ㄦ埛鎺掑湪鏈�墠闈�
     * @param paging controls pagination
     * @return the list of friends
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
     */
    public List<User> getFriendsStatuses(Paging paging) throws WeiboException {
        /*return User.constructUsers(get(getBaseURL() + "statuses/friends.xml", null,
                paging, true), this);*/
    	return User.constructUsers(get(getBaseURL() + "statuses/friends.json", null,
                paging, true));
    }


    /**
     * 鑾峰彇鎸囧畾鐢ㄦ埛鍏虫敞鍒楄〃鍙婃瘡涓叧娉ㄧ敤鎴风殑鏈�柊涓�潯寰崥锛岃繑鍥炵粨鏋滄寜鍏虫敞鏃堕棿鍊掑簭鎺掑垪锛屾渶鏂板叧娉ㄧ殑鐢ㄦ埛鎺掑湪鏈�墠闈�
     * @param id id 鐢ㄦ埛ID 鎴栬�鏄电О 
     * @return the list of friends
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
     * @since Weibo4J 1.2.0
     */
    public List<User> getFriendsStatuses(String id) throws WeiboException {
        /*return User.constructUsers(get(getBaseURL() + "statuses/friends/" + id + ".xml"
                , false), this);*/
    	return User.constructUsers(get(getBaseURL() + "statuses/friends.json"
    			,new PostParameter[]{new PostParameter("id", id)}
                , false));
    }

    /**
     * 鑾峰彇褰撳墠鐢ㄦ埛绮変笣鍒楄〃鍙婂強姣忎釜绮変笣鐢ㄦ埛鏈�柊涓�潯寰崥锛岃繑鍥炵粨鏋滄寜鍏虫敞鏃堕棿鍊掑簭鎺掑垪锛屾渶鏂板叧娉ㄧ殑鐢ㄦ埛鎺掑湪鏈�墠闈�
     * @return List
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
     * @since Weibo4J 1.2.0
     */
    public List<User> getFollowersStatuses() throws WeiboException {
//        return User.constructUsers(get(getBaseURL() + "statuses/followers.xml", true), this);
    	return User.constructResult(get(getBaseURL() + "statuses/followers.json", true));
    }
 
    /**
     * 鑾峰彇鎸囧畾鐢ㄦ埛绮変笣鍒楄〃鍙婂強姣忎釜绮変笣鐢ㄦ埛鏈�柊涓�潯寰崥锛岃繑鍥炵粨鏋滄寜鍏虫敞鏃堕棿鍊掑簭鎺掑垪锛屾渶鏂板叧娉ㄧ殑鐢ㄦ埛鎺掑湪鏈�墠闈�
     * @param id   鐢ㄦ埛ID 鎴栬�鏄电О 
     * @param paging controls pagination
     * @return List
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
     */
    public List<User> getFollowersStatuses(String id, Paging paging) throws WeiboException {
    	return User.constructUsers(get(getBaseURL() + "statuses/followers.json", 
    			new PostParameter[]{new PostParameter("id", id)},
    			paging, true));
    }

    /**
     * 鑾峰彇褰撳墠鐢ㄦ埛绮変笣鍒楄〃鍙婂強姣忎釜绮変笣鐢ㄦ埛鏈�柊涓�潯寰崥锛岃繑鍥炵粨鏋滄寜鍏虫敞鏃堕棿鍊掑簭鎺掑垪锛屾渶鏂板叧娉ㄧ殑鐢ㄦ埛鎺掑湪鏈�墠闈�
     * @param paging  鍒嗛〉鏁版嵁
     * @return List
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
     */
    public List<User> getFollowersStatuses(Paging paging) throws WeiboException {
    	return User.constructUsers(get(getBaseURL() + "statuses/followers.json", null
                , paging, true));
    }
    /**
     * 鑾峰彇褰撳墠鐢ㄦ埛绮変笣鍒楄〃鍙婂強姣忎釜绮変笣鐢ㄦ埛鏈�柊涓�潯寰崥锛岃繑鍥炵粨鏋滄寜鍏虫敞鏃堕棿鍊掑簭鎺掑垪锛屾渶鏂板叧娉ㄧ殑鐢ㄦ埛鎺掑湪鏈�墠闈�
     * @param cursor 鍒嗛〉鏁版嵁
     * @return List
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
     */
    public List<User> getFollowersStatuses(int cursor) throws WeiboException {
    	return User.constructUser(get(getBaseURL()+"statuses/followers.json",
    			new PostParameter[]{new PostParameter("cursor", cursor)},true));
    }
   
    /**
     * 鑾峰彇鎸囧畾鐢ㄦ埛鍓�0 绮変笣鍒楄〃鍙婂強姣忎釜绮変笣鐢ㄦ埛鏈�柊涓�潯寰崥锛岃繑鍥炵粨鏋滄寜鍏虫敞鏃堕棿鍊掑簭鎺掑垪锛屾渶鏂板叧娉ㄧ殑鐢ㄦ埛鎺掑湪鏈�墠闈�
     * @return List
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
     */
    public List<User> getFollowersStatuses(String id) throws WeiboException {
    	 return User.constructUsers(get(getBaseURL() + "statuses/followers.json",new PostParameter[]{new PostParameter("id", id)}, true));
    }

    /**
     * 鑾峰彇绯荤粺鎺ㄨ崘鐢ㄦ埛
     * @param category 杩斿洖鏌愪竴绫诲埆鐨勬帹鑽愮敤鎴� 鍏蜂綋鐩綍鍙傝鏂囨。
     * @return User
     * @throws WeiboException
     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Users/hot">users/hot</a> 
     * @since Weibo4J 1.2.0
     */
    public List<User> getHotUsers(String category) throws WeiboException{
    	return User.constructUsers(get(getBaseURL()+"users/hot.json","category",  category, true));
    }
    
    /**
     * 鏇存柊褰撳墠鐧诲綍鐢ㄦ埛鎵�叧娉ㄧ殑鏌愪釜濂藉弸鐨勫娉ㄤ俊鎭�
     * @param userid 闇�淇敼澶囨敞淇℃伅鐨勭敤鎴稩D銆�     * @param remark 澶囨敞淇℃伅
     * @return
     * @throws WeiboException
     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/User/friends/update_remark">friends/update_remark</a> 
     */
    public User updateRemark(String userid,String remark) throws WeiboException {
    	if(!URLEncodeUtils.isURLEncoded(remark))
    		remark=URLEncodeUtils.encodeURL(remark);
	return new User(http.post(getBaseURL()+"user/friends/update_remark.json",
    			new PostParameter[]{new PostParameter("user_id", userid),
    			new PostParameter("remark", remark)},
    			true).asJSONObject());
    }
    /**
     * 鏇存柊褰撳墠鐧诲綍鐢ㄦ埛鎵�叧娉ㄧ殑鏌愪釜濂藉弸鐨勫娉ㄤ俊鎭�
     * @param userid 闇�淇敼澶囨敞淇℃伅鐨勭敤鎴稩D銆�     * @param remark 澶囨敞淇℃伅
     * @return
     * @throws WeiboException
     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/User/friends/update_remark">friends/update_remark</a> 
     */
    public User updateRemark(Long userid,String remark) throws WeiboException {
	return updateRemark(Long.toString(userid), remark);
    }
    
    /**
     * 杩斿洖褰撳墠鐢ㄦ埛鍙兘鎰熷叴瓒ｇ殑鐢ㄦ埛
     * @return user list
     * @throws WeiboException
     * * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Users/suggestions">friends/update_remark</a> 
     */
    public List<User> getSuggestionUsers() throws WeiboException {
    	return User.constructUsers(get(getBaseURL()+"users/suggestions.json","with_reason", "0", true));
    }
    
    
    //-----------------------鑾峰彇涓嬭鏁版嵁闆�timeline)鎺ュ彛-----------------------------
    /**
     * 杩斿洖鏈�柊鐨�0鏉″叕鍏卞井鍗氥�杩斿洖缁撴灉闈炲畬鍏ㄥ疄鏃讹紝鏈�暱浼氱紦瀛�0绉�     * @return list of statuses of the Public Timeline
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/public_timeline">statuses/public_timeline </a>
     */
    public List<Status> getPublicTimeline() throws
            WeiboException {
        return Status.constructStatuses(get(getBaseURL() +
                "statuses/public_timeline.json", true));
    }
    /**杩斿洖鏈�柊鐨勫叕鍏卞井鍗�     * @param count 姣忔杩斿洖鐨勮褰曟暟
     * @param baseApp 鏄惁浠呰幏鍙栧綋鍓嶅簲鐢ㄥ彂甯冪殑淇℃伅銆�涓烘墍鏈夛紝1涓轰粎鏈簲鐢ㄣ�
     * @return
     * @throws WeiboException
     */
    public List<Status> getPublicTimeline(int count,int baseApp) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() +
        "statuses/public_timeline.json", 
        new PostParameter[]{
    		new PostParameter("count", count),
    		new PostParameter("base_app", baseApp)
    	}
        , false));
    }
    /**
     * 鑾峰彇褰撳墠鐧诲綍鐢ㄦ埛鍙婂叾鎵�叧娉ㄧ敤鎴风殑鏈�柊20鏉″井鍗氭秷鎭�<br/>
     * 鍜岀敤鎴风櫥褰�http://t.sina.com.cn 鍚庡湪鈥滄垜鐨勯椤碘�涓湅鍒扮殑鍐呭鐩稿悓銆�     * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
     *
     * @return list of the Friends Timeline
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
     */
    public List<Status> getFriendsTimeline() throws
            WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/friends_timeline.json", true));
    }
    /**
     *鑾峰彇褰撳墠鐧诲綍鐢ㄦ埛鍙婂叾鎵�叧娉ㄧ敤鎴风殑鏈�柊寰崥娑堟伅銆�br/>
     * 鍜岀敤鎴风櫥褰�http://t.sina.com.cn 鍚庡湪鈥滄垜鐨勯椤碘�涓湅鍒扮殑鍐呭鐩稿悓銆�     * @param paging 鐩稿叧鍒嗛〉鍙傛暟
     * @return list of the Friends Timeline
     * @throws WeiboException when Weibo service or network is unavailable
     * @since  Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
     */
    public List<Status> getFriendsTimeline(Paging paging) throws
            WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/friends_timeline.json",null, paging, true));
    }
    
    /**
     *鑾峰彇褰撳墠鐧诲綍鐢ㄦ埛鍙婂叾鎵�叧娉ㄧ敤鎴风殑鏈�柊寰崥娑堟伅銆�br/>
     * 鍜岀敤鎴风櫥褰�http://t.sina.com.cn 鍚庡湪鈥滄垜鐨勯椤碘�涓湅鍒扮殑鍐呭鐩稿悓銆�     * @return list of the home Timeline
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
     * @since  Weibo4J 1.2.0
     */
    public List<Status> getHomeTimeline() throws
            WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/home_timeline.json", true));
    }


    /**
     *鑾峰彇褰撳墠鐧诲綍鐢ㄦ埛鍙婂叾鎵�叧娉ㄧ敤鎴风殑鏈�柊寰崥娑堟伅銆�br/>
     * 鍜岀敤鎴风櫥褰�http://t.sina.com.cn 鍚庡湪鈥滄垜鐨勯椤碘�涓湅鍒扮殑鍐呭鐩稿悓銆�     * @param paging controls pagination
     * @return list of the home Timeline
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
     * @since  Weibo4J 1.2.0
     */
    public List<Status> getHomeTimeline(Paging paging) throws
            WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/home_timeline.json", null, paging, true));
    }
    /**
     * 杩斿洖鎸囧畾鐢ㄦ埛鏈�柊鍙戣〃鐨勫井鍗氭秷鎭垪琛�     * @param id   鏍规嵁鐢ㄦ埛ID(int64)鎴栬�寰崥鏄电О(string)杩斿洖鎸囧畾鐢ㄦ埛鐨勬渶鏂板井鍗氭秷鎭垪琛ㄣ�
     * @param paging 鍒嗛〉淇℃伅
     * @return list of the user Timeline
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
     */
    public List<Status> getUserTimeline(String id, Paging paging)
            throws WeiboException {
//        return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline/" + id + ".xml",
//                null, paging, http.isAuthenticationEnabled()), this);
        return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json",
        		 new PostParameter[]{new PostParameter("id", id)}, paging, http.isAuthenticationEnabled()));
    }
    public List<Status> getUserTimeline(String id,Integer baseAPP,Integer feature, Paging paging)
    throws WeiboException {
    	Map<String,String> maps= new HashMap<String, String>();
    	if(id!=null){
    		maps.put("id", id);
    	}
    	if(baseAPP!=null){
    		maps.put("base_app", baseAPP.toString());
    	}
    	if(feature!=null){
    		maps.put("feature", feature.toString());
    	}
    	return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json",
    			generateParameterArray(maps), paging, http.isAuthenticationEnabled()));
    }
    /**
     * 杩斿洖鎸囧畾鐢ㄦ埛鏈�柊鍙戣〃鐨勫井鍗氭秷鎭垪琛�     * @param id 鏍规嵁鐢ㄦ埛ID(int64)鎴栬�寰崥鏄电О(string)杩斿洖鎸囧畾鐢ㄦ埛鐨勬渶鏂板井鍗氭秷鎭垪琛ㄣ�
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
     */
    public List<Status> getUserTimeline(String id) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json",
    			new PostParameter[]{new PostParameter("id", id)},
    			http.isAuthenticationEnabled()));
    }
    /**
     *杩斿洖褰撳墠鐢ㄦ埛鏈�柊鍙戣〃鐨勫井鍗氭秷鎭垪琛�     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
     */
    public List<Status> getUserTimeline() throws
            WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json"
                , true));
    }

    /**
     *杩斿洖褰撳墠鐢ㄦ埛鏈�柊鍙戣〃鐨勫井鍗氭秷鎭垪琛�     * @param paging controls pagination
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
     * @since Weibo4J 1.2.0
     */
    public List<Status> getUserTimeline(Paging paging) throws
            WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json"
                , null, paging, true));
    }
    /**
     *杩斿洖褰撳墠鐢ㄦ埛鏈�柊鍙戣〃鐨勫井鍗氭秷鎭垪琛�     * @param paging controls pagination
     * @param base_app 鏄惁鍩轰簬褰撳墠搴旂敤鏉ヨ幏鍙栨暟鎹�1涓洪檺鍒舵湰搴旂敤寰崥锛�涓轰笉鍋氶檺鍒躲�
     * @param feature 寰崥绫诲瀷锛�鍏ㄩ儴锛�鍘熷垱锛�鍥剧墖锛�瑙嗛锛�闊充箰. 杩斿洖鎸囧畾绫诲瀷鐨勫井鍗氫俊鎭唴瀹广�
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
     * @since Weibo4J 1.2.0
     */
    public List<Status> getUserTimeline(Integer baseAPP,Integer feature,Paging paging) throws
    WeiboException {
    	return getUserTimeline(null,baseAPP,feature,paging);
    }
    /**
     * 杩斿洖鏈�柊20鏉℃彁鍒扮櫥褰曠敤鎴风殑寰崥娑堟伅锛堝嵆鍖呭惈@username鐨勫井鍗氭秷鎭級
     * @return the 20 most recent replies
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/mentions">Statuses/mentions </a>
     */
    public List<Status> getMentions() throws WeiboException {
        return Status.constructStatuses(get(getBaseURL() + "statuses/mentions.json",
                null, true));
    }

    /**
     * 杩斿洖鏈�柊鎻愬埌鐧诲綍鐢ㄦ埛鐨勫井鍗氭秷鎭紙鍗冲寘鍚獲username鐨勫井鍗氭秷鎭級
     * @param paging 鍒嗛〉鏁版嵁
     * @return the 20 most recent replies
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/mentions">Statuses/mentions </a>
     */
    public List<Status> getMentions(Paging paging) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/mentions.json",
                null, paging, true));
        /*return Status.constructStatuses(get(getBaseURL() + "statuses/mentions.xml",
                null, paging, true), this);*/
    }
 
    /**
     * 杩斿洖鏈�柊20鏉″彂閫佸強鏀跺埌鐨勮瘎璁恒�
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_timeline">Statuses/comments_timeline</a>
     */
    public List<Comment> getCommentsTimeline() throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_timeline.json", true));
    }
    /**
     * 杩斿洖鏈�柊n鏉″彂閫佸強鏀跺埌鐨勮瘎璁恒�
     * @param paging 鍒嗛〉鏁版嵁
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_timeline">Statuses/comments_timeline</a>
     */
    public List<Comment> getCommentsTimeline(Paging paging) throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_timeline.json",null,paging, true));
    }
    
    /**
     * 鑾峰彇鏈�柊20鏉″綋鍓嶇敤鎴峰彂鍑虹殑璇勮
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_by_me">Statuses/comments_by_me</a>
     */
    public List<Comment> getCommentsByMe() throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_by_me.json", true));
    }
    /**
     * 鑾峰彇褰撳墠鐢ㄦ埛鍙戝嚭鐨勮瘎璁�     *@param paging 鍒嗛〉鏁版嵁
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_by_me">Statuses/comments_by_me</a>
     */
    public List<Comment> getCommentsByMe(Paging paging) throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_by_me.json",null,paging, true));
    }
    
    /**
     * 杩斿洖鏈�柊20鏉″綋鍓嶇櫥褰曠敤鎴锋敹鍒扮殑璇勮
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_to_me">Statuses/comments_to_me</a>
     */
    public List<Comment> getCommentsToMe() throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_to_me.json", true));
    }
    /**
     * 杩斿洖褰撳墠鐧诲綍鐢ㄦ埛鏀跺埌鐨勮瘎璁�     *@param paging 鍒嗛〉鏁版嵁
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_to_me">Statuses/comments_to_me</a>
     */
    public List<Comment> getCommentsToMe(Paging paging) throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_to_me.json",null,paging, true));
    }


    
    /**
     * 杩斿洖鐢ㄦ埛杞彂鐨勬渶鏂�0鏉″井鍗氫俊鎭�
     * @param id specifies the id of user
     * @return statuses
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/repost_by_me">Statuses/repost_by_me</a>
     */
    public List<Status> getrepostbyme(String id)throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL()+"statuses/repost_by_me.json","id",id,true));
    }
    /**
     * 杩斿洖鐢ㄦ埛杞彂鐨勬渶鏂皀鏉″井鍗氫俊鎭�
     * @param id specifies the id of user
     * @return statuses
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/repost_by_me">Statuses/repost_by_me</a>
     */
    public List<Status> getrepostbyme(String id,Paging paging)throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL()+"statuses/repost_by_me.json",
    			new PostParameter[]{new PostParameter("id", id)},
    			paging,true));
    }
    /**
     * 杩斿洖涓�潯鍘熷垱寰崥鐨勬渶鏂�0鏉¤浆鍙戝井鍗氫俊鎭湰鎺ュ彛鏃犳硶瀵归潪鍘熷垱寰崥杩涜鏌ヨ銆�     * @param id specifies the id of original status.
     * @return a list of statuses object
     * @throws WeiboException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/repost_timeline">Statuses/repost_timeline</a>
     */
    public List<Status>getreposttimeline(String id)throws WeiboException{
    	return Status.constructStatuses(get(getBaseURL()+"statuses/repost_timeline.json",
    			"id", id,true));
    }
    /**
     * 杩斿洖涓�潯鍘熷垱寰崥鐨勬渶鏂皀鏉¤浆鍙戝井鍗氫俊鎭湰鎺ュ彛鏃犳硶瀵归潪鍘熷垱寰崥杩涜鏌ヨ銆�     * @param id specifies the id of original status.
     * @return a list of statuses object
     * @throws WeiboException
     * @since Weibo4J 1.2.0  
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/repost_timeline">Statuses/repost_timeline</a>
     */
    public List<Status>getreposttimeline(String id,Paging paging)throws WeiboException{
    	return Status.constructStatuses(get(getBaseURL()+"statuses/repost_timeline.json"
    			,new PostParameter[]{new PostParameter("id", id)},
    			paging,true));
    }
    /**
     * 鏍规嵁寰崥娑堟伅ID杩斿洖鏌愭潯寰崥娑堟伅鐨�0鏉¤瘎璁哄垪琛�     * @param id specifies the ID of status
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0  
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments">Statuses/comments</a>
     */
    public List<Comment> getComments(String id) throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments.json","id",id, true));
    }
    /**
     * 鏍规嵁寰崥娑堟伅ID杩斿洖鏌愭潯寰崥娑堟伅鐨刵璇勮鍒楄〃
     * @param id specifies the ID of status
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments">Statuses/comments</a>
     */
    public List<Comment> getComments(String id,Paging paging) throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments.json",
    			new PostParameter[]{new PostParameter("id", id)},paging,
    			true));
    }
    /**
     * 鎵归噺鑾峰彇n鏉″井鍗氭秷鎭殑璇勮鏁板拰杞彂鏁般�瑕佽幏鍙栬瘎璁烘暟鍜岃浆鍙戞暟鐨勫井鍗氭秷鎭疘D鍒楄〃锛岀敤閫楀彿闅斿紑
     * 涓�璇锋眰鏈�鍙互鑾峰彇100鏉″井鍗氭秷鎭殑璇勮鏁板拰杞彂鏁�     * @param ids ids a string, separated by commas
     * @return a list of counts objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0  
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/counts">Statuses/counts</a>
     */
    public List<Count> getCounts(String ids) throws WeiboException{
    	return Count.constructCounts(get(getBaseURL() + "statuses/counts.json","ids",ids, true));
    }

    
    /**
     *鑾峰彇褰撳墠鐢ㄦ埛Web涓荤珯鏈娑堟伅鏁�     * @return count objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @throws JSONException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/unread">Statuses/unread</a>
     */
    public Count getUnread() throws WeiboException{
    	return new Count(get(getBaseURL() + "statuses/unread.json", true));
    }
    /**
     * 鑾峰彇褰撳墠鐢ㄦ埛Web涓荤珯鏈娑堟伅鏁�     * @param withNewStatus 1琛ㄧず缁撴灉涓寘鍚玭ew_status瀛楁锛�琛ㄧず缁撴灉涓嶅寘鍚玭ew_status瀛楁銆俷ew_status瀛楁琛ㄧず鏄惁鏈夋柊寰崥娑堟伅锛�琛ㄧず鏈夛紝0琛ㄧず娌℃湁
     * @param sinceId 鍙傛暟鍊间负寰崥id銆傝鍙傛暟闇�厤鍚坵ith_new_status鍙傛暟浣跨敤锛岃繑鍥瀞ince_id涔嬪悗锛屾槸鍚︽湁鏂板井鍗氭秷鎭骇鐢�     * @return
     * @throws WeiboException
     * @throws JSONException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/unread">Statuses/unread</a>
     */
    public Count getUnread(Integer withNewStatus,Long sinceId) throws WeiboException, JSONException {
    	Map<String, String> maps= new HashMap<String, String>();
    	if(withNewStatus!=null)
    		maps.put("with_new_status",Integer.toString( withNewStatus));
    	if(sinceId!=null)
    		maps.put("since_id",Long.toString( sinceId));
    	return new Count(get(getBaseURL() + "statuses/unread.json", generateParameterArray(maps),true).asJSONObject());
    }
    
    /**
     *灏嗗綋鍓嶇櫥褰曠敤鎴风殑鏌愮鏂版秷鎭殑鏈鏁颁负0
     * @param type  1. 璇勮鏁帮紝2.@ me鏁帮紝3. 绉佷俊鏁帮紝4. 鍏虫敞鏁�     * @return
     * @throws WeiboException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/reset_count">statuses/reset_count</a> 
     */
    public Boolean resetCount(int type)throws WeiboException{
    	boolean res=false;
    	JSONObject json = null;
    	    try {
		 json=http.post(getBaseURL()+"statuses/reset_count.json",
			new PostParameter[]{new PostParameter("type",type)},true).asJSONObject();
		res=json.getBoolean("result");
    	    } catch (JSONException je) {
		throw new WeiboException(je.getMessage() + ":" + json,
			je);
	    }
	    return res;
    }

    /**
     * 杩斿洖鏂版氮寰崥瀹樻柟鎵�湁琛ㄦ儏銆侀瓟娉曡〃鎯呯殑鐩稿叧淇℃伅銆傚寘鎷煭璇�琛ㄦ儏绫诲瀷銆佽〃鎯呭垎绫伙紝鏄惁鐑棬绛夈�
     * @param type 琛ㄦ儏绫诲埆銆�face":鏅�琛ㄦ儏锛�ani"锛氶瓟娉曡〃鎯咃紝"cartoon"锛氬姩婕〃鎯�     * @param language 璇█绫诲埆锛�cnname"绠�綋锛�twname"绻佷綋
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Emotions">Emotions</a> 
     */
    public List<Emotion> getEmotions(String type,String language)throws WeiboException {
    	if(type==null)
    		type="face";
    	if(language==null)
    		language="cnname";
    	Map<String, String> maps= new HashMap<String, String>();
    	maps.put("type", type);
    	maps.put("language", language);
		return Emotion.constructEmotions(get(getBaseURL()+"emotions.json",generateParameterArray(maps),true));
    }
    /**
     * 杩斿洖鏂版氮寰崥绠�綋涓枃鐨勬櫘閫氳〃鎯呫�
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Emotions">Emotions</a> 
     */
    public List<Emotion> getEmotions()throws WeiboException {
    	return getEmotions(null,null);
    }
    //--------------寰崥璁块棶鎺ュ彛----------
    
    /**
     * 鏍规嵁ID鑾峰彇鍗曟潯寰崥娑堟伅锛屼互鍙婅寰崥娑堟伅鐨勪綔鑰呬俊鎭�
     * @param id 瑕佽幏鍙栫殑寰崥娑堟伅ID
     * @return 寰崥娑堟伅
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/show">statuses/show </a>
     */
    public Status showStatus(String id) throws WeiboException {
    	return new Status(get(getBaseURL() + "statuses/show/" + id + ".json", true));
    }
    /**
     * 鏍规嵁ID鑾峰彇鍗曟潯寰崥娑堟伅锛屼互鍙婅寰崥娑堟伅鐨勪綔鑰呬俊鎭�
     * @param id 瑕佽幏鍙栫殑寰崥娑堟伅ID
     * @return 寰崥娑堟伅
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/show">statuses/show </a>
     */
    public Status showStatus(long id) throws WeiboException {
    	return showStatus(Long.toString(id));
    }

    /**
     * 鍙戝竷涓�潯寰崥淇℃伅
     * @param status 瑕佸彂甯冪殑寰崥娑堟伅鏂囨湰鍐呭
     * @return the latest status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
     */
    public Status updateStatus(String status) throws WeiboException{

    	return new Status(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status)}, true));
    }
    
    /**
     * 鍙戝竷涓�潯寰崥淇℃伅
     * @param status    瑕佸彂甯冪殑寰崥娑堟伅鏂囨湰鍐呭
     * @param latitude  绾害銆傛湁鏁堣寖鍥达細-90.0鍒�90.0锛�琛ㄧず鍖楃含銆�     * @param longitude 缁忓害銆傛湁鏁堣寖鍥达細-180.0鍒�180.0锛�琛ㄧず涓滅粡銆�     * @return the latest status
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
     * @since Weibo4J 1.2.0
     */
    public Status updateStatus(String status, double latitude, double longitude) throws WeiboException{
    	return new Status(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status),
                        new PostParameter("lat", latitude),
                        new PostParameter("long", longitude)}, true));
    }

    /**
     * 鍙戝竷涓�潯寰崥淇℃伅
     * @param status  瑕佸彂甯冪殑寰崥娑堟伅鏂囨湰鍐呭
     * @param inReplyToStatusId 瑕佽浆鍙戠殑寰崥娑堟伅ID
     * @return the latest status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
     */
    public Status updateStatus(String status, long inReplyToStatusId) throws WeiboException {
    	 return new Status(http.post(getBaseURL() + "statuses/update.json",
                 new PostParameter[]{new PostParameter("status", status), new PostParameter("in_reply_to_status_id", String.valueOf(inReplyToStatusId)), new PostParameter("source", source)}, true));
    }

    /**
     *鍙戝竷涓�潯寰崥淇℃伅
     * @param status           瑕佸彂甯冪殑寰崥娑堟伅鏂囨湰鍐呭
     * @param inReplyToStatusId 瑕佽浆鍙戠殑寰崥娑堟伅ID
     * @param latitude  绾害銆傛湁鏁堣寖鍥达細-90.0鍒�90.0锛�琛ㄧず鍖楃含銆�     * @param longitude 缁忓害銆傛湁鏁堣寖鍥达細-180.0鍒�180.0锛�琛ㄧず涓滅粡銆�     * @return the latest status
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
     * @since Weibo4J 1.2.0
     */
    public Status updateStatus(String status, long inReplyToStatusId
            , double latitude, double longitude) throws WeiboException {
    	return new Status(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status),
                        new PostParameter("lat", latitude),
                        new PostParameter("long", longitude),
                        new PostParameter("in_reply_to_status_id",
                                String.valueOf(inReplyToStatusId)),
                        new PostParameter("source", source)}, true));
    }
    
    /**
     * 鍙戣〃鍥剧墖寰崥娑堟伅銆傜洰鍓嶄笂浼犲浘鐗囧ぇ灏忛檺鍒朵负<5M銆�     * @param status 瑕佸彂甯冪殑寰崥娑堟伅鏂囨湰鍐呭
     * @param item 瑕佷笂浼犵殑鍥剧墖
     * @return the latest status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/upload">statuses/upload </a>
     */
    public Status uploadStatus(String status,ImageItem item) throws WeiboException {
    	if(!URLEncodeUtils.isURLEncoded(status))
    		status=URLEncodeUtils.encodeURL(status);
    	return new Status(http.multPartURL(getBaseURL() + "statuses/upload.json",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("source", source)},item, true));
    }
    /**
     * 鍙戣〃鍥剧墖寰崥娑堟伅銆傜洰鍓嶄笂浼犲浘鐗囧ぇ灏忛檺鍒朵负<5M銆�     * @param status 瑕佸彂甯冪殑寰崥娑堟伅鏂囨湰鍐呭
     * @param item 瑕佷笂浼犵殑鍥剧墖
     * @param latitude  绾害銆傛湁鏁堣寖鍥达細-90.0鍒�90.0锛�琛ㄧず鍖楃含銆�     * @param longitude 缁忓害銆傛湁鏁堣寖鍥达細-180.0鍒�180.0锛�琛ㄧず涓滅粡銆�     * @return
     * @throws WeiboException
     */
    public Status uploadStatus(String status,ImageItem item, double latitude, double longitude) throws WeiboException {
    	if(!URLEncodeUtils.isURLEncoded(status))
    		status=URLEncodeUtils.encodeURL(status);
    	return new Status(http.multPartURL(getBaseURL() + "statuses/upload.json",
		new PostParameter[]{new PostParameter("status", status), 
	        new PostParameter("source", source),
		new PostParameter("lat", latitude),
                new PostParameter("long", longitude),	
	},item, true));
    }

    /**
     * 鍙戣〃鍥剧墖寰崥娑堟伅銆傜洰鍓嶄笂浼犲浘鐗囧ぇ灏忛檺鍒朵负<5M銆�     * @param status 瑕佸彂甯冪殑寰崥娑堟伅鏂囨湰鍐呭
     * @param file 瑕佷笂浼犵殑鍥剧墖
     * @return
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/upload">statuses/upload </a>
     */
    public Status uploadStatus(String status,File file) throws WeiboException {
    	if(!URLEncodeUtils.isURLEncoded(status))
    		status=URLEncodeUtils.encodeURL(status);
    	return new Status(http.multPartURL("pic",getBaseURL() + "statuses/upload.json",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("source", source)},file, true));
    }
    /**
     * 鍙戣〃鍥剧墖寰崥娑堟伅銆傜洰鍓嶄笂浼犲浘鐗囧ぇ灏忛檺鍒朵负<5M銆�     * @param status 瑕佸彂甯冪殑寰崥娑堟伅鏂囨湰鍐呭
     * @param file 瑕佷笂浼犵殑鍥剧墖
     * @param latitude  绾害銆傛湁鏁堣寖鍥达細-90.0鍒�90.0锛�琛ㄧず鍖楃含銆�     * @param longitude 缁忓害銆傛湁鏁堣寖鍥达細-180.0鍒�180.0锛�琛ㄧず涓滅粡銆�     * @return
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/upload">statuses/upload </a>
     */
    public Status uploadStatus(String status,File file, double latitude, double longitude) throws WeiboException {
    	if(!URLEncodeUtils.isURLEncoded(status))
    		status=URLEncodeUtils.encodeURL(status);
    	return new Status(http.multPartURL("pic",getBaseURL() + "statuses/upload.json",
		new PostParameter[]{new PostParameter("status", status), 
	    new PostParameter("source", source),
	    new PostParameter("lat", latitude),
            new PostParameter("long", longitude)},
            file, true));
    }
    

    /**
     * Destroys the status specified by the required ID parameter.  The authenticating user must be the author of the specified status.
     * <br>This method calls http://api.t.sina.com.cn/statuses/destroy/id.format
     *
     * @param statusId The ID of the status to destroy.
     * @return the deleted status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/destroy">statuses/destroy </a>
     */
    public Status destroyStatus(long statusId) throws WeiboException {
    	return destroyStatus(Long.toString(statusId));
    }
    public Status destroyStatus(String statusId) throws WeiboException {
	return new Status(http.post(getBaseURL() + "statuses/destroy/" + statusId + ".json",
		new PostParameter[0], true));
    }
    
    /**
     * 杞彂寰崥
     * @param sid 瑕佽浆鍙戠殑寰崥ID
     * @param status 娣诲姞鐨勮浆鍙戞枃鏈�蹇呴』鍋歎RLEncode,淇℃伅鍐呭涓嶈秴杩�40涓眽瀛椼�濡備笉濉垯榛樿涓衡�杞彂寰崥鈥濄�
     * @return a single status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public Status repost(String sid,String status) throws WeiboException {
    	return repost(sid, status,0);
    }
    /**
     * 杞彂寰崥
     * @param sid 瑕佽浆鍙戠殑寰崥ID
     * @param status 娣诲姞鐨勮浆鍙戞枃鏈�蹇呴』鍋歎RLEncode,淇℃伅鍐呭涓嶈秴杩�40涓眽瀛椼�濡備笉濉垯榛樿涓衡�杞彂寰崥鈥濄�
     * @param isComment 鏄惁鍦ㄨ浆鍙戠殑鍚屾椂鍙戣〃璇勮銆�琛ㄧず鍙戣〃璇勮锛�琛ㄧず涓嶅彂琛�
     * @return a single status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public Status repost(String sid,String status,int isComment) throws WeiboException {
	return new Status(http.post(getBaseURL() + "statuses/repost.json",
		new PostParameter[]{new PostParameter("id", sid),
	    new PostParameter("status", status),
	    new PostParameter("is_comment", isComment)}, true));
    }
    
    /**
    *瀵逛竴鏉″井鍗氫俊鎭繘琛岃瘎璁�    * @param 璇勮鍐呭銆傚繀椤诲仛URLEncode,淇℃伅鍐呭涓嶈秴杩�40涓眽瀛椼�
    * @param id 瑕佽瘎璁虹殑寰崥娑堟伅ID
    * @param cid 瑕佸洖澶嶇殑璇勮ID,鍙互涓簄ull.濡傛灉id鍙奵id涓嶅瓨鍦紝灏嗚繑鍥�00閿欒
    * </br>濡傛灉鎻愪緵浜嗘纭殑cid鍙傛暟锛屽垯璇ユ帴鍙ｇ殑琛ㄧ幇涓哄洖澶嶆寚瀹氱殑璇勮銆�br/>姝ゆ椂id鍙傛暟灏嗚蹇界暐銆�br/>鍗充娇cid鍙傛暟浠ｈ〃鐨勮瘎璁轰笉灞炰簬id鍙傛暟浠ｈ〃鐨勫井鍗氭秷鎭紝閫氳繃璇ユ帴鍙ｅ彂琛ㄧ殑璇勮淇℃伅鐩存帴鍥炲cid浠ｈ〃鐨勮瘎璁恒�
    * @return the comment object
    * @throws WeiboException
    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comment">Statuses/comment</a>
    */
   public Comment updateComment(String comment, String id, String cid) throws WeiboException {
       PostParameter[] params = null;
       if (cid == null)
       	params = new PostParameter[] {
   			new PostParameter("comment", comment),
   			new PostParameter("id", id)
   		};
       else
       	params = new PostParameter[] {
   			new PostParameter("comment", comment),
   			new PostParameter("cid", cid),
   			new PostParameter("id", id)
   		};
       return new Comment(http.post(getBaseURL() + "statuses/comment.json", params, true));
   }
   /**
    * 鍒犻櫎璇勮銆傛敞鎰忥細鍙兘鍒犻櫎鐧诲綍鐢ㄦ埛鑷繁鍙戝竷鐨勮瘎璁猴紝涓嶅彲浠ュ垹闄ゅ叾浠栦汉鐨勮瘎璁恒�
    * @param statusId 娆插垹闄ょ殑璇勮ID
    * @return the deleted status
    * @throws WeiboException when Weibo service or network is unavailable
    * @since Weibo4J 1.2.0
    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comment_destroy">statuses/comment_destroy </a>
    */
	public Comment destroyComment(long commentId) throws WeiboException {
		return new Comment(http.delete(getBaseURL()
				+ "statuses/comment_destroy/" + commentId + ".json?source="
				+ CONSUMER_KEY, true));
	}

   /**
    * 鎵归噺鍒犻櫎璇勮銆傛敞鎰忥細鍙兘鍒犻櫎鐧诲綍鐢ㄦ埛鑷繁鍙戝竷鐨勮瘎璁猴紝涓嶅彲浠ュ垹闄ゅ叾浠栦汉鐨勮瘎璁恒�
    * @Ricky
    * @param ids 娆插垹闄ょ殑涓�粍璇勮ID锛岀敤鍗婅閫楀彿闅斿紑锛屾渶澶�0涓�    * @return
    * @throws WeiboException
    * @since Weibo4J 1.2.0
    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comment/destroy_batch">statuses/comment/destroy batch</a> 
    */
   public List<Comment> destroyComments(String ids)throws WeiboException{
   	return Comment.constructComments(http.post(getBaseURL()+"statuses/comment/destroy_batch.json",
   			new PostParameter[]{new PostParameter("ids",ids)},true));
   }
   public List<Comment> destroyComments(String[] ids)throws WeiboException{
   	StringBuilder sb = new StringBuilder();
	    for(String id : ids) {
		   sb.append(id).append(',');
	    }
	    sb.deleteCharAt(sb.length() - 1);
   	return Comment.constructComments(http.post(getBaseURL()+"statuses/comment/destroy_batch.json",
   			new PostParameter[]{new PostParameter("ids",sb.toString())},true));
   }

   /**
    * 鍥炲璇勮
    * @param sid 瑕佸洖澶嶇殑璇勮ID銆�    * @param cid 瑕佽瘎璁虹殑寰崥娑堟伅ID
    * @param comment 瑕佸洖澶嶇殑璇勮鍐呭銆傚繀椤诲仛URLEncode,淇℃伅鍐呭涓嶈秴杩�40涓眽瀛�    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/reply">Statuses/reply</a> 
    * @throws WeiboException when Weibo service or network is unavailable
    * @since Weibo4J 1.2.0
    */
	public Comment reply(String sid, String cid, String comment)
			throws WeiboException {
		return new Comment(http.post(getBaseURL() + "statuses/reply.json",
				new PostParameter[] { new PostParameter("id", sid),
						new PostParameter("cid", cid),
						new PostParameter("comment", comment) }, true));
	}
   
  //--------------auth method----------
    /**
     *
     * @param consumerKey OAuth consumer key
     * @param consumerSecret OAuth consumer secret
     * @since Weibo4J 1.2.0
     */
    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret){
        this.http.setOAuthConsumer(consumerKey, consumerSecret);
    }

    /**
     * Retrieves a request token
     * @return generated request token.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://oauth.net/core/1.0/#auth_step1">OAuth Core 1.0 - 6.1.  Obtaining an Unauthorized Request Token</a>
     */
    public RequestToken getOAuthRequestToken() throws WeiboException {
        return http.getOAuthRequestToken();
    }

    public RequestToken getOAuthRequestToken(String callback_url) throws WeiboException {
        return http.getOauthRequestToken(callback_url);
    }

    /**
     * Retrieves an access token assosiated with the supplied request token.
     * @param requestToken the request token
     * @return access token associsted with the supplied request token.
     * @throws WeiboException when Weibo service or network is unavailable, or the user has not authorized
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Oauth/access_token">Oauth/access token </a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Weibo4J 1.2.0
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws WeiboException {
        return http.getOAuthAccessToken(requestToken);
    }

    /**
     * Retrieves an access token assosiated with the supplied request token and sets userId.
     * @param requestToken the request token
     * @param pin pin
     * @return access token associsted with the supplied request token.
     * @throws WeiboException when Weibo service or network is unavailable, or the user has not authorized
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Oauth/access_token">Oauth/access token </a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since  Weibo4J 1.2.0
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String pin) throws WeiboException {
        AccessToken accessToken = http.getOAuthAccessToken(requestToken, pin);
        return accessToken;
    }

    /**
     * Retrieves an access token assosiated with the supplied request token and sets userId.
     * @param token request token
     * @param tokenSecret request token secret
     * @return access token associsted with the supplied request token.
     * @throws WeiboException when Weibo service or network is unavailable, or the user has not authorized
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Oauth/access_token">Oauth/access token </a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since  Weibo4J 1.2.0
     */
    public synchronized AccessToken getOAuthAccessToken(String token, String tokenSecret) throws WeiboException {
        AccessToken accessToken = http.getOAuthAccessToken(token, tokenSecret);
        return accessToken;
    }

    /**
     * Retrieves an access token assosiated with the supplied request token.
     * @param token request token
     * @param tokenSecret request token secret
     * @param oauth_verifier oauth_verifier or pin
     * @return access token associsted with the supplied request token.
     * @throws WeiboException when Weibo service or network is unavailable, or the user has not authorized
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Oauth/access_token">Oauth/access token </a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since  Weibo4J 1.2.0
     */
    public synchronized AccessToken getOAuthAccessToken(String token
            , String tokenSecret, String oauth_verifier) throws WeiboException {
        return http.getOAuthAccessToken(token, tokenSecret, oauth_verifier);
    }

    public synchronized AccessToken getXAuthAccessToken(String userId,String passWord,String mode) throws WeiboException {
    	return http.getXAuthAccessToken(userId, passWord, mode);
    }
    public synchronized AccessToken getXAuthAccessToken(String userid, String password) throws WeiboException {
	return getXAuthAccessToken(userid,password,Constants.X_AUTH_MODE);
	
    }
    /**
     * Sets the access token
     * @param accessToken accessToken
     * @since  Weibo4J 1.2.0
     */
    public void setOAuthAccessToken(AccessToken accessToken){
        this.http.setOAuthAccessToken(accessToken);
    }

    /**
     * Sets the access token
     * @param token token
     * @param tokenSecret token secret
     * @since  Weibo4J 1.2.0
     */
    public void setOAuthAccessToken(String token, String tokenSecret) {
        setOAuthAccessToken(new AccessToken(token, tokenSecret));
    }


 

    /* Status Methods */

 
    public RateLimitStatus getRateLimitStatus()throws
            WeiboException {
    	/*modify by sycheng edit with json */
        return new RateLimitStatus(get(getBaseURL() +
                "account/rate_limit_status.json", true),this);
    }

   

  

   
    /**
     * Returns the 20 most recent retweets posted by the authenticating user.
     *
     * @return the 20 most recent retweets posted by the authenticating user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<Status> getRetweetedByMe() throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_by_me.json",
                null, true));
        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_by_me.xml",
                null, true), this);*/
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user.
     * @param paging controls pagination
     * @return the 20 most recent retweets posted by the authenticating user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<Status> getRetweetedByMe(Paging paging) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_by_me.json",
                null, true));
        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_by_me.xml",
                null, paging, true), this);*/
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user's friends.
     * @return the 20 most recent retweets posted by the authenticating user's friends.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<Status> getRetweetedToMe() throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_to_me.json",
                null, true));
        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_to_me.xml",
                null, true), this);*/
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user's friends.
     * @param paging controls pagination
     * @return the 20 most recent retweets posted by the authenticating user's friends.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<Status> getRetweetedToMe(Paging paging) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_to_me.json",
                null, paging, true));
        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_to_me.xml",
                null, paging, true), this);*/
    }

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<Status> getRetweetsOfMe() throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweets_of_me.json",
                null, true));
        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweets_of_me.xml",
                null, true), this);*/
    }

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @param paging controls pagination
     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<Status> getRetweetsOfMe(Paging paging) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweets_of_me.json",
                null, paging, true));
    	/* return Status.constructStatuses(get(getBaseURL() + "statuses/retweets_of_me.xml",
                null, paging, true), this);*/
    }


    /**
     * Retweets a tweet. Requires the id parameter of the tweet you are retweeting. Returns the original tweet with retweet details embedded.
     * @param statusId The ID of the status to retweet.
     * @return the retweeted status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public Status retweetStatus(long statusId) throws WeiboException {
        /*return new Status(http.post(getBaseURL() + "statuses/retweet/" + statusId + ".xml",
                new PostParameter[0], true), this);*/
    	return new Status(http.post(getBaseURL() + "statuses/retweet/" + statusId + ".json",
                new PostParameter[0], true));
    }

    /**
     * Returns up to 100 of the first retweets of a given tweet.
     * @param statusId The numerical ID of the tweet you want the retweets of.
     * @return the retweets of a given tweet
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<RetweetDetails> getRetweets(long statusId) throws WeiboException {
       /* return RetweetDetails.createRetweetDetails(get(getBaseURL()
                + "statuses/retweets/" + statusId + ".xml", true), this);*/
    	 return RetweetDetails.createRetweetDetails(get(getBaseURL()
                 + "statuses/retweets/" + statusId + ".json", true));
    }

    /**
     * Settings privacy information
     * @param comment (message&realname&geo&badge) 
     * @return User
     * @throws WeiboException
     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Account/update_privacy">Account/update privacy</a>
     * @since Weibo4J 1.2.0
     * @deprecated
     */
    
    public User updatePrivacy(String comment) throws WeiboException{
        return new User(http.post(getBaseURL() + "account/update_privacy.json",
    	                new PostParameter[]{new PostParameter("comment", comment)}, true).asJSONObject());
    	     
    	    
    	     
    	    }
  
    	 




    /**
     * Returns a list of the users currently featured on the site with their current statuses inline.
     *
     * @return List of User
     * @throws WeiboException when Weibo service or network is unavailable
     */
    public List<User> getFeatured() throws WeiboException {
//        return User.constructUsers(get(getBaseURL() + "statuses/featured.xml", true), this);
        return User.constructUsers(get(getBaseURL() + "statuses/featured.json", true));
    }

  

   

 

  

  

    /**
     * Tests if a friendship exists between two users.
     *
     * @param userA The ID or screen_name of the first user to test friendship for.
     * @param userB The ID or screen_name of the second user to test friendship for.
     * @return if a friendship exists between two users.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/exists">friendships/exists </a>
     */
    public boolean existsFriendship(String userA, String userB) throws WeiboException {
        return -1 != get(getBaseURL() + "friendships/exists.json", "user_a", userA, "user_b", userB, true).
                asString().indexOf("true");
    }

    /**
     * Returns an array of numeric IDs for every user the authenticating user is following.
     * @return an array of numeric IDs for every user the authenticating user is following
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">friends/ids</a>
     * @deprecated use getFriendsIDS(String userid,String username,Paging paging) instead.
     */
    public IDs getFriendsIDs() throws WeiboException {
        return getFriendsIDs(-1l);
    }

    /**
     * Returns an array of numeric IDs for every user the authenticating user is following.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return an array of numeric IDs for every user the authenticating user is following
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">friends/ids</a>
     *  @deprecated use getFriendsIDS(String userid,String username,Paging paging) instead.
     */
    public IDs getFriendsIDs(long cursor) throws WeiboException {
        return new IDs(get(getBaseURL() + "friends/ids.xml?cursor=" + cursor, true));
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.<br>
     * all IDs are attempted to be returned, but large sets of IDs will likely fail with timeout errors.
     * @param userId Specfies the ID of the user for whom to return the friends list.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">friends/ids</a>
     *  @deprecated use getFriendsIDS(String userid,String username,Paging paging) instead.
     */
    public IDs getFriendsIDs(int userId) throws WeiboException {
        return getFriendsIDs(userId, -1l);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * @param userId Specifies the ID of the user for whom to return the friends list.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">friends/ids</a>
     *  @deprecated use getFriendsIDS(String userid,String username,Paging paging) instead.
     */
    public IDs getFriendsIDs(int userId, long cursor) throws WeiboException {
        /*return new IDs(get(getBaseURL() + "friends/ids.xml?user_id=" + userId +
                "&cursor=" + cursor, true));*/
    	return new IDs(get(getBaseURL() + "friends/ids.json?user_id=" + userId +
                "&cursor=" + cursor, true),this);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * @param screenName Specfies the screen name of the user for whom to return the friends list.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">friends/ids</a>
     *  @deprecated use getFriendsIDS(String userid,String username,Paging paging) instead.
     */
    public IDs getFriendsIDs(String screenName) throws WeiboException {
        return getFriendsIDs(screenName, -1l);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is following.
     * @param screenName Specfies the screen name of the user for whom to return the friends list.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return an array of numeric IDs for every user the specified user is following
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">friends/ids</a>
     *  @deprecated use getFriendsIDS(String userid,String username,Paging paging) instead.
     */
    public IDs getFriendsIDs(String screenName, long cursor) throws WeiboException {
       /* return new IDs(get(getBaseURL() + "friends/ids.xml?screen_name=" + screenName
                + "&cursor=" + cursor, true));*/
    	 return new IDs(get(getBaseURL() + "friends/ids.json?screen_name=" + screenName
                 + "&cursor=" + cursor, true),this);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">followers/ids </a>
     *  @deprecated use getFriendsIDS(String userid,String username,Paging paging) instead.
     */
    public IDs getFollowersIDs() throws WeiboException {
        return getFollowersIDs(-1l);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">followers/ids </a>
     *  @deprecated use getFriendsIDS(String userid,String username,Paging paging) instead.
     */
    public IDs getFollowersIDs(long cursor) throws WeiboException {
        /*return new IDs(get(getBaseURL() + "followers/ids.xml?cursor=" + cursor
                , true));*/
    	return new IDs(get(getBaseURL() + "followers/ids.json?cursor=" + cursor
                , true),this);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * @param userId Specfies the ID of the user for whom to return the followers list.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">followers/ids </a>
     *  @deprecated use getFriendsIDS(String userid,String username,Paging paging) instead.
     */
    public IDs getFollowersIDs(int userId) throws WeiboException {
        return getFollowersIDs(userId, -1l);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * @param userId Specifies the ID of the user for whom to return the followers list.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">followers/ids </a>
     *  @deprecated use getFriendsIDS(String userid,String username,Paging paging) instead.
     */
    public IDs getFollowersIDs(int userId, long cursor) throws WeiboException {
        return new IDs(get(getBaseURL() + "followers/ids.xml?user_id=" + userId
                + "&cursor=" + cursor, true));
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * @param screenName Specfies the screen name of the user for whom to return the followers list.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">followers/ids </a>
     *  @deprecated use getFriendsIDS(String userid,String username,Paging paging) instead.
     */
    public IDs getFollowersIDs(String screenName) throws WeiboException {
        return getFollowersIDs(screenName, -1l);
    }

    /**
     * Returns an array of numeric IDs for every user the specified user is followed by.
     * @param screenName Specfies the screen name of the user for whom to return the followers list.
     * @param cursor  Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
     * @return The ID or screen_name of the user to retrieve the friends ID list for.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">followers/ids </a>
     *  @deprecated use getFriendsIDS(String userid,String username,Paging paging) instead.
     */
    public IDs getFollowersIDs(String screenName, long cursor) throws WeiboException {
       /* return new IDs(get(getBaseURL() + "followers/ids.xml?screen_name="
                + screenName + "&cursor=" + cursor, true));*/
    	 return new IDs(get(getBaseURL() + "followers/ids.json?screen_name="
                 + screenName + "&cursor=" + cursor, true),this);
    }



    private void addParameterToList(List<PostParameter> colors,
                                      String paramName, String color) {
        if(null != color){
            colors.add(new PostParameter(paramName,color));
        }
    }

   

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * @param id String
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public User enableNotification(String id) throws WeiboException {
//        return new User(http.post(getBaseURL() + "notifications/follow/" + id + ".xml", true), this);
    	return new User(http.post(getBaseURL() + "notifications/follow/" + id + ".json", true).asJSONObject());
    }

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * @param id String
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public User disableNotification(String id) throws WeiboException {
//        return new User(http.post(getBaseURL() + "notifications/leave/" + id + ".xml", true), this);
    	return new User(http.post(getBaseURL() + "notifications/leave/" + id + ".json", true).asJSONObject());
    }

   

    /* Saved Searches Methods */
    /**
     * Returns the authenticated user's saved search queries.
     * @return Returns an array of numeric user ids the authenticating user is blocking.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<SavedSearch> getSavedSearches() throws WeiboException {
        return SavedSearch.constructSavedSearches(get(getBaseURL() + "saved_searches.json", true));
    }

    /**
     * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
     * @param id The id of the saved search to be retrieved.
     * @return the data for a saved search
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public SavedSearch showSavedSearch(int id) throws WeiboException {
        return new SavedSearch(get(getBaseURL() + "saved_searches/show/" + id
                + ".json", true));
    }

    /**
     * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
     * @return the data for a created saved search
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public SavedSearch createSavedSearch(String query) throws WeiboException {
        return new SavedSearch(http.post(getBaseURL() + "saved_searches/create.json"
                , new PostParameter[]{new PostParameter("query", query)}, true));
    }

    /**
     * Destroys a saved search for the authenticated user. The search specified by id must be owned by the authenticating user.
     * @param id The id of the saved search to be deleted.
     * @return the data for a destroyed saved search
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public SavedSearch destroySavedSearch(int id) throws WeiboException {
        return new SavedSearch(http.post(getBaseURL() + "saved_searches/destroy/" + id
                + ".json", true));
    }


	/**
	 * Obtain the ListObject feed list
	 * @param uid		User ID or screen_name
	 * @param listId	The ID or slug of ListObject
	 * @param auth		if true, the request will be sent with BASIC authentication header
	 * @return
	 * @throws WeiboException
	 */
	public List<Status> getListStatuses(String uid, String listId, boolean auth) throws WeiboException {
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseURL()).append(uid).append("/lists/").append(listId).append("/statuses.xml").append("?source=").append(CONSUMER_KEY);
		String httpMethod = "GET";
		String url = sb.toString();
		//
		return Status.constructStatuses(http.httpRequest(url, null, auth, httpMethod), this);
	}


	/**
	 * Obtain ListObject member list
	 * @param uid		User ID or screen_name
	 * @param listId	The ID or slug of ListObject
	 * @param auth		if true, the request will be sent with BASIC authentication header
	 * @return
	 * @throws WeiboException
	 */
	public UserWapper getListMembers(String uid, String listId, boolean auth) throws WeiboException {
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseURL()).append(uid).append("/").append(listId).append("/members.xml").append("?source=").append(CONSUMER_KEY);
		String httpMethod = "GET";
		String url = sb.toString();
		//
		return User.constructWapperUsers(http.httpRequest(url, null, auth, httpMethod), this);
	}

	/**
	 * Obtain ListObject subscribe user's list 
	 * @param uid		User ID or screen_name
	 * @param listId	The ID or slug of ListObject
	 * @param auth		if true, the request will be sent with BASIC authentication header
	 * @return
	 * @throws WeiboException
	 */
	public UserWapper getListSubscribers(String uid, String listId, boolean auth) throws WeiboException {
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseURL()).append(uid).append("/").append(listId).append("/subscribers.xml").append("?source=").append(CONSUMER_KEY);
		String httpMethod = "GET";
		String url = sb.toString();
		//
		return User.constructWapperUsers(http.httpRequest(url, null, auth, httpMethod), this);
	}



	/**
	 * confirm list member
	 * @param uid		User ID or screen_name
	 * @param listId	The ID or slug of ListObject
	 * @param targetUid	Target user ID or screen_name
	 * @param auth		if true, the request will be sent with BASIC authentication header
	 * @return
	 * @throws WeiboException
	 */
	public boolean isListMember(String uid, String listId, String targetUid, boolean auth)
			throws WeiboException {
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseURL()).append(uid).append("/").append(listId).append("/members/").append(targetUid)
				.append(".xml").append("?source=").append(CONSUMER_KEY);
		String url = sb.toString();
		//
		String httpMethod = "GET";
		//
		Document doc = http.httpRequest(url, null, auth, httpMethod).asDocument();
		Element root = doc.getDocumentElement();
		return "true".equals(root.getNodeValue());
	}

	/**
	 * confirm subscription list
	 * @param uid		User ID or screen_name
	 * @param listId	The ID or slug of ListObject
	 * @param targetUid	Target user ID or screen_name
	 * @param auth		if true, the request will be sent with BASIC authentication header
	 * @return
	 * @throws WeiboException
	 */
	public boolean isListSubscriber(String uid, String listId, String targetUid, boolean auth)
			throws WeiboException {
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseURL()).append(uid).append("/").append(listId).append("/subscribers/").append(targetUid)
				.append(".xml").append("?source=").append(CONSUMER_KEY);
		String url = sb.toString();
		//
		String httpMethod = "GET";
		//
		Document doc = http.httpRequest(url, null, auth, httpMethod).asDocument();
		Element root = doc.getDocumentElement();
		return "true".equals(root.getNodeValue());
	}

    /* Help Methods */
    /**
     * Returns the string "ok" in the requested format with a 200 OK HTTP status code.
     * @return true if the API is working
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public boolean test() throws WeiboException {
        return -1 != get(getBaseURL() + "help/test.json", false).
                asString().indexOf("ok");
    }

    private SimpleDateFormat format = new SimpleDateFormat(
            "EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weibo weibo = (Weibo) o;

        if (!baseURL.equals(weibo.baseURL)) return false;
        if (!format.equals(weibo.format)) return false;
        if (!http.equals(weibo.http)) return false;
        if (!searchBaseURL.equals(weibo.searchBaseURL)) return false;
        if (!source.equals(weibo.source)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = http.hashCode();
        result = 31 * result + baseURL.hashCode();
        result = 31 * result + searchBaseURL.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + format.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Weibo{" +
                "http=" + http +
                ", baseURL='" + baseURL + '\'' +
                ", searchBaseURL='" + searchBaseURL + '\'' +
                ", source='" + source + '\'' +
                ", format=" + format +
                '}';
    }

 
  

   

    /**
     * Return your relationship with the details of a user
     * @param target_id id of the befriended user
     * @return jsonObject
     * @throws WeiboException when Weibo service or network is unavailable
     */
    public JSONObject showFriendships(String target_id) throws WeiboException {
    	return get(getBaseURL() + "friendships/show.json?target_id="+target_id, true).asJSONObject();
    }

    /**
     * Return the details of the relationship between two users
     * @param target_id id of the befriended user
     * @return jsonObject
     * @throws WeiboException when Weibo service or network is unavailable
     * @Ricky  Add source parameter&missing "="
     */
    public JSONObject showFriendships(String source_id,String target_id) throws WeiboException {
    	return get(getBaseURL() + "friendships/show.json?target_id="+target_id+"&source_id="+source_id+"&source="+CONSUMER_KEY, true).asJSONObject();
    }

  

    /**
     *  Return infomation of current user
     * @param ip a specified ip(Only open to invited partners)
     * @param args User Information args[2]:nickname,args[3]:gender,args[4],password,args[5]:email
     * @return jsonObject
     * @throws WeiboException when Weibo service or network is unavailable
     */
    public JSONObject register(String ip,String ...args) throws WeiboException {
    	return http.post(getBaseURL() + "account/register.json",
                new PostParameter[]{new PostParameter("nick", args[2]),
									new PostParameter("gender", args[3]),
									new PostParameter("password", args[4]),
									new PostParameter("email", args[5]),
									new PostParameter("ip", ip)}, true).asJSONObject();
    }
   
  


  //--------------base method----------
    public Weibo() {
        super();
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        http.setRequestTokenURL(Configuration.getScheme() + "api.t.sina.com.cn/oauth/request_token");
        http.setAuthorizationURL(Configuration.getScheme() + "api.t.sina.com.cn/oauth/authorize");
        http.setAccessTokenURL(Configuration.getScheme() + "api.t.sina.com.cn/oauth/access_token");
    }

    /**
     * Sets token information
     * @param token
     * @param tokenSecret
     */
    public void setToken(String token, String tokenSecret) {
        http.setToken(token, tokenSecret);
    }

    public Weibo(String baseURL) {
        this();
        this.baseURL = baseURL;
    }

    /**
     * Sets the base URL
     *
     * @param baseURL String the base URL
     */
    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * Returns the base URL
     *
     * @return the base URL
     */
    public String getBaseURL() {
        return this.baseURL;
    }

    /**
     * Sets the search base URL
     *
     * @param searchBaseURL the search base URL
     * @since Weibo4J 1.2.0
     */
    public void setSearchBaseURL(String searchBaseURL) {
        this.searchBaseURL = searchBaseURL;
    }

    /**
     * Returns the search base url
     * @return search base url
     * @since Weibo4J 1.2.0
     */
    public String getSearchBaseURL(){
        return this.searchBaseURL;
    }
    
    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws WeiboException when Weibo service or network is unavailable
     */

    private Response get(String url, boolean authenticate) throws WeiboException {
        return get(url, null, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @param name1        the name of the first parameter
     * @param value1       the value of the first parameter
     * @return the response
     * @throws WeiboException when Weibo service or network is unavailable
     */

    protected Response get(String url, String name1, String value1, boolean authenticate) throws WeiboException {
        return get(url, new PostParameter[]{new PostParameter(name1, value1)}, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param name1        the name of the first parameter
     * @param value1       the value of the first parameter
     * @param name2        the name of the second parameter
     * @param value2       the value of the second parameter
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws WeiboException when Weibo service or network is unavailable
     */

    protected Response get(String url, String name1, String value1, String name2, String value2, boolean authenticate) throws WeiboException {
        return get(url, new PostParameter[]{new PostParameter(name1, value1), new PostParameter(name2, value2)}, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws WeiboException when Weibo service or network is unavailable
     */
    protected Response get(String url, PostParameter[] params, boolean authenticate) throws WeiboException {
		if (url.indexOf("?") == -1) {
			url += "?source=" + CONSUMER_KEY;
		} else if (url.indexOf("source") == -1) {
			url += "&source=" + CONSUMER_KEY;
		}
    	if (null != params && params.length > 0) {
			url += "&" + HttpClient.encodeParameters(params);
		}
        return http.get(url, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param paging controls pagination
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws WeiboException when Weibo service or network is unavailable
     */
    protected Response get(String url, PostParameter[] params, Paging paging, boolean authenticate) throws WeiboException {
        if (null != paging) {
            List<PostParameter> pagingParams = new ArrayList<PostParameter>(4);
            if (-1 != paging.getMaxId()) {
                pagingParams.add(new PostParameter("max_id", String.valueOf(paging.getMaxId())));
            }
            if (-1 != paging.getSinceId()) {
                pagingParams.add(new PostParameter("since_id", String.valueOf(paging.getSinceId())));
            }
            if (-1 != paging.getPage()) {
                pagingParams.add(new PostParameter("page", String.valueOf(paging.getPage())));
            }
            if (-1 != paging.getCount()) {
                if (-1 != url.indexOf("search")) {
                    // search api takes "rpp"
                    pagingParams.add(new PostParameter("rpp", String.valueOf(paging.getCount())));
                } else {
                    pagingParams.add(new PostParameter("count", String.valueOf(paging.getCount())));
                }
            }
            PostParameter[] newparams = null;
            PostParameter[] arrayPagingParams = pagingParams.toArray(new PostParameter[pagingParams.size()]);
            if (null != params) {
                newparams = new PostParameter[params.length + pagingParams.size()];
                System.arraycopy(params, 0, newparams, 0, params.length);
                System.arraycopy(arrayPagingParams, 0, newparams, params.length, pagingParams.size());
            } else {
                if (0 != arrayPagingParams.length) {
                    String encodedParams = HttpClient.encodeParameters(arrayPagingParams);
                    if (-1 != url.indexOf("?")) {
                        url += "&source=" + CONSUMER_KEY +
                        		"&" + encodedParams;
                    } else {
                        url += "?source=" + CONSUMER_KEY +
                        		"&" + encodedParams;
                    }
                }
            }
            return get(url, newparams, authenticate);
        } else {
            return get(url, params, authenticate);
        }
    }

	private PostParameter[] generateParameterArray(Map<String, String> parames)
			throws WeiboException {
		PostParameter[] array = new PostParameter[parames.size()];
		int i = 0;
		for (String key : parames.keySet()) {
			if (parames.get(key) != null) {
				array[i] = new PostParameter(key, parames.get(key));
				i++;
			}
		}
		return array;
}
	  public final static Device IM = new Device("im");
	    public final static Device SMS = new Device("sms");
	    public final static Device NONE = new Device("none");

	    static class Device {
	        final String DEVICE;

	        public Device(String device) {
	            DEVICE = device;
	        }
	    }
	//---------------@deprecated---------------------------------------

	/**
	 * Returns the user's friends, each with current status inline.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends.format
	 *
	 * @param id the ID or screen name of the user for whom to request a list of friends
	 * @param paging controls pagination
	 * @return the list of friends
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
	 * @deprecated use getFriendsStatuses(id,paging) instead
	 */
	public List<User> getFriends(String id, Paging paging) throws WeiboException {
	    return getFriendsStatuses(id, paging);
	}


	/**
	 * Returns the user's friends, each with current status inline.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends.format
	 *
	 * @param id   the ID or screen name of the user for whom to request a list of friends
	 * @param page the number of page
	 * @return List
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use getFriendsStatuses(String id, Paging paging) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
	 */
	public List<User> getFriends(String id, int page) throws WeiboException {
	    return getFriendsStatuses(id, new Paging(page));
	}


	/**
	 * Returns the specified user's friends, each with current status inline.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends.format
	 *
	 * @param page number of page
	 * @return the list of friends
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use getFriendsStatuses(Paging paging) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
	 */
	public List<User> getFriends(int page) throws WeiboException {
	    return getFriendsStatuses(new Paging(page));
	}


	/**
	 * Returns the specified user's friends, each with current status inline.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends.format
	 *
	 * @return the list of friends
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
	 * @deprecated use getFriendsStatues() instead
	 */
	public List<User> getFriends() throws WeiboException {
	    return getFriendsStatuses();
	}


	/**
	 * Returns the specified user's friends, each with current status inline.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends.format
	 *
	 * @param paging controls pagination
	 * @return the list of friends
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
	 * @deprecated Use getFriendsStatuses(Paging paging) instead
	 */
	public List<User> getFriends(Paging paging) throws WeiboException {
	    return getFriendsStatuses(paging);
	}


	/**
	 * Returns the user's friends, each with current status inline.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends.format
	 *
	 * @param id the ID or screen name of the user for whom to request a list of friends
	 * @return the list of friends
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
	 * @deprecated use getFriendsStatuses(id) instead
	 */
	public List<User> getFriends(String id) throws WeiboException {
	    return getFriendsStatuses(id);
	}


	/**
	 * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Weibo (this is going to be changed).
	 * <br>This method calls http://api.t.sina.com.cn/statuses/followers.format
	 *
	 * @param id   The ID or screen name of the user for whom to request a list of followers.
	 * @param paging controls pagination
	 * @return List
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
	 * @deprecated use getFollowersStatuses(id) instead
	 */
	public List<User> getFollowers(String id, Paging paging) throws WeiboException {
	    return getFollowersStatuses(id, paging);
	}


	/**
	 * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Weibo (this is going to be changed).
	 * <br>This method calls http://api.t.sina.com.cn/statuses/followers.format
	 *
	 * @param id   The ID or screen name of the user for whom to request a list of followers.
	 * @param page Retrieves the next 100 followers.
	 * @return List
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @deprecated Use getFollowersStatuses(String id, Paging paging) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
	 */
	public List<User> getFollowers(String id, int page) throws WeiboException {
	    return getFollowersStatuses(id, new Paging(page));
	}


	/**
	 * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Weibo (this is going to be changed).
	 * <br>This method calls http://api.t.sina.com.cn/statuses/followers.format
	 *
	 * @return List
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
	 * @deprecated use getFollowersStatuses() instead
	 */
	public List<User> getFollowers() throws WeiboException {
	    return getFollowersStatuses();
	}


	/**
	 * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Weibo (this is going to be changed).
	 * <br>This method calls http://api.t.sina.com.cn/statuses/followers.format
	 *
	 * @param paging controls pagination
	 * @return List
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
	 * @deprecated use getFollowersStatuses(paging)
	 */
	public List<User> getFollowers(Paging paging) throws WeiboException {
	    return getFollowersStatuses(paging);
	}


	/**
	 * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Weibo (this is going to be changed).
	 * <br>This method calls http://api.t.sina.com.cn/statuses/followers.format
	 *
	 * @param page Retrieves the next 100 followers.
	 * @return List
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @deprecated Use getFollowersStatuses(Paging paging) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
	 */
	public List<User> getFollowers(int page) throws WeiboException {
	    return getFollowersStatuses(new Paging(page));
	}


	/**
	 * Returns the authenticating user's followers, each with current status inline. They are ordered by the order in which they joined Weibo (this is going to be changed).
	 * <br>This method calls http://api.t.sina.com.cn/statuses/followers.format
	 *
	 * @param id The ID or screen name of the user for whom to request a list of followers.
	 * @return List
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
	 * @deprecated use getFollowersStatuses(id) instead
	 */
	public List<User> getFollowers(String id) throws WeiboException {
	    return getFollowersStatuses(id);
	}


	/**
	 * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/public_timeline.format
	 *
	 * @param sinceID returns only public statuses with an ID greater than (that is, more recent than) the specified ID
	 * @return the 20 most recent statuses
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/public_timeline">statuses/public_timeline </a>
	 * @deprecated use getPublicTimeline(long sinceID) instead
	 */
	public List<Status> getPublicTimeline(int sinceID) throws
	        WeiboException {
	    return getPublicTimeline((long)sinceID);
	}


	/**
	 * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/public_timeline.format
	 *
	 * @param sinceID returns only public statuses with an ID greater than (that is, more recent than) the specified ID
	 * @return the 20 most recent statuses
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/public_timeline">statuses/public_timeline </a>
	 * @deprecated user getPublicTimeline(int count,int baseApp) instead
	 */
	public List<Status> getPublicTimeline(long sinceID) throws
	        WeiboException {
		return Status.constructStatuses(get(getBaseURL() +
	            "statuses/public_timeline.json", null, new Paging((long) sinceID)
	            , false));
	}


	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
	 *
	 * @param page the number of page
	 * @return list of the Friends Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use getFriendsTimeline(Paging paging) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
	 */
	public List<Status> getFriendsTimelineByPage(int page) throws
	        WeiboException {
	    return getFriendsTimeline(new Paging(page));
	}


	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
	 *
	 * @param page the number of page
	 * @return list of the Friends Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since  Weibo4J 1.2.0
	 * @deprecated Use getFriendsTimeline(Paging paging) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
	 */
	public List<Status> getFriendsTimeline(int page) throws
	        WeiboException {
	    return getFriendsTimeline(new Paging(page));
	}


	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
	 *
	 * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
	 * @param page    the number of page
	 * @return list of the Friends Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since  Weibo4J 1.2.0
	 * @deprecated Use getFriendsTimeline(Paging paging) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
	 */
	public List<Status> getFriendsTimeline(long sinceId, int page) throws
	        WeiboException {
	    return getFriendsTimeline(new Paging(page).sinceId(sinceId));
	}


	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
	 *
	 * @param id specifies the ID or screen name of the user for whom to return the friends_timeline
	 * @return list of the Friends Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
	 * @deprecated The Weibo API does not support this method anymore.
	 */
	public List<Status> getFriendsTimeline(String id) throws
	        WeiboException {
	    throw new IllegalStateException("The Weibo API is not supporting this method anymore");
	}


	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
	 *
	 * @param id   specifies the ID or screen name of the user for whom to return the friends_timeline
	 * @param page the number of page
	 * @return list of the Friends Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
	 * @deprecated The Weibo API does not support this method anymore.
	 */
	public List<Status> getFriendsTimelineByPage(String id, int page) throws
	        WeiboException {
	    throw new IllegalStateException("The Weibo API is not supporting this method anymore");
	}


	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
	 *
	 * @param id   specifies the ID or screen name of the user for whom to return the friends_timeline
	 * @param page the number of page
	 * @return list of the Friends Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since  Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
	 * @deprecated The Weibo API does not support this method anymore.
	 */
	public List<Status> getFriendsTimeline(String id, int page) throws
	        WeiboException {
	    throw new IllegalStateException("The Weibo API is not supporting this method anymore");
	}


	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
	 *
	 * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
	 * @param id   specifies the ID or screen name of the user for whom to return the friends_timeline
	 * @param page the number of page
	 * @return list of the Friends Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since  Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
	 * @deprecated The Weibo API does not support this method anymore.
	 */
	public List<Status> getFriendsTimeline(long sinceId, String id, int page) throws
	        WeiboException {
	    throw new IllegalStateException("The Weibo API is not supporting this method anymore");
	}


	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
	 *
	 * @param id   specifies the ID or screen name of the user for whom to return the friends_timeline
	 * @param paging controls pagination
	 * @return list of the Friends Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since  Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
	 * @deprecated The Weibo API does not support this method anymore.
	 */
	public List<Status> getFriendsTimeline(String id, Paging paging) throws
	        WeiboException {
	    throw new IllegalStateException("The Weibo API is not supporting this method anymore");
	}


	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
	 *
	 * @param since narrows the returned results to just those statuses created after the specified HTTP-formatted date
	 * @return list of the Friends Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use getFriendsTimeline(Paging paging) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
	 */
	public List<Status> getFriendsTimeline(Date since) throws
	        WeiboException {
	    return Status.constructStatuses(get(getBaseURL() + "statuses/friends_timeline.xml",
	            "since", format.format(since), true), this);
	}


	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
	 *
	 * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
	 * @return list of the Friends Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since  Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
	 * @deprecated Use getFriendsTimeline(Paging paging) instead
	 */
	public List<Status> getFriendsTimeline(long sinceId) throws
	        WeiboException {
	    return Status.constructStatuses(get(getBaseURL() + "statuses/friends_timeline.xml",
	            "since_id", String.valueOf(sinceId), true), this);
	}


	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
	 *
	 * @param id    specifies the ID or screen name of the user for whom to return the friends_timeline
	 * @param since narrows the returned results to just those statuses created after the specified HTTP-formatted date
	 * @return list of the Friends Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
	 * @deprecated The Weibo API does not support this method anymore.
	 */
	public List<Status> getFriendsTimeline(String id,Date since) throws WeiboException {
	    throw new IllegalStateException("The Weibo API is not supporting this method anymore");
	}


	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
	 *
	 * @param id    specifies the ID or screen name of the user for whom to return the friends_timeline
	 * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
	 * @return list of the Friends Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since  Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
	 * @deprecated The Weibo API does not support this method anymore.
	 */
	public List<Status> getFriendsTimeline(String id, long sinceId) throws WeiboException {
	    throw new IllegalStateException("The Weibo API is not supporting this method anymore");
	}


	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/user_timeline.format
	 *
	 * @param id    specifies the ID or screen name of the user for whom to return the user_timeline
	 * @param count specifies the number of statuses to retrieve.  May not be greater than 200 for performance purposes
	 * @param since narrows the returned results to just those statuses created after the specified HTTP-formatted date
	 * @return list of the user Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
	 * @deprecated using long sinceId is suggested.
	 */
	public List<Status> getUserTimeline(String id, int count
	        , Date since) throws WeiboException {
	    return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline/" + id + ".xml",
	            "since", format.format(since), "count", String.valueOf(count), http.isAuthenticationEnabled()), this);
	}


	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/user_timeline.format
	 *
	 * @param id    specifies the ID or screen name of the user for whom to return the user_timeline
	 * @param count specifies the number of statuses to retrieve.  May not be greater than 200 for performance purposes
	 * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
	 * @return list of the user Timeline
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since  Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
	 * @deprecated Use getUserTimeline(String id, Paging paging) instead
	 */
	public List<Status> getUserTimeline(String id, int count,long sinceId) throws WeiboException {
	    return getUserTimeline(id, new Paging(sinceId).count(count));
	}


	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/user_timeline.format
	 *
	 * @param id    specifies the ID or screen name of the user for whom to return the user_timeline
	 * @param since narrows the returned results to just those statuses created after the specified HTTP-formatted date
	 * @return the 20 most recent statuses posted in the last 24 hours from the user
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
	 * @deprecated Use getUserTimeline(String id, Paging paging) instead
	 */
	public List<Status> getUserTimeline(String id, Date since) throws WeiboException {
	    return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline/" + id + ".xml",
	            "since", format.format(since), http.isAuthenticationEnabled()), this);
	}


	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/user_timeline.format
	 *
	 * @param id    specifies the ID or screen name of the user for whom to return the user_timeline
	 * @param count specifies the number of statuses to retrieve.  May not be greater than 200 for performance purposes
	 * @return the 20 most recent statuses posted in the last 24 hours from the user
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
	 * @deprecated Use getUserTimeline(String id, Paging paging) instead
	 */
	public List<Status> getUserTimeline(String id, int count) throws
	        WeiboException {
	    return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline/" + id + ".xml",
	            "count", String.valueOf(count), http.isAuthenticationEnabled()), this);
	}


	/**
	 * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/user_timeline.format
	 *
	 * @param count specifies the number of statuses to retrieve.  May not be greater than 200 for performance purposes
	 * @param since narrows the returned results to just those statuses created after the specified HTTP-formatted date
	 * @return the 20 most recent statuses posted in the last 24 hours from the user
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
	 * @deprecated using long sinceId is suggested.
	 */
	public List<Status> getUserTimeline(int count, Date since) throws WeiboException {
	    return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.xml",
	            "since", format.format(since), "count", String.valueOf(count), true), this);
	}


	/**
	 * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/user_timeline.format
	 *
	 * @param count specifies the number of statuses to retrieve.  May not be greater than 200 for performance purposes
	 * @param sinceId returns only statuses with an ID greater than (that is, more recent than) the specified ID.
	 * @return the 20 most recent statuses posted in the last 24 hours from the user
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
	 * @since Weibo4J 1.2.0
	 * @deprecated Use getUserTimeline(String id, Paging paging) instead
	 */
	public List<Status> getUserTimeline(int count, long sinceId) throws WeiboException {
	    return getUserTimeline(new Paging(sinceId).count(count));
	}


	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified userid.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/user_timeline.format
	 *
	 * @param id specifies the ID or screen name of the user for whom to return the user_timeline
	 * @param sinceId returns only statuses with an ID greater than (that is, more recent than) the specified ID.
	 * @return the 20 most recent statuses posted in the last 24 hours from the user
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
	 * @since Weibo4J 1.2.0
	 * @deprecated Use getUserTimeline(String id, Paging paging) instead
	 */
	public List<Status> getUserTimeline(String id, long sinceId) throws WeiboException {
	    return getUserTimeline(id, new Paging(sinceId));
	}


	/**
	 * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/user_timeline.format
	 *
	 * @param sinceId returns only statuses with an ID greater than (that is, more recent than) the specified ID.
	 * @return the 20 most recent statuses posted in the last 24 hours from the user
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
	 * @since Weibo4J 1.2.0
	 * @deprecated Use getUserTimeline(Paging paging) instead
	 */
	public List<Status> getUserTimeline(long sinceId) throws
	        WeiboException {
	    return getUserTimeline(new Paging(sinceId));
	}


	/**
	 * 鍙戝竷涓�潯寰崥淇℃伅
	 * @param status 瑕佸彂甯冪殑寰崥娑堟伅鏂囨湰鍐呭
	 * @return the latest status
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
	 * @deprecated Use updateStatus(String status) instead
	 */
	public Status update(String status) throws WeiboException {
	    return updateStatus(status);
	}


	/**
	 *鍙戝竷涓�潯寰崥淇℃伅
	 * @param status            瑕佸彂甯冪殑寰崥娑堟伅鏂囨湰鍐呭
	 * @param inReplyToStatusId 瑕佽浆鍙戠殑寰崥娑堟伅ID
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
	 * @deprecated Use updateStatus(String status, long inReplyToStatusId) instead
	 */
	public Status update(String status, long inReplyToStatusId) throws WeiboException {
	    return updateStatus(status, inReplyToStatusId);
	}


	/**
	 * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/reply.format
	 *
	 * @return the 20 most recent replies
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use getMentions() instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/reply">statuses/reply </a>
	 */
	public List<Status> getReplies() throws WeiboException {
	    return Status.constructStatuses(get(getBaseURL() + "statuses/replies.xml", true), this);
	}


	/**
	 * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/reply.format
	 *
	 * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
	 * @return the 20 most recent replies
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @deprecated Use getMentions(Paging paging) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/reply">statuses/reply </a>
	 */
	public List<Status> getReplies(long sinceId) throws WeiboException {
	    return Status.constructStatuses(get(getBaseURL() + "statuses/replies.xml",
	            "since_id", String.valueOf(sinceId), true), this);
	}


	/**
	 * Returns the most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/reply.format
	 *
	 * @param page the number of page
	 * @return the 20 most recent replies
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use getMentions(Paging paging) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/reply">statuses/reply </a>
	 */
	public List<Status> getRepliesByPage(int page) throws WeiboException {
	    if (page < 1) {
	        throw new IllegalArgumentException("page should be positive integer. passed:" + page);
	    }
	    return Status.constructStatuses(get(getBaseURL() + "statuses/replies.xml",
	            "page", String.valueOf(page), true), this);
	}


	/**
	 * Returns the most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/reply.format
	 *
	 * @param page the number of page
	 * @return the 20 most recent replies
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @deprecated Use getMentions(Paging paging) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/reply">statuses/reply </a>
	 */
	public List<Status> getReplies(int page) throws WeiboException {
	    if (page < 1) {
	        throw new IllegalArgumentException("page should be positive integer. passed:" + page);
	    }
	    return Status.constructStatuses(get(getBaseURL() + "statuses/replies.xml",
	            "page", String.valueOf(page), true), this);
	}


	/**
	 * Returns the most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/reply.format
	 *
	 * @param sinceId Returns only statuses with an ID greater than (that is, more recent than) the specified ID
	 * @param page the number of page
	 * @return the 20 most recent replies
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @deprecated Use getMentions(Paging paging) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/reply">statuses/reply </a>
	 */
	public List<Status> getReplies(long sinceId, int page) throws WeiboException {
	    if (page < 1) {
	        throw new IllegalArgumentException("page should be positive integer. passed:" + page);
	    }
	    return Status.constructStatuses(get(getBaseURL() + "statuses/replies.xml",
	            "since_id", String.valueOf(sinceId),
	            "page", String.valueOf(page), true), this);
	}


	/**
	 * Returns a single status, specified by the id parameter. The status's author will be returned inline.
	 * @param id the numerical ID of the status you're trying to retrieve
	 * @return a single status
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use showStatus(long id) instead.
	 */
	public Status show(int id) throws WeiboException {
	    return showStatus((long)id);
	}


	/**
	 * Returns a single status, specified by the id parameter. The status's author will be returned inline.
	 * <br>This method calls http://api.t.sina.com.cn/statuses/show/id.format
	 *
	 * @param id the numerical ID of the status you're trying to retrieve
	 * @return a single status
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/show">statuses/show </a>
	 * @deprecated Use showStatus(long id) instead.
	 */
	
	public Status show(long id) throws WeiboException {
	    return new Status(get(getBaseURL() + "statuses/show/" + id + ".xml", false), this);
	}


	/**
	 * Returns extended information of a given user, specified by ID or screen name as per the required id parameter below.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
	 * <br>This method calls http://api.t.sina.com.cn/users/show.format
	 * @param id the ID or screen name of the user for whom to request the detail
	 * @return User
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Users/show">users/show </a>
	 * @deprecated use showUser(id) instead
	 */
	public User getUserDetail(String id) throws WeiboException {
	    return showUser(id);
	}


	
	/**
	 * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
	 *
	 * @param id the ID or screen name of the user to be befriended
	 * @return the befriended user
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use createFriendship(String id) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/create">friendships/create </a>
	 */
	
	public User create(String id) throws WeiboException {
	    return createFriendship(id);
	}


	/**
	 * Discontinues friendship with the user specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
	 *
	 * @param id the ID or screen name of the user for whom to request a list of friends
	 * @return User
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use destroyFriendship(String id) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/destroy">friendships/destroy </a>
	 */
	public User destroy(String id) throws WeiboException {
	    return destroyFriendship(id);
	}


	/**
	 * Tests if a friendship exists between two users.
	 *
	 * @param userA The ID or screen_name of the first user to test friendship for.
	 * @param userB The ID or screen_name of the second user to test friendship for.
	 * @return if a friendship exists between two users.
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use friendshipExists(String userA, String userB)
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/exists">friendships/exists </a>
	 */
	public boolean exists(String userA, String userB) throws WeiboException {
	    return existsFriendship(userA, userB);
	}


	/**
	 * Returns an array of numeric IDs for every user the authenticating user is following.
	 * @param paging Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @return an array of numeric IDs for every user the authenticating user is following
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">friends/ids</a>
	 * @deprecated use getFriendsIDs(long cursor) instead
	 */
	public IDs getFriendsIDs(Paging paging) throws WeiboException {
	    return new IDs(get(getBaseURL() + "friends/ids.xml", null, paging, true));
	}


	/**
	 * Returns an array of numeric IDs for every user the specified user is following.
	 * @param userId Specifies the ID of the user for whom to return the friends list.
	 * @param paging Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @return an array of numeric IDs for every user the specified user is following
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">friends/ids</a>
	 * @deprecated use getFriendsIDs(int userId, long cursor) instead
	 */
	public IDs getFriendsIDs(int userId, Paging paging) throws WeiboException {
	    return new IDs(get(getBaseURL() + "friends/ids.xml?user_id=" + userId, null
	            , paging, true));
	}


	/**
	 * Returns an array of numeric IDs for every user the specified user is following.
	 * @param screenName Specfies the screen name of the user for whom to return the friends list.
	 * @param paging Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @return an array of numeric IDs for every user the specified user is following
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">friends/ids</a>
	 * @deprecated use getFriendsIDs(String screenName, long cursor) instead
	 */
	public IDs getFriendsIDs(String screenName, Paging paging) throws WeiboException {
	    return new IDs(get(getBaseURL() + "friends/ids.xml?screen_name=" + screenName
	            , null, paging, true));
	}


	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * @return The ID or screen_name of the user to retrieve the friends ID list for.
	 * @param paging Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">followers/ids </a>
	 * @deprecated use getFollowersIDs(long cursor) instead
	 */
	public IDs getFollowersIDs(Paging paging) throws WeiboException {
	    return new IDs(get(getBaseURL() + "followers/ids.xml", null, paging
	            , true));
	}


	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * @param userId Specfies the ID of the user for whom to return the followers list.
	 * @param paging Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @return The ID or screen_name of the user to retrieve the friends ID list for.
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">followers/ids </a>
	 * @deprecated use getFollowersIDs(int userId, long cursor) instead
	 */
	public IDs getFollowersIDs(int userId, Paging paging) throws WeiboException {
	    return new IDs(get(getBaseURL() + "followers/ids.xml?user_id=" + userId, null
	            , paging, true));
	}


	/**
	 * Returns an array of numeric IDs for every user the specified user is followed by.
	 * @param screenName Specfies the screen name of the user for whom to return the followers list.
	 * @param paging Specifies the page number of the results beginning at 1. A single page contains 5000 ids. This is recommended for users with large ID lists. If not provided all ids are returned.
	 * @return The ID or screen_name of the user to retrieve the friends ID list for.
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">followers/ids </a>
	 * @deprecated use getFollowersIDs(String screenName, long cursor) instead
	 */
	public IDs getFollowersIDs(String screenName, Paging paging) throws WeiboException {
	    return new IDs(get(getBaseURL() + "followers/ids.xml?screen_name="
	            + screenName, null, paging, true));
	}


	/**
	 * Updates the location
	 *
	 * @param location the current location of the user
	 * @return the updated user
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @deprecated Use updateProfile(String name, String email, String url, String location, String description) instead
	 */
	public User updateLocation(String location) throws WeiboException {
	    return new User(http.post(getBaseURL() + "account/update_location.xml", new PostParameter[]{new PostParameter("location", location)}, true), this);
	}


	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * @return List<Status>
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites">favorites </a>
	 * @deprecated Use getFavorited() instead
	 */
	public List<Status> favorites() throws WeiboException {
	    return getFavorites();
	}


	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * @param page the number of page
	 * @return List<Status>
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites">favorites </a>
	 * @deprecated Use getFavorites(int page) instead
	 */
	public List<Status> favorites(int page) throws WeiboException {
	    return getFavorites(page);
	}


	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 *
	 * @param id the ID or screen name of the user for whom to request a list of favorite statuses
	 * @return List<Status>
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites">favorites </a>
	 * @deprecated Use getFavorites(String id) instead
	 */
	public List<Status> favorites(String id) throws WeiboException {
	    return getFavorites(id);
	}


	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 *
	 * @param id   the ID or screen name of the user for whom to request a list of favorite statuses
	 * @param page the number of page
	 * @return List<Status>
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use getFavorites(String id, int page) instead
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites">favorites </a>
	 */
	public List<Status> favorites(String id, int page) throws WeiboException {
	    return getFavorites(id, page);
	}


	/**
	 * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 *
	 * @param id String
	 * @return User
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use enableNotification(String id) instead
	 */
	public User follow(String id) throws WeiboException {
	    return enableNotification(id);
	}


	/**
	 * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
	 * @param id String
	 * @return User
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @deprecated Use disableNotification(String id) instead
	 */
	public User leave(String id) throws WeiboException {
	    return disableNotification(id);
	}


	/* Block Methods */
	
	/**
	 * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
	 * @param id the ID or screen_name of the user to block
	 * @return the blocked user
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @deprecated Use createBlock(String id) instead
	 */
	public User block(String id) throws WeiboException {
	    return new User(http.post(getBaseURL() + "blocks/create/" + id + ".xml", true), this);
	}


	/**
	 * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
	 * @param id the ID or screen_name of the user to block
	 * @return the unblocked user
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @deprecated Use destroyBlock(String id) instead
	 */
	public User unblock(String id) throws WeiboException {
	    return new User(http.post(getBaseURL() + "blocks/destroy/" + id + ".xml", true), this);
	}


	/**
	 * Returns extended information of the authenticated user.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.<br>
	 * The call Weibo.getAuthenticatedUser() is equivalent to the call:<br>
	 * weibo.getUserDetail(weibo.getUserId());
	 * @return User
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.2.0
	 * @deprecated Use verifyCredentials() instead
	 */
	public User getAuthenticatedUser() throws WeiboException {
	    return new User(get(getBaseURL() + "account/verify_credentials.xml", true),this);
	}


	/**
	 * @return the schedule
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since Weibo4J 1.0.4
	 * @deprecated this method is not supported by the Weibo API anymore
	 */
	public String getDowntimeSchedule() throws WeiboException {
	    throw new WeiboException(
	            "this method is not supported by the Weibo API anymore"
	            , new NoSuchMethodException("this method is not supported by the Weibo API anymore"));
	}
	/**
	 * Returns the top 20 trending topics for each hour in a given day.
	 * @return the result
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since  Weibo4J 1.2.0
	 *  @deprecated user getTreandDaily instead
	 */
	public List<Trends> getDailyTrends() throws WeiboException {
	    return Trends.constructTrendsList(get(searchBaseURL + "trends/daily.json", false));
	}
	/**
	 * Returns the top 20 trending topics for each hour in a given day.
	 * @param date Permits specifying a start date for the report.
	 * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
	 * @return the result
	 * @throws WeiboException when Weibo service or network is unavailable
	 * @since  Weibo4J 1.2.0
	 * @deprecated user getTreandDaily instead
	 */
	public List<Trends> getDailyTrends(Date date, boolean excludeHashTags) throws WeiboException {
	    return Trends.constructTrendsList(get(searchBaseURL
	            + "trends/daily.json?date=" + toDateStr(date)
	            + (excludeHashTags ? "&exclude=hashtags" : ""), false));
	}
	
	
	  /**
     * Returns the top 30 trending topics for each day in a given week.
     * @return the result
     * @throws WeiboException when Weibo service or network is unavailable
     * @since  Weibo4J 1.2.0
     * @deprecated use getTrendsWeekly instead
     */
    public List<Trends> getWeeklyTrends() throws WeiboException {
        return Trends.constructTrendsList(get(searchBaseURL
                + "trends/weekly.json", false));
    }

    /**
     * Returns the top 30 trending topics for each day in a given week.
     * @param date Permits specifying a start date for the report.
     * @param excludeHashTags Setting this to true will remove all hashtags from the trends list.
     * @return the result
     * @throws WeiboException when Weibo service or network is unavailable
     * @since  Weibo4J 1.2.0
     * @deprecated use getTrendsWeekly instead
     */
    public List<Trends> getWeeklyTrends(Date date, boolean excludeHashTags) throws WeiboException {
        return Trends.constructTrendsList(get(searchBaseURL
                + "trends/weekly.json?date=" + toDateStr(date)
                + (excludeHashTags ? "&exclude=hashtags" : ""), false));
    }
    public void setToken(AccessToken accessToken) {
	this.setToken(accessToken.getToken(), accessToken.getTokenSecret());
	
    }
	//----------------------------Tags鎺ュ彛 ----------------------------------------
	
	/**
	 * Return to the list of tags specified user
	 * @param user_id
	 * @return tags
	 * @throws WeiboException
	 * @since Weibo4J 1.2.0
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags">Tags </a>
	 * @deprecated use getTags instead
	 */
	public List<Tag>gettags(String user_id)throws WeiboException{
		return Tag.constructTags(http.get(getBaseURL()+"tags.json?"+"user_id="+user_id,true));
	}
	/**
	 * 杩斿洖鐢ㄦ埛鎰熷叴瓒ｇ殑鏍囩
	 * @return a list of tags
	 * @throws WeiboException
	 * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/suggestions">Tags/suggestions </a>
	 * @deprecated use getSuggestionsTags
	 */
	
	public List<Tag> getSuggestions()throws WeiboException{
	    return Tag.constructTags(get(getBaseURL()+"tags/suggestions.json",true));
	 }
    /**
     * 
     * @return List<Status>
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites">favorites </a>
     * @since Weibo4J 1.2.0
     * @deprecated
     */
    public List<Status> getFavorites() throws WeiboException {
//        return Status.constructStatuses(get(getBaseURL() + "favorites.xml", new PostParameter[0], true), this);
    	return Status.constructStatuses(get(getBaseURL() + "favorites.json", new PostParameter[0], true));
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     *
     * @param id the ID or screen name of the user for whom to request a list of favorite statuses
     * @return List<Status>
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites">favorites </a>
     * @since Weibo4J 1.2.0
     *  @deprecated
     */
    public List<Status> getFavorites(String id) throws WeiboException {
//        return Status.constructStatuses(get(getBaseURL() + "favorites/" + id + ".xml", new PostParameter[0], true), this);
    	 return Status.constructStatuses(get(getBaseURL() + "favorites/" + id + ".json", new PostParameter[0], true));
    }

    /**
     * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
     *
     * @param id   the ID or screen name of the user for whom to request a list of favorite statuses
     * @param page the number of page
     * @return List<Status>
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites">favorites </a>
     * @deprecated
     */
    public List<Status> getFavorites(String id, int page) throws WeiboException {
//        return Status.constructStatuses(get(getBaseURL() + "favorites/" + id + ".xml", "page", String.valueOf(page), true), this);
    	return Status.constructStatuses(get(getBaseURL() + "favorites/" + id + ".json", "page", String.valueOf(page), true));
    }

	
	
}