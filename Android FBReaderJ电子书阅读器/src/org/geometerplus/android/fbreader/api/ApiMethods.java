/*
 * This code is in the public domain.
 */

package org.geometerplus.android.fbreader.api;

interface ApiMethods {
	// fbreader information
	int GET_FBREADER_VERSION = 1;

	// library information

	// network library information

	// bookmarks information

	// preferences
	int LIST_OPTION_GROUPS = 401;
	int LIST_OPTION_NAMES = 402;
	int GET_OPTION_VALUE = 403;
	int SET_OPTION_VALUE = 404;

	// book information
	int GET_BOOK_LANGUAGE = 501;
	int GET_BOOK_TITLE = 502;
	int LIST_BOOK_AUTHORS = 503;
	int LIST_BOOK_TAGS = 504;
	int GET_BOOK_FILE_PATH = 505;
	int GET_BOOK_HASH = 506;
	int GET_BOOK_UNIQUE_ID = 507;
	int GET_BOOK_LAST_TURNING_TIME = 508;

	// text information
	int GET_PARAGRAPHS_NUMBER = 601;
	int GET_ELEMENTS_NUMBER = 602;
	int GET_PARAGRAPH_TEXT = 603;

	// page information
	int GET_PAGE_START = 701;
	int GET_PAGE_END = 702;
	int IS_PAGE_END_OF_TEXT = 703;
	int IS_PAGE_END_OF_SECTION = 704;

	// view management
	int SET_PAGE_START = 801;
	int HIGHLIGHT_AREA = 802;
	int CLEAR_HIGHLIGHTING = 803;

	// action control
	int LIST_ACTIONS = 901;
	int LIST_ACTION_NAMES = 902;

	int GET_KEY_ACTION = 911;
	int SET_KEY_ACTION = 912;

	int LIST_ZONEMAPS = 921;
	int GET_ZONEMAP = 922;
	int SET_ZONEMAP = 923;
	int GET_ZONEMAP_HEIGHT = 924;
	int GET_ZONEMAP_WIDTH = 925;
	int CREATE_ZONEMAP = 926;
	int IS_ZONEMAP_CUSTOM = 927;
	int DELETE_ZONEMAP = 928;

	int GET_TAPZONE_ACTION = 931;
	int SET_TAPZONE_ACTION = 932;
}
