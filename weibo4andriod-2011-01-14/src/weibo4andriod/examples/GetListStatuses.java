package weibo4andriod.examples;

import java.util.List;

import weibo4andriod.Status;
import weibo4andriod.Weibo;

/**
 * Example of get statuses of list's members.
 * 
 * @author		bwl (刘道儒)
 */
public class GetListStatuses {

	/**
	 * Usage: java -DWeibo4j.oauth.consumerKey=[consumer key] -DWeibo4j.oauth.consumerSecret=[consumer secret] Weibo4j.examples.GetListStatuses [accessToken] [accessSecret] [Uid] [listId]
	 * @param args message
	 */
	public static void main(String[] args) {
		try {
			if (args.length < 4) {
				System.out.println("No Token/TokenSecret/(Uid or ScreenName)/(ListId or Slug) specified.");
				System.out.println("Usage: java Weibo4j.examples.GetListStatuses Token TokenSecret Uid ListId");
				System.exit(-1);
			}
			//
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);

			Weibo weibo = new Weibo();
			weibo.setToken(args[0], args[1]);

			String screenName = args[2];
			String listId = args[3];

			try {
				List<Status> statuses = weibo.getListStatuses(screenName, listId, true);
				for (Status status : statuses) {
					System.out.println(status.toString());
				}
				//
				System.out.println("Successfully get statuses of [" + listId + "].");
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
