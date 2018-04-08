package com.mime.qweibo.examples;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.mime.qweibo.OauthKey;
import com.mime.qweibo.QParameter;
import com.mime.qweibo.QWeiboRequest;
import com.mime.qweibo.examples.QWeiboType.PageFlag;
import com.mime.qweibo.examples.QWeiboType.ResultType;

public class QWeiboSyncApi {

	/**
	 * Get request token.
	 * 
	 * @param customKey
	 *            Your AppKey.
	 * @param customSecret
	 *            Your AppSecret.
	 * @return The request token.
	 */
	public String getRequestToken(String customKey, String customSecret) {
		String url = "http://open.t.qq.com/cgi-bin/request_token";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		//The OAuth Call back URL(You should encode this url if it
		//contains some unreserved characters).
		oauthKey.callbackUrl = MyWeiboSync.CALLBACK_URL;

		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Get access token.
	 * 
	 * @param customKey
	 *            Your AppKey.
	 * @param customSecret
	 *            Your AppSecret
	 * @param requestToken
	 *            The request token.
	 * @param requestTokenSecret
	 *            The request token Secret
	 * @param verify
	 *            The verification code.
	 * @return
	 */
	public String getAccessToken(String customKey, String customSecret,
			String requestToken, String requestTokenSecrect, String verify) {

		String url = "http://open.t.qq.com/cgi-bin/access_token";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;
		oauthKey.verify = verify;

		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Get home page messages.
	 * 
	 * @param customKey
	 *            Your AppKey
	 * @param customSecret
	 *            Your AppSecret
	 * @param requestToken
	 *            The access token
	 * @param requestTokenSecret
	 *            The access token secret
	 * @param format
	 *            Response format, xml or json
	 * @param pageFlag
	 *            Page number.
	 * @param nReqNum
	 *            Number of messages you want.
	 * @return Response messages based on the specified format.
	 */
	public String getHomeMsg(String customKey, String customSecret,
			String requestToken, String requestTokenSecrect, ResultType format,
			PageFlag pageFlag, int nReqNum) {
		String url = "http://open.t.qq.com/api/statuses/home_timeline";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}

		parameters.add(new QParameter("format", strFormat));
		parameters.add(new QParameter("pageflag", String.valueOf(pageFlag
				.ordinal())));
		parameters.add(new QParameter("reqnum", String.valueOf(nReqNum)));

		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public String getWeiboDetail(String customKey, String customSecret,String requestToken, String requestTokenSecrect,String id,ResultType format) {
		String url = "http://open.t.qq.com/api/t/show";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}

		parameters.add(new QParameter("format", strFormat));
		parameters.add(new QParameter("id", id));
		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public String getUserInfoByName(String customKey, String customSecret,String requestToken, String requestTokenSecrect,String name,ResultType format) {
		String url = "http://open.t.qq.com/api/user/other_info";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}
		
		parameters.add(new QParameter("format", strFormat));
		parameters.add(new QParameter("name", name));
		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	//获取提及到我的微博列表
	public String getRefers(String customKey, String customSecret,String requestToken, String requestTokenSecrect,PageFlag pageflag,int pagetime,int reqnum,int lastid,ResultType format) {
		String url = "http://open.t.qq.com/api/statuses/mentions_timeline";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}
		parameters.add(new QParameter("format", strFormat));
		parameters.add(new QParameter("pageflag", String.valueOf(pageflag.ordinal())));
		parameters.add(new QParameter("pagetime", String.valueOf(pagetime)));
		parameters.add(new QParameter("reqnum", String.valueOf(reqnum)));
		parameters.add(new QParameter("lastid", String.valueOf(lastid)));
		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	//获取某用户广播列表
	public String getTweets(String customKey, String customSecret,String requestToken, String requestTokenSecrect,PageFlag pageflag,int pagetime,int reqnum,int lastid,String name,ResultType format) {
		String url = "http://open.t.qq.com/api/statuses/user_timeline";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}
		parameters.add(new QParameter("format", strFormat));
		parameters.add(new QParameter("pageflag", String.valueOf(pageflag.ordinal())));
		parameters.add(new QParameter("pagetime", String.valueOf(pagetime)));
		parameters.add(new QParameter("reqnum", String.valueOf(reqnum)));
		parameters.add(new QParameter("lastid", String.valueOf(lastid)));
		parameters.add(new QParameter("name", name));
		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	//获取粉丝列表
	public String getFans(String customKey, String customSecret,String requestToken, String requestTokenSecrect,int reqnum,int startindex,String name,ResultType format) {
		String url = "http://open.t.qq.com/api/friends/user_fanslist";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}
		
		parameters.add(new QParameter("format", strFormat));
		parameters.add(new QParameter("reqnum", String.valueOf(reqnum)));
		parameters.add(new QParameter("startindex", String.valueOf(startindex)));
		parameters.add(new QParameter("name", name));
		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	//获取偶像列表
	public String getIdols(String customKey, String customSecret,String requestToken, String requestTokenSecrect,int reqnum,int startindex,String name,ResultType format) {
		String url = "http://open.t.qq.com/api/friends/user_idollist";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}
		
		parameters.add(new QParameter("format", strFormat));
		parameters.add(new QParameter("reqnum", String.valueOf(reqnum)));
		parameters.add(new QParameter("startindex", String.valueOf(startindex)));
		parameters.add(new QParameter("name", name));
		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	//转播一条微博
	public String reBroad(String customKey, String customSecret,String requestToken, String requestTokenSecrect,String content,String reid,ResultType format) {
		String url = "http://open.t.qq.com/api/t/re_add";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}
		
		parameters.add(new QParameter("format", strFormat));
		try {
			parameters.add(new QParameter("content",new String(content.getBytes("UTF-8"))));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		parameters.add(new QParameter("clientip", "127.0.0.1"));
		parameters.add(new QParameter("reid",reid));
		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "POST", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	//对话,发私信
	public String addPrivate(String customKey, String customSecret,String requestToken, String requestTokenSecrect,String content,String name,ResultType format) {
		String url = "http://open.t.qq.com/api/private/add";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}
		
		parameters.add(new QParameter("format", strFormat));
		try {
			parameters.add(new QParameter("content",new String(content.getBytes("UTF-8"))));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		parameters.add(new QParameter("clientip", "127.0.0.1"));
		parameters.add(new QParameter("name",name));
		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	//点评一条微博
	public String addComment(String customKey, String customSecret,String requestToken, String requestTokenSecrect,String content,String reid,ResultType format) {
		String url = "http://open.t.qq.com/api/t/comment";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}
		
		parameters.add(new QParameter("format", strFormat));
		try {
			parameters.add(new QParameter("content",new String(content.getBytes("UTF-8"))));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		parameters.add(new QParameter("clientip", "127.0.0.1"));
		parameters.add(new QParameter("reid", reid));
		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	//删除一条微博
	public String delete(String customKey, String customSecret,String requestToken, String requestTokenSecrect,String id,ResultType format) {
		String url = "http://open.t.qq.com/api/t/del";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}
		
		parameters.add(new QParameter("format", strFormat));
		parameters.add(new QParameter("id",id));
		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	//收藏一条微博
	public String addFav(String customKey, String customSecret,String requestToken, String requestTokenSecrect,String id,ResultType format) {
		String url = "http://open.t.qq.com/api/fav/addt";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}
		parameters.add(new QParameter("format", strFormat));
		parameters.add(new QParameter("id",id));
		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Publish a Weibo message.
	 * 
	 * @param customKey
	 *            Your AppKey
	 * @param customSecret
	 *            Your AppSecret
	 * @param requestToken
	 *            The access token
	 * @param requestTokenSecrect
	 *            The access token secret
	 * @param content
	 *            The content of your message
	 * @param pic
	 *            The files of your images.
	 * @param format
	 *            Response format, xml or json(Default).
	 * @return Result info based on the specified format.
	 */
	public String publishMsg(String customKey, String customSecret,
			String requestToken, String requestTokenSecrect, String content,
			String pic, ResultType format) {

		List<QParameter> files = new ArrayList<QParameter>();
		String url = null;
		String httpMethod = "POST";

		if (pic == null || pic.trim().equals("")) {
			url = "http://open.t.qq.com/api/t/add";
		} else {
			url = "http://open.t.qq.com/api/t/add_pic";
			files.add(new QParameter("pic", pic));
		}

		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		List<QParameter> parameters = new ArrayList<QParameter>();

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}

		parameters.add(new QParameter("format", strFormat));
		try {
			parameters.add(new QParameter("content", new String(content
					.getBytes("UTF-8"))));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return "";
		}
		parameters.add(new QParameter("clientip", "127.0.0.1"));

		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, httpMethod, oauthKey, parameters,
					files);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public String getUserInfo(String customKey, String customSecret,String requestToken, String requestTokenSecrect, ResultType format) {
		String url = "http://open.t.qq.com/api/user/info";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			return "";
		}

		parameters.add(new QParameter("format", strFormat));

		QWeiboRequest request = new QWeiboRequest();
		String res = null;
		try {
			res = request.syncRequest(url, "GET", oauthKey, parameters, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
