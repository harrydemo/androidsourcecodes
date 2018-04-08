package trainApp.common;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpHelper {
	private HttpHelper() {
	}

	/**
	 * 解析postback信息，以便方便得到我们需要的串
	 * @param postBackMessage
	 * @return
	 */
	public static String parse(String postBackMessage) {
		postBackMessage = postBackMessage.replace("<script>", "");
		postBackMessage = postBackMessage.replace("</script>", "");
		postBackMessage = postBackMessage.replace("//", "");
		postBackMessage = postBackMessage.replace("^", ",");
		return postBackMessage;
	}

	/**
	 * 获取服务器更新时间
	 * @param postBackMessage
	 * @return
	 */
	public static String getRefreshTime(String postBackMessage) {
		String[] messages = postBackMessage.split("pparent.mygrid.clearAll();");
		int start = "null\nparent.document.getElementById(\"gxsj\").innerHTML=\"余票信息每小时更新一次，本次信息更新时间为"
				.length();
		int end = start + 5;
		String resultMsg = postBackMessage.substring(start, end);
		return resultMsg;
	}

	/**
	 * 获取火车信息列表
	 * @param postBackMessage
	 *            服务器端postback数据
	 * @return
	 */
	public static List<TrainInfo> getTrainInfoList(String postBackMessage) {
		List<TrainInfo> TrainArray = new ArrayList<TrainInfo>();

		String[] messages = postBackMessage.split("parent.mygrid.addRow");
		for (int i = 2; i < messages.length; i += 2) {
			String message = messages[i];
			String[] items = message.split(",");

			TrainInfo trainInfo = new TrainInfo();
			String traincode = items[2].trim().replace("(", ",");
			String[] codes = traincode.split(",");
			trainInfo.setTrainCode(codes[0]);
			trainInfo.setStartStation(items[4].trim());
			trainInfo.setArriveStation(items[6].trim());

			trainInfo.setStartTime(items[8].trim());
			trainInfo.setArriveTime(items[9].trim());
			trainInfo.setUsedTime(items[10].trim());

			trainInfo.setHardSeatCount(items[11].trim());
			trainInfo.setSoftSeatCount(items[12].trim());
			trainInfo.setHardCouchetteCount(items[13].trim());
			trainInfo.setSoftCouchetteCount(items[14].trim());
			trainInfo.setFirstClassSeatCount(items[15].trim());
			trainInfo.setSecondClassSeatCount(items[16].trim());
			trainInfo.setPremiumCouchetteCount(items[17].trim());

			trainInfo.setHaveSeat(items[18].trim() == "有" ? true : false);
			TrainArray.add(trainInfo);
		}
		return TrainArray;
	}

	/**
	 * 判断当前设备的网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean flag = false;
		NetworkInfo[] netinfo = null;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != cm) {
			try {
				netinfo = cm.getAllNetworkInfo();
				if (null != netinfo) {
					for (int i = 0; i < netinfo.length; i++) {
						if (netinfo[i].isConnected()) {
							flag = true;
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
			
		return flag;
		
	}
}
