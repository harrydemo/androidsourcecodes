/*
 * Copyright (C) 2009-2011 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.geometerplus.android.fbreader.api.PluginApi;
import org.geometerplus.android.fbreader.library.KillerCallback;
import org.geometerplus.android.fbreader.library.SQLiteBooksDatabase;
import org.geometerplus.android.util.UIUtil;
import org.geometerplus.fbreader.bookmodel.BookModel;
import org.geometerplus.fbreader.fbreader.ActionCode;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.fbreader.library.Book;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.text.hyphenation.ZLTextHyphenator;
import org.geometerplus.zlibrary.text.view.ZLTextView;
import org.geometerplus.zlibrary.ui.android.R;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidActivity;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidApplication;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.nil.util.L;

public final class FBReader extends ZLAndroidActivity {
	public static final String BOOK_PATH_KEY = "BookPath";

	public static final int REQUEST_PREFERENCES = 1;
	public static final int REQUEST_BOOK_INFO = 2;
	public static final int REQUEST_CANCEL_MENU = 3;

	public static final int RESULT_DO_NOTHING = RESULT_FIRST_USER;
	public static final int RESULT_REPAINT = RESULT_FIRST_USER + 1;
	public static final int RESULT_RELOAD_BOOK = RESULT_FIRST_USER + 2;

	private int myFullScreenFlag;

	private static final String PLUGIN_ACTION_PREFIX = "___";
	private final List<PluginApi.ActionInfo> myPluginActions = new LinkedList<PluginApi.ActionInfo>();
	private final BroadcastReceiver myPluginInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final ArrayList<PluginApi.ActionInfo> actions = getResultExtras(
					true).<PluginApi.ActionInfo> getParcelableArrayList(
					PluginApi.PluginInfo.KEY);
			if (actions != null) {
				L.l("=============action count:"+actions.size());
				synchronized (myPluginActions) {
					final FBReaderApp fbReader = (FBReaderApp) FBReaderApp
							.Instance();
					int index = 0;
					while (index < myPluginActions.size()) {
						fbReader.removeAction(PLUGIN_ACTION_PREFIX + index++);
					}
					myPluginActions.addAll(actions);
					index = 0;
					L.l("================index:" + myPluginActions.size());
					for (int i = 0; i < actions.size(); i++) {
						L.l("=========ha======");
						PluginApi.ActionInfo info = actions.get(i);
						if (info != null)
						{
							L.l("==============id:"+info.getId());
							fbReader.addAction(PLUGIN_ACTION_PREFIX + index++,
									new RunPluginAction(FBReader.this,
											fbReader, info.getId()));
						}
					}
					// for (PluginApi.ActionInfo info : myPluginActions) {
					// L.l("======name:" + (info == null));
					// if (info != null) {
					// L.l("======name:" +info.getId());
					// fbReader.addAction(PLUGIN_ACTION_PREFIX + index++,
					// new RunPluginAction(FBReader.this,
					// fbReader, info.getId()));
					// }
					// }
				}
			}
		}
	};

	@Override
	protected ZLFile fileFromIntent(Intent intent) {
		String filePath = intent.getStringExtra(BOOK_PATH_KEY);
		if (filePath == null) {
			final Uri data = intent.getData();
			if (data != null) {
				filePath = data.getPath();
			}
		}
		return filePath != null ? ZLFile.createFileByPath(filePath) : null;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		final ZLAndroidApplication application = (ZLAndroidApplication) getApplication();
		myFullScreenFlag = application.ShowStatusBarOption.getValue() ? 0
				: WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				myFullScreenFlag);

		final FBReaderApp fbReader = (FBReaderApp) FBReaderApp.Instance();
		if (fbReader.getPopupById(TextSearchPopup.ID) == null) {
			new TextSearchPopup(fbReader);
		}
		if (fbReader.getPopupById(NavigationPopup.ID) == null) {
			new NavigationPopup(fbReader);
		}
		if (fbReader.getPopupById(SelectionPopup.ID) == null) {
			new SelectionPopup(fbReader);
		}

		fbReader.addAction(ActionCode.SHOW_LIBRARY, new ShowLibraryAction(this,
				fbReader));
		fbReader.addAction(ActionCode.SHOW_PREFERENCES,
				new ShowPreferencesAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_BOOK_INFO, new ShowBookInfoAction(
				this, fbReader));
		fbReader.addAction(ActionCode.SHOW_TOC, new ShowTOCAction(this,
				fbReader));
		fbReader.addAction(ActionCode.SHOW_BOOKMARKS, new ShowBookmarksAction(
				this, fbReader));
		fbReader.addAction(ActionCode.SHOW_NETWORK_LIBRARY,
				new ShowNetworkLibraryAction(this, fbReader));

		fbReader.addAction(ActionCode.SHOW_MENU, new ShowMenuAction(this,
				fbReader));
		fbReader.addAction(ActionCode.SHOW_NAVIGATION,
				new ShowNavigationAction(this, fbReader));
		fbReader.addAction(ActionCode.SEARCH, new SearchAction(this, fbReader));

		fbReader.addAction(ActionCode.SELECTION_SHOW_PANEL,
				new SelectionShowPanelAction(this, fbReader));
		fbReader.addAction(ActionCode.SELECTION_HIDE_PANEL,
				new SelectionHidePanelAction(this, fbReader));
		fbReader.addAction(ActionCode.SELECTION_COPY_TO_CLIPBOARD,
				new SelectionCopyAction(this, fbReader));
		fbReader.addAction(ActionCode.SELECTION_SHARE,
				new SelectionShareAction(this, fbReader));
		fbReader.addAction(ActionCode.SELECTION_TRANSLATE,
				new SelectionTranslateAction(this, fbReader));
		fbReader.addAction(ActionCode.SELECTION_BOOKMARK,
				new SelectionBookmarkAction(this, fbReader));

		fbReader.addAction(ActionCode.PROCESS_HYPERLINK,
				new ProcessHyperlinkAction(this, fbReader));

		fbReader.addAction(ActionCode.SHOW_CANCEL_MENU,
				new ShowCancelMenuAction(this, fbReader));
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		final ZLAndroidApplication application = (ZLAndroidApplication) getApplication();
		if (!application.ShowStatusBarOption.getValue()
				&& application.ShowStatusBarWhenMenuIsActiveOption.getValue()) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		final ZLAndroidApplication application = (ZLAndroidApplication) getApplication();
		if (!application.ShowStatusBarOption.getValue()
				&& application.ShowStatusBarWhenMenuIsActiveOption.getValue()) {
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if ((intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
			super.onNewIntent(intent);
		} else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			final String pattern = intent.getStringExtra(SearchManager.QUERY);
			final Runnable runnable = new Runnable() {
				public void run() {
					final FBReaderApp fbReader = (FBReaderApp) FBReaderApp
							.Instance();
					final TextSearchPopup popup = (TextSearchPopup) fbReader
							.getPopupById(TextSearchPopup.ID);
					popup.initPosition();
					fbReader.TextSearchPatternOption.setValue(pattern);
					if (fbReader.getTextView().search(pattern, true, false,
							false, false) != 0) {
						runOnUiThread(new Runnable() {
							public void run() {
								fbReader.showPopup(popup.getId());
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							public void run() {
								UIUtil.showErrorMessage(FBReader.this,
										"textNotFound");
								popup.StartPosition = null;
							}
						});
					}
				}
			};
			UIUtil.wait("search", runnable, this);
		} else {
			super.onNewIntent(intent);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		final ZLAndroidApplication application = (ZLAndroidApplication) getApplication();

		final int fullScreenFlag = application.ShowStatusBarOption.getValue() ? 0
				: WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (fullScreenFlag != myFullScreenFlag) {
			finish();
			startActivity(new Intent(this, getClass()));
		}

		final FBReaderApp fbReader = (FBReaderApp) FBReaderApp.Instance();
		final RelativeLayout root = (RelativeLayout) findViewById(R.id.root_view);
		((PopupPanel) fbReader.getPopupById(TextSearchPopup.ID))
				.createControlPanel(this, root, PopupWindow.Location.Bottom);
		((PopupPanel) fbReader.getPopupById(NavigationPopup.ID))
				.createControlPanel(this, root, PopupWindow.Location.Bottom);
		((PopupPanel) fbReader.getPopupById(SelectionPopup.ID))
				.createControlPanel(this, root, PopupWindow.Location.Floating);

		synchronized (myPluginActions) {
			myPluginActions.clear();
		}

		sendOrderedBroadcast(new Intent(PluginApi.ACTION_REGISTER), null,
				myPluginInfoReceiver, null, RESULT_OK, null, null);
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			sendBroadcast(new Intent(getApplicationContext(),
					KillerCallback.class));
		} catch (Throwable t) {
		}
		PopupPanel.restoreVisibilities(FBReaderApp.Instance());
	}

	@Override
	public void onStop() {
		PopupPanel.removeAllWindows(FBReaderApp.Instance());
		super.onStop();
	}

	@Override
	protected FBReaderApp createApplication(ZLFile file) {
		if (SQLiteBooksDatabase.Instance() == null) {
			new SQLiteBooksDatabase(this, "READER");
		}
		return new FBReaderApp(file != null ? file.getPath() : null);
	}

	@Override
	public boolean onSearchRequested() {
		final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
		final FBReaderApp.PopupPanel popup = fbreader.getActivePopup();
		fbreader.hideActivePopup();
		final SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
		manager.setOnCancelListener(new SearchManager.OnCancelListener() {
			public void onCancel() {
				if (popup != null) {
					fbreader.showPopup(popup.getId());
				}
				manager.setOnCancelListener(null);
			}
		});
		startSearch(fbreader.TextSearchPatternOption.getValue(), true, null,
				false);
		return true;
	}

	public void showSelectionPanel() {
		final FBReaderApp fbReader = (FBReaderApp) FBReaderApp.Instance();
		final ZLTextView view = fbReader.getTextView();
		((SelectionPopup) fbReader.getPopupById(SelectionPopup.ID)).move(
				view.getSelectionStartY(), view.getSelectionEndY());
		fbReader.showPopup(SelectionPopup.ID);
	}

	public void hideSelectionPanel() {
		final FBReaderApp fbReader = (FBReaderApp) FBReaderApp.Instance();
		final FBReaderApp.PopupPanel popup = fbReader.getActivePopup();
		if (popup != null && popup.getId() == SelectionPopup.ID) {
			FBReaderApp.Instance().hideActivePopup();
		}
	}

	private void onBookInfoResult(int resultCode) {
		final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
		switch (resultCode) {
		case RESULT_DO_NOTHING:
			break;
		case RESULT_REPAINT: {
			final BookModel model = fbreader.Model;
			if (model != null) {
				final Book book = model.Book;
				if (book != null) {
					book.reloadInfoFromDatabase();
					ZLTextHyphenator.Instance().load(book.getLanguage());
				}
			}
			fbreader.clearTextCaches();
			fbreader.getViewWidget().repaint();
			break;
		}
		case RESULT_RELOAD_BOOK:
			fbreader.reloadBook();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_PREFERENCES:
			onBookInfoResult(RESULT_REPAINT);
			break;
		case REQUEST_BOOK_INFO:
			onBookInfoResult(resultCode);
			break;
		case REQUEST_CANCEL_MENU: {
			final FBReaderApp fbreader = (FBReaderApp) FBReaderApp.Instance();
			fbreader.runCancelAction(resultCode - 1);
			break;
		}
		}
	}

	public void navigate() {
		((NavigationPopup) FBReaderApp.Instance().getPopupById(
				NavigationPopup.ID)).runNavigation();
	}

	private void addMenuItem(Menu menu, String actionId, String name) {
		final ZLAndroidApplication application = (ZLAndroidApplication) getApplication();
		application.myMainWindow.addMenuItem(menu, actionId, null, name);
	}

	private void addMenuItem(Menu menu, String actionId, int iconId) {
		final ZLAndroidApplication application = (ZLAndroidApplication) getApplication();
		application.myMainWindow.addMenuItem(menu, actionId, iconId, null);
	}

	private void addMenuItem(Menu menu, String actionId) {
		final ZLAndroidApplication application = (ZLAndroidApplication) getApplication();
		application.myMainWindow.addMenuItem(menu, actionId, null, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		addMenuItem(menu, ActionCode.SHOW_LIBRARY, R.drawable.ic_menu_library);// 本地藏书
		addMenuItem(menu, ActionCode.SHOW_NETWORK_LIBRARY,
				R.drawable.ic_menu_networklibrary);// 在线书库
		addMenuItem(menu, ActionCode.SHOW_TOC, R.drawable.ic_menu_toc);// 目录
		addMenuItem(menu, ActionCode.SHOW_BOOKMARKS,
				R.drawable.ic_menu_bookmarks);// 书签
		addMenuItem(menu, ActionCode.SWITCH_TO_NIGHT_PROFILE,
				R.drawable.ic_menu_night);// 黑底白字
		addMenuItem(menu, ActionCode.SWITCH_TO_DAY_PROFILE,
				R.drawable.ic_menu_day);// 白底黑字
		addMenuItem(menu, ActionCode.SEARCH, R.drawable.ic_menu_search);// 查找
		addMenuItem(menu, ActionCode.SHOW_PREFERENCES);// 参数设置
		addMenuItem(menu, ActionCode.SHOW_BOOK_INFO);// 书籍信息
		addMenuItem(menu, ActionCode.ROTATE);// 翻转屏幕
		addMenuItem(menu, ActionCode.INCREASE_FONT);// 字体加大
		addMenuItem(menu, ActionCode.DECREASE_FONT);// 字体缩小
		addMenuItem(menu, ActionCode.SHOW_NAVIGATION);// 导航
		// TURN_PAGE_FORWARD和TURN_PAGE_BACK
		synchronized (myPluginActions) {
			int index = 0;
			for (PluginApi.ActionInfo info : myPluginActions) {
				if (info instanceof PluginApi.MenuActionInfo) {
					addMenuItem(menu, PLUGIN_ACTION_PREFIX + index++,
							((PluginApi.MenuActionInfo) info).MenuItemName);
				}
			}
		}

		final ZLAndroidApplication application = (ZLAndroidApplication) getApplication();
		application.myMainWindow.refreshMenu();

		return true;
	}
}
