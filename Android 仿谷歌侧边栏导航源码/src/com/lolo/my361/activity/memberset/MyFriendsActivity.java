package com.lolo.my361.activity.memberset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.lolo.my361.activity.slyday.R;
import com.lolo.my361.adapter.memberset.MyFriendsAdapter;
import com.lolo.my361.entity.Friend;
import com.lolo.my361.view.LetterlistViewForFriend;
import com.lolo.my361.view.LetterlistViewForFriend.OnTouchingLetterChangedListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MyFriendsActivity extends Activity {
	ListView lv_friend;
	String[] strings;// 存放存在的汉语拼音首字母
	List<Friend> friends;
	//右边字母导航
	LetterlistViewForFriend letterlistViewForFriend;
	HashMap<String, Integer> alphaIndex;// 存放存在的汉语拼音首字母和与之对应的列表位置
	TextView overlay;
	Handler handler;
	OverlayThread overlayThread; // 隐藏字母提示框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vip_myactivity_myfriend);
		overlayThread = new OverlayThread();
		handler = new Handler();
		/* 初始化右边导航 */
		initOverlay();
		/* 初始化通讯录 */
		initData();
		lv_friend = (ListView) this.findViewById(R.id.vip_myfriend_lv_1);
		
		lv_friend.setAdapter(new MyFriendsAdapter(MyFriendsActivity.this,
				friends));
		lv_friend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			}

		});
		
		letterlistViewForFriend=(LetterlistViewForFriend)this.findViewById(R.id.LetterlistViewForFriend);
		letterlistViewForFriend.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				// TODO Auto-generated method stub
				System.out.println(s);
				int position = alphaIndex.get(s);
				if (alphaIndex.get(s) != null) {
					lv_friend.setSelection(position);
				}
				overlay.setText(s);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1500);
			}
		});
   
	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay_layout, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	public class OverlayThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			overlay.setVisibility(View.INVISIBLE);
		}

	}

	private void initData() {
		List<String> list_data = new ArrayList<String>();
		list_data.add("Alex");
		list_data.add("Blic");
		list_data.add("Blpha");
		list_data.add("Clex");
		list_data.add("Clic");
		list_data.add("Clpha");
		list_data.add("Clex");
		list_data.add("Dlic");
		list_data.add("Dlpha");
		list_data.add("Dace");
		list_data.add("Dlpha");
		list_data.add("Elic");
		list_data.add("Elpha");
		list_data.add("Eace");
		list_data.add("Elpha");
		list_data.add("Elea");
		list_data.add("Flpha");
		list_data.add("Flea");
		list_data.add("Glpha");
		list_data.add("Gace");
		list_data.add("Glpha");
		list_data.add("Hlea");
		list_data.add("Ilpha");
		list_data.add("Jace");
		list_data.add("Jlpha");
		list_data.add("Jlea");
		list_data.add("Kace");
		list_data.add("Klpha");
		list_data.add("Klea");
		list_data.add("Lace");
		list_data.add("Llpha");
		list_data.add("Mlea");
		list_data.add("Mlea");
		list_data.add("Nlea");
		list_data.add("Olea");
		list_data.add("Plea");
		list_data.add("Qlpha");
		list_data.add("Qlea");
		list_data.add("Race");
		list_data.add("Rlpha");
		list_data.add("Slea");
		list_data.add("Slea");
		list_data.add("Tea");
		list_data.add("Tea");
		list_data.add("Tea");
		list_data.add("Uea");
		list_data.add("Uea");
		list_data.add("Uea");
		list_data.add("Vea");
		list_data.add("Wea");
		list_data.add("Wea");
		list_data.add("Yea");
		list_data.add("Yea");
		list_data.add("Yea");
		list_data.add("Yea");
		list_data.add("Yea");
		list_data.add("Zea");

		friends = new ArrayList<Friend>();
		alphaIndex = new HashMap<String, Integer>();

		for (int i = 0; i < list_data.size(); i++) {
			Friend friend = new Friend();
			friend.setName(list_data.get(i));
			friends.add(friend);
		}
		strings = new String[friends.size()];
		for (int i = 0; i < friends.size(); i++) {
			char letter = friends.get(i).getName().substring(0, 1).charAt(0);
			String currentLetter = (letter + "").toUpperCase();
			// 上一个汉语拼音首字母，如果不存在为""
			String lastLetter = (i - 1) > 0 ? (friends.get(i - 1).getName()
					.substring(0, 1).charAt(0) + "").toUpperCase() : "";
			if (!lastLetter.equals(currentLetter)) {
				alphaIndex.put(currentLetter, i);
				strings[i]=currentLetter;
			}

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.removeView(overlay);
	}

}
