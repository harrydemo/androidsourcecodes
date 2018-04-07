package com.hrw.android.player.utils;

public class Constants {
	public final static int MENU_TO_PLAYER_REQUEST_CODE = 0;
	public final static int MENU_TO_PLAYER_RESULT_CODE = 0;

	public final static int MENU_TO_PLAYLIST_REQUEST_CODE = 1;
	public final static int MENU_TO_PLAYLIST_RESULT_CODE = 1;

	public final static String UPDATE_UI_ACTION = "com.hrw.android.updateui";

	public static enum TAB_SPEC_TAG {
		MAIN_SPEC_TAG("1"), PLAYLIST_SPEC_TAG("2"), AUDIO_LIST_SPEC_TAG("3"), PLAYER_SPEC_TAG(
				"4");

		private TAB_SPEC_TAG(String id) {
			this.id = id;

		}

		private String id;

		public String getId() {
			return this.id;
		}

	};

	public enum PopupMenu {
		ADD_TO(1), ADD_ALL_TO(2), DELETE(3), CREATE_LIST(4), SCAN(5), SETTING(6), HELP(
				7), EXIT(8);
		private final int menu;

		private PopupMenu(int menu) {
			this.menu = menu;
		}

		public int getMenu() {
			return menu;
		}

		public static Boolean VISIABLE = true;
		public static Boolean INVISIABLE = false;
		public final static int ADD_TO_INDEX = 1;
		public final static int ADD_ALL_TO_INDEX = 2;
		public final static int DELETE_INDEX = 3;
		public final static int CREATE_LIST_INDEX = 4;
		public final static int SCAN_INDEX = 5;
		public final static int SETTING_INDEX = 6;
		public final static int HELP_INDEX = 7;
		public final static int EXIT_INDEX = 8;

	}
}
