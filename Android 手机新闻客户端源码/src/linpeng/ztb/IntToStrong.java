package linpeng.ztb;

import android.util.Log;

public class IntToStrong {
	public String getname(int i) {
		String name = "title";
		if (i == 0) {
			name = "title1";
		} else if (i == 1) {
			name = "title2";
		} else if (i == 2) {
			name = "title3";
		} else if (i == 3) {
			name = "title4";
		} else if (i == 4) {
			name = "title5";
		} else if (i == 5) {
			name = "title6";
		} else if (i == 6) {
			name = "title7";
		}
		return name;
	}

	public String TypeToType(String name) {
		Log.i("a", name);
		String names = "";
		if (name.equals("title1")) {
			Log.i("a", name);
			names = "newsdetails1";
		}
		if (name.equals("title2")) {
			names = "newsdetails2";
		}
		if (name.equals("title3")) {
			Log.i("a", name);
			names = "newsdetails3";
		}
		if (name.equals("title4")) {
			names = "newsdetails4";
			Log.i("s", "s");
		}
		if (name.equals("title5")) {
			names = "newsdetails5";
		}
		if (name.equals("title6")) {
			names = "newsdetails6";
		}
		if (name.equals("title7")) {
			names = "newsdetails7";
		}
		Log.i("as", names);
		return names;
	}

	public int text_size_to_order(int text_size) {
		int order = 0;
		if (text_size == 16) {
			order = 2;
		} else if (text_size == 20) {
			order = 1;
		} else if (text_size == 24) {
			order = 0;
		}
		return order;
	}

	public int table_name_to_int(String name) {
		int names = 0;
		if (name.equals("title1")) {
			names = 0;
		}
		if (name.equals("title2")) {
			names = 1;
		}
		if (name.equals("title3")) {
			names = 2;
		}
		if (name.equals("title4")) {
			names = 3;
		}
		if (name.equals("title5")) {
			names = 4;
		}
		if (name.equals("title6")) {
			names = 5;
		}
		if (name.equals("title7")) {
			names = 6;
		}
		return names;
	}
}
