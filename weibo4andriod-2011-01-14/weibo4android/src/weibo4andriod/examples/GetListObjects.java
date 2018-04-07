package weibo4andriod.examples;

import java.util.List;

import weibo4andriod.ListObject;
import weibo4andriod.ListObjectWapper;
import weibo4andriod.Weibo;

public class GetListObjects {

	/**
	 * Usage: java -DWeibo4j.oauth.consumerKey=[consumer key] -DWeibo4j.oauth.consumerSecret=[consumer secret] Weibo4j.examples.GetListObjects [accessToken] [accessSecret] [uid]
	 * @param args message
	 */
	public static void main(String[] args) {
		try {
			if (args.length < 3) {
				System.out.println("No Token/TokenSecret/(Uid or ScreenName) specified.");
				System.out.println("Usage: java Weibo4j.examples.GetListObjects Token TokenSecret Uid");
				System.exit(-1);
			}
			//
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);

			Weibo weibo = new Weibo();
			weibo.setToken(args[0], args[1]);

			String screenName = args[2]; 

			try {
				ListObjectWapper wapper = weibo.getUserLists(screenName, true);
				List<ListObject> lists = wapper.getListObjects();
				for (ListObject listObject : lists) {
					System.out.println(listObject.toString());
				}
				System.out.println("previous_cursor: " + wapper.getPreviousCursor());
				System.out.println("next_cursor: " + wapper.getNextCursor());
				//
				System.out.println("Successfully get lists of [" + screenName + "].");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		} catch (Exception ioe) {
			System.out.println("Failed to read the system input.");
			System.exit(-1);
		}
	}
}
