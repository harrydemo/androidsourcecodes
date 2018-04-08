package weibo4andriod.examples;

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;

public class WebOAuth {

	public static RequestToken request(String backUrl) {
		try {
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",Weibo.CONSUMER_SECRET);
			
			Weibo weibo = new Weibo();
			RequestToken requestToken = weibo.getOAuthRequestToken(backUrl);

			System.out.println("Got request token.");
			System.out.println("Request token: " + requestToken.getToken());
			System.out.println("Request token secret: "
					+ requestToken.getTokenSecret());
			return requestToken;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static AccessToken requstAccessToken(RequestToken requestToken,
			String verifier) {
		try {
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);

			Weibo weibo = new Weibo();
			AccessToken accessToken = weibo.getOAuthAccessToken(requestToken
					.getToken(), requestToken.getTokenSecret(), verifier);

			System.out.println("Got access token.");
			System.out.println("access token: " + accessToken.getToken());
			System.out.println("access token secret: "
					+ accessToken.getTokenSecret());
			return accessToken;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void update(AccessToken access, String content) {
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(access.getToken(), access.getTokenSecret());
            Status status = weibo.updateStatus(content);
			System.out.println("Successfully updated the status to ["
			                + status.getText() + "].");
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}
