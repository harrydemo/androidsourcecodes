package weibo4andriod.examples;

import java.io.File;

import weibo4andriod.User;
import weibo4andriod.Weibo;

public class OAuthUpdateProfile {

	/**
	 * Usage: java -DWeibo4j.oauth.consumerKey=[consumer key]
	 * -DWeibo4j.oauth.consumerSecret=[consumer secret]
	 * Weibo4j.examples.OAuthUpdateProfile [accessToken] [accessSecret]
	 * [imageFilePath]
	 * 
	 * @param args
	 *            message
	 */
	public static void main(String[] args) {
		try {
			if (args.length < 3) {
	            System.out.println(
	                "Usage: java weibo4j.examples.OAuthUpdateProfile token tokenSecret filePath");
	            System.exit( -1);
	        }
			
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);

			Weibo weibo = new Weibo();
			
			/*
			 * 此处需要填写AccessToken的key和Secret，可以从OAuthUpdate的执行结果中拷贝过来
             */
			weibo.setToken(args[0], args[1]);
			try {
				File file=new File(args[2]);
				if(file==null){
					System.out.println("file is null");
					System.exit(-1);
				}
				
				User user = weibo.updateProfileImage(file);

				System.out.println("Successfully upload the status to ["
						+ user.getName() + "].");
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception ioe) {
			System.out.println("Failed to read the system input.");
		}
	}

}
