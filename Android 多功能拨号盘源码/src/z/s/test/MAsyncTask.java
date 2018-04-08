package z.s.test;

import java.util.ArrayList;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MAsyncTask extends AsyncTask<Cursor, Void, ArrayList<ContactInfo>>{

	private static final String TAG = MAsyncTask.class.getSimpleName();

	/** 下载开始  */
	public static final int DOWNLOADING_START_MESSAGE = 7;
	/** 下载完成  */
	public static final int DOWNLOAD_END_MESSAGE = 17;

	private Context mContext = null;
	private Handler mHandler = null;

	protected MAsyncTask(Context context, Handler handler){
		this.mContext = context;
		this.mHandler = handler;
	}

	@Override
	protected void onPreExecute() {
		sendStartMessage(DOWNLOADING_START_MESSAGE);
	}

	@Override
	protected ArrayList<ContactInfo> doInBackground(Cursor... params) {		
		Cursor cursor = params[0];

		ArrayList<ContactInfo> ciList = new ArrayList<ContactInfo>();
		if (cursor != null && cursor.getCount() > 0) {
			try {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					long contactId = cursor.getLong(4);
					ContactInfo contactInfo = new ContactInfo();
					contactInfo.setId(contactId);
					contactInfo.setPhoneNum(number);
					contactInfo.setName(name);
					if (contactInfo.getName() == null) {
						contactInfo.setName(contactInfo.getPhoneNum());
					}
					contactInfo.setFormattedNumber(getNameNum(contactInfo.getName() + ""));
					contactInfo.setPinyin(ToPinYin.getPinYin(contactInfo.getName() + ""));
					ciList.add(contactInfo);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}
		return ciList;
	}

	@Override
	protected void onPostExecute(ArrayList<ContactInfo> result) {
		sendEndMessage(DOWNLOAD_END_MESSAGE, result);
	}

	public static void startRequestServerData(Context context, Handler handler, Cursor cursor){
		new MAsyncTask(context, handler).execute(cursor);
	}

	/**
	 * 整理开始
	 * @param messageWhat 
	 */
	private void sendStartMessage(int messageWhat){	
		Message message = new Message();
		message.what = messageWhat; 
		if (mHandler != null) {
			mHandler.sendMessage(message);	
		}	
	}

	/**
	 * 整理完成
	 * @param messageWhat 
	 */
	private void sendEndMessage(int messageWhat, ArrayList<ContactInfo> result){	
		Message message = new Message();
		message.what = messageWhat; 
		Bundle bundle = new Bundle();
		bundle.putSerializable("完成", result);
		message.setData(bundle);
		if (mHandler != null) {
			mHandler.sendMessage(message);	
		}	
	}

	private String getNameNum(String name) {
		try {
			if (name != null && name.length() != 0) {
				int len = name.length();
				char[] nums = new char[len];
				for (int i = 0; i < len; i++) {
					String tmp = name.substring(i);
					nums[i] = getOneNumFromAlpha(ToPinYin.getPinYin(tmp).toLowerCase().charAt(0));
				}
				return new String(nums);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return null;
	}

	private char getOneNumFromAlpha(char firstAlpha) {
		switch (firstAlpha) {
		case 'a':
		case 'b':
		case 'c':
			return '2';
		case 'd':
		case 'e':
		case 'f':
			return '3';
		case 'g':
		case 'h':
		case 'i':
			return '4';
		case 'j':
		case 'k':
		case 'l':
			return '5';
		case 'm':
		case 'n':
		case 'o':
			return '6';
		case 'p':
		case 'q':
		case 'r':
		case 's':
			return '7';
		case 't':
		case 'u':
		case 'v':
			return '8';
		case 'w':
		case 'x':
		case 'y':
		case 'z':
			return '9';
		default:
			return '0';
		}
	}
}
