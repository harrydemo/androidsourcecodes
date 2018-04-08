package com.uvchip.mediacenter.filebrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.uvchip.files.FileItem;
import com.uvchip.files.FileItemForOperation;
import com.uvchip.files.FileManager.FileFilter;
import com.uvchip.files.FileManager.FilesFor;
import com.uvchip.files.FileManager.ViewMode;
import com.uvchip.files.FileOperationThreadManager;
import com.uvchip.files.FileOperationThreadManager.CopyOperation;
import com.uvchip.files.FilePropertyAdapter;
import com.uvchip.mediacenter.MainActivity;
import com.uvchip.mediacenter.PreparedResource;
import com.uvchip.mediacenter.R;
import com.uvchip.mediacenter.filebrowser.HorizontalLayout.OnTVItemClickListener;
import com.uvchip.utils.CustomListener;
import com.uvchip.utils.ViewEffect;

public class FileBrowser extends Browser {
	final boolean DEBUG = false;
	static {
		TAG = FileBrowser.class.getCanonicalName();
	}
	private final String SDCARD = Environment.getExternalStorageDirectory()
			.getPath();
	private final String MNT_SDCARD = "/mnt/sdcard/";
	private final String KEY_PATH = "com.uvchip.filebrowser.path";

	private GridView mGridView;
	private ListView mListView;
	public File externalStorageDirectory = Environment
			.getExternalStorageDirectory();
	private HorizontalLayout filePathLayout;
	/**
	 * 当前浏览的文件夹
	 */
	public String currFolder = externalStorageDirectory.getParentFile()
			.getAbsolutePath();

	private ImageButton btn_uplevel;
	private ImageButton btn_newFolder; //
	private ImageButton btn_paste;
	private ImageButton btn_toggle;
	private ImageButton btn_operating;
	private ImageButton btn_home;
	private LinearLayout ivEmptyFolder;

	List<Button> btns = new ArrayList<Button>();
	private final int MENU_FIRST = Menu.FIRST + 100;
	private final int MENU_COPY = MENU_FIRST;
	private final int MENU_CUT = MENU_FIRST + 1;
	private final int MENU_DELETE = MENU_FIRST + 2;
	private final int MENU_RENAME = MENU_FIRST + 3;
	private final int MENU_READPROP = MENU_FIRST + 4;
	private final int MENU_SELECT_ALL = MENU_FIRST + 5;
	private final int MENU_HELP = MENU_FIRST + 6;
	private final int MENU_REFRESH = MENU_FIRST + 7;
	private final int MENU_OPEN_AS = MENU_FIRST + 9;

	public PreparedResource getPreparedResource() {
		return preResource;
	}

	/**
	 * 在后台执行操作
	 */
	private boolean backgroundOperation = false;

	public FileBrowser(Context context) {
		super(context);
		nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		initView();
		mViewMode = ViewMode.GRIDVIEW;
		QueryData(new File(currFolder));
	}

	private void QueryData(File preFile) {
		QueryData(preFile, true, FileFilter.ALL);
	}

	protected void QueryData(File preFile, boolean clear, FileFilter filter) {
		super.QueryData(preFile, clear, filter);
		toggleViewMode();
		SetFilePath(preFile.getAbsolutePath().equals("/") ? "/" : preFile
				.getAbsolutePath() + "/");
		toggleBtnEnable(false);
		selectedAll = false;
		toggleOperatingView(false);
	}

	public void onResume() {
		nm.cancelAll();
		mFileManager.resetDataForOperation();

	}

	public void onDestroy() {
		preResource.recycle();
		mFileManager.resetDataForOperation();
	}

	public void onPause() {

	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mView = (View) mInflater.inflate(R.layout.file_browser, null);
		mGridView = (GridView) mView.findViewById(R.id.filesGridView);
		mListView = (ListView) mView.findViewById(R.id.filesListView);
		btn_home = CreateAndSetBtn(mView, R.id.imgbtn_home);
		btn_home.setImageResource(R.drawable.toolbar_home);
		btn_uplevel = CreateAndSetBtn(mView, R.id.imgbtn_uplevel);
		btn_uplevel.setImageResource(R.drawable.toolbar_uplevel);
		btn_newFolder = CreateAndSetBtn(mView, R.id.imgbtn_newFolder);
		btn_newFolder.setImageResource(R.drawable.toolbar_newfolder);
		btn_paste = CreateAndSetBtn(mView, R.id.imgbtn_paste);
		btn_paste.setImageResource(R.drawable.toolbar_paste_unenable);
		btn_toggle = CreateAndSetBtn(mView, R.id.imgbtn_toggleListAndGrid);
		btn_toggle.setImageResource(R.drawable.toolbar_mode_list);
		btn_operating = CreateAndSetBtn(mView, R.id.imgbtn_operating);
		btn_operating.setImageResource(R.drawable.toolbar_single);
		ivEmptyFolder = (LinearLayout) mView.findViewById(R.id.empty_folder);

		mListView.setOnItemClickListener(this);
		((Activity) mContext).registerForContextMenu(mListView);
		mGridView.setOnItemClickListener(this);
		((Activity) mContext).registerForContextMenu(mGridView);

		filePathLayout = (HorizontalLayout) mView
				.findViewById(R.id.filePathLayout);
		filePathLayout.setOnItemClickListener(new OnTVItemClickListener() {
			@Override
			public void onItemClick(TextView v) {
				willExit = false;
				currFolder = filePathLayout.GetPathByTv(v);
				QueryData(new File(currFolder));
			}
		});
		setViewWidth();

	}

	/**
	 * 计算并设置按钮的宽度
	 */
	private void setViewWidth() {
		int width = (MainActivity.mScreenWidth - 10) / 6;
		btn_home.setMinimumWidth(width);
		btn_operating.setMinimumWidth(width);
		btn_uplevel.setMinimumWidth(width);
		btn_newFolder.setMinimumWidth(width);
		btn_paste.setMinimumWidth(width);
		btn_toggle.setMinimumWidth(width);
		mGridView.setNumColumns(MainActivity.mScreenWidth / 160);
	}

	private ImageButton CreateAndSetBtn(View parent, int id) {
		ImageButton btn = (ImageButton) parent.findViewById(id);
		btn.setOnClickListener(this);
		btn.setOnLongClickListener(this);
		return btn;
	}

	/**
	 * 切换浏览模式
	 */
	private void toggleViewMode() {
		switch (mViewMode) {
		case LISTVIEW:
			mItemsAdapter.setViewMode(ViewMode.LISTVIEW);
			btn_toggle.setImageResource(R.drawable.toolbar_mode_icon);
			mListView.setVisibility(View.VISIBLE);
			mListView.setAdapter(mItemsAdapter);
			mGridView.setVisibility(View.GONE);
			mGridView.setAdapter(null);
			break;
		case GRIDVIEW:
			mItemsAdapter.setViewMode(ViewMode.GRIDVIEW);
			btn_toggle.setImageResource(R.drawable.toolbar_mode_list);
			mGridView.setVisibility(View.VISIBLE);
			mGridView.setAdapter(mItemsAdapter);
			mListView.setVisibility(View.GONE);
			mListView.setAdapter(null);
			break;
		default:
			break;
		}
	}

	private void toggleBtnEnable(boolean finished) {
		if (finished) {
			btn_home.setEnabled(true);
			btn_toggle.setEnabled(true);
			btn_uplevel.setEnabled(true);
			btn_operating.setEnabled(true);
		} else {
			btn_home.setEnabled(false);
			btn_operating.setEnabled(false);
			btn_uplevel.setEnabled(false);
			btn_toggle.setEnabled(false);
		}
	}

	/**
	 * 更新地址栏显示的路径
	 */
	private void SetFilePath(String currFolder) {
		filePathLayout.SetFilePath(currFolder);
	}

	/**
	 * 当前目录是否可操作（粘贴，新建文件夹，重命名，删除）
	 * 
	 * @param justBrowser
	 *            是否只是进入当前目录
	 * @return
	 */
	private boolean currFolderCanOperate(boolean justBrowser) {
		if (!mFileManager.getSdcardState().equals(Environment.MEDIA_MOUNTED)) {
			if ((currFolder.equals(SDCARD) || currFolder.equals(MNT_SDCARD))
					&& justBrowser) {
				ViewEffect.showToast(mContext, R.string.toast_sdcard_error);
				return false;
			} else if ((currFolder.startsWith(SDCARD) || currFolder
					.startsWith(MNT_SDCARD)) && !justBrowser) {
				ViewEffect.showToast(mContext,
						R.string.toast_operation_failed_sdcard_error);
				return false;
			}
		}
		return true;
	}

	private boolean isOperating;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FileItemForOperation fileItem = mData.getFileItems().get(position);
		if (!isOperating) {
			if (!fileItem.getFileItem().isDirectory()) {
				clickFileItem(fileItem);
				return;
			}
			currFolder = fileItem.getFileItem().getFilePath();
			currFolderCanOperate(true);
			QueryData(new File(currFolder));
		} else {
			int selState = fileItem.getSelectState();
			if (selState == 0) {
				fileItem.setSelectState(FileItemForOperation.SELECT_STATE_SEL);
			} else if (selState == 1) {
				fileItem.setSelectState(FileItemForOperation.SELECT_STATE_NOR);
			}
			refreshData();
		}
	}

	/**
	 * 将选中的Item保存起来
	 */
	private void addSelectedItemToApp(FilesFor filesFor) {
		for (FileItemForOperation operationFile : mData.getFileItems()) {
			if (operationFile.getSelectState() == FileItemForOperation.SELECT_STATE_SEL) {
				mFileManager.addFileItem(operationFile);
				if (filesFor == FilesFor.CUT) {
					operationFile
							.setSelectState(FileItemForOperation.SELECT_STATE_CUT);
					refreshData();
				}
			}
		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		if (DEBUG)
			Log.i(TAG, "newConfig========>" + newConfig);
		setViewWidth();
		switch (newConfig.orientation) {
		case Configuration.ORIENTATION_LANDSCAPE:
			if (mViewMode == ViewMode.LISTVIEW) {
				mViewMode = ViewMode.GRIDVIEW;
				toggleViewMode();
			}
			break;
		case Configuration.ORIENTATION_PORTRAIT:
			if (mViewMode == ViewMode.GRIDVIEW) {
				mViewMode = ViewMode.LISTVIEW;
				toggleViewMode();
			}
			break;
		default:
			break;
		}
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.add(1, MENU_REFRESH, Menu.NONE, R.string.menu_refresh).setIcon(
				R.drawable.ic_menu_refresh);
		// menu.add(1, MENU_HELP, Menu.NONE,
		// R.string.menu_help).setIcon(android.R.drawable.ic_menu_help);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_HELP:
			// Intent intent = new Intent();
			// intent.setClass(mContext, HelpInfo.class);
			// startActivity(intent);
			break;
		case MENU_REFRESH:
			QueryData(new File(currFolder));
			return true;
		default:
			break;
		}
		return false;
	}

	/**
	 * 长按选择的数据的位置
	 */
	private int selectedPosition;

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		selectedPosition = info.position;
		FileItemForOperation operationItem = mData.getFileItems().get(
				selectedPosition);
		menu.setHeaderTitle(R.string.title_menutitle);
		menu.setHeaderIcon(R.drawable.toolbar_operation);
		if (selectedAll)
			menu.add(0, MENU_SELECT_ALL, Menu.NONE, R.string.menu_unselect_all);
		else
			menu.add(0, MENU_SELECT_ALL, Menu.NONE, R.string.menu_select_all);
		/*
		 * if(operationItem.getFileItem().isDirectory()){ menu.add(0,
		 * MENU_OPEN_AS, Menu.NONE, R.string.menu_open); }else{ SubMenu subMenu
		 * = menu.addSubMenu(1, MENU_OPEN_AS, Menu.NONE, R.string.menu_open_as);
		 * subMenu.add(1, SUB_MENU_TXT, Menu.NONE, R.string.sub_menu_txt);
		 * subMenu.add(1, SUB_MENU_AUDIO, Menu.NONE, R.string.sub_menu_audio);
		 * subMenu.add(1, SUB_MENU_VIDEO, Menu.NONE, R.string.sub_menu_video);
		 * subMenu.add(1, SUB_MENU_PIC, Menu.NONE, R.string.sub_menu_pic); }
		 */
		menu.add(0, MENU_OPEN_AS, Menu.NONE, R.string.menu_open);

		menu.add(0, MENU_COPY, Menu.NONE, R.string.menu_copy_selected);
		menu.add(0, MENU_CUT, Menu.NONE, R.string.menu_cut_selected);
		menu.add(0, MENU_DELETE, Menu.NONE, R.string.menu_delete_selected);
		menu.add(0, MENU_RENAME, Menu.NONE, R.string.menu_rename);
		menu.add(0, MENU_READPROP, Menu.NONE, R.string.menu_read_prop);
		if (isOperating)
			operationItem.setSelectState(FileItemForOperation.SELECT_STATE_SEL);
		else {
			SelectNothing();
			operationItem.setSelectState(FileItemForOperation.SELECT_STATE_SEL);
		}
		refreshData();
	}

	public void onContextMenuClosed(Menu menu) {
		if (!hasContextItemSelected) {
			FileItemForOperation fileItemForOperation = mData.getFileItems()
					.get(selectedPosition);
			fileItemForOperation
					.setSelectState(FileItemForOperation.SELECT_STATE_NOR);
			refreshData();
		}
		hasContextItemSelected = false;
	}

	boolean hasContextItemSelected = false;

	public boolean onContextItemSelected(MenuItem item) {

		hasContextItemSelected = true;
		final FileItemForOperation fileItemForOperation = mData.getFileItems()
				.get(selectedPosition);
		switch (item.getItemId()) {
		/*
		 * case SUB_MENU_TXT : case SUB_MENU_PIC: case SUB_MENU_AUDIO: case
		 * SUB_MENU_VIDEO: openAs(item.getItemId(),
		 * fileItemForOperation.getFileItem()); break;
		 */
		case MENU_OPEN_AS:
			if (fileItemForOperation.getFileItem().isDirectory()) {
				currFolder = fileItemForOperation.getFileItem().getFilePath();
				currFolderCanOperate(true);
				QueryData(new File(currFolder));
			} else {
				openAsDialog(fileItemForOperation.getFileItem()).show();
			}
			break;
		case MENU_SELECT_ALL:
			if (!selectedAll) {
				SelectAll();
			} else {
				SelectNothing();
			}
			toggleOperatingView(selectedAll);
			break;
		case MENU_COPY:
			if (backgroundOperation) {
				ViewEffect.showToast(mContext, R.string.toast_please_waite);
				break;
			}
			mFileManager.resetDataForOperation();
			mFileManager.addFileItem(fileItemForOperation);
			addSelectedItemToApp(FilesFor.COPY);
			mFileManager.setFilesFor(FilesFor.COPY);
			toggleOperatingView(false);
			break;
		case MENU_CUT:
			if (backgroundOperation) {
				ViewEffect.showToast(mContext, R.string.toast_please_waite);
				break;
			}
			mFileManager.resetDataForOperation();
			mFileManager.addFileItem(fileItemForOperation);
			addSelectedItemToApp(FilesFor.CUT);
			mFileManager.setFilesFor(FilesFor.CUT);
			toggleOperatingView(false);
			break;
		case MENU_DELETE:
			if (backgroundOperation) {
				ViewEffect.showToast(mContext, R.string.toast_please_waite);
				break;
			}
			if (!currFolderCanOperate(false))
				return false;
			comfirDialog = ViewEffect.createComfirDialog(mContext,
					R.string.title_comfir_delete,
					R.string.dialog_msg_comfir_delete, new CustomListener() {
						@Override
						public void onListener() {
							mFileManager.resetDataForOperation();
							mFileManager.addFileItem(fileItemForOperation);
							addSelectedItemToApp(FilesFor.DELETE);
							mFileManager.setFilesFor(FilesFor.DELETE);
							List<FileItemForOperation> list = mFileManager
									.getDataForOperation().getFileItems();
							showOperationProgressDialog(
									R.string.title_deleting, list.size(), true);
							FileOperationThreadManager manager = new FileOperationThreadManager(
									list, mHandler);
							manager.beginDelete();
						}
					}, new CustomListener() {
						@Override
						public void onListener() {
							comfirDialog.dismiss();
						}
					});
			comfirDialog.show();
			break;
		case MENU_RENAME:
			if (!currFolderCanOperate(false))
				return false;
			FileItem fileItem = fileItemForOperation.getFileItem();
			renameDialog = ViewEffect.createRenameDialog(mContext,
					R.string.title_rename, fileItem.getFileName(),
					new CustomListener() {
						@Override
						public void onListener() {
							EditText et = (EditText) renameDialog
									.findViewById(R.id.rename);
							String newName = et.getText().toString();
							FileOperationThreadManager manager = new FileOperationThreadManager(
									fileItemForOperation, mHandler);
							manager.rename(newName);
						}
					}, new CustomListener() {
						@Override
						public void onListener() {
							renameDialog.dismiss();
						}
					});

			renameDialog.show();
			break;
		case MENU_READPROP:
			FileOperationThreadManager manager = new FileOperationThreadManager();
			FilePropertyAdapter adapter = manager.readProp(mContext,
					fileItemForOperation);
			AlertDialog propertyDialog = ViewEffect.createPropertyDialog(
					mContext, R.string.title_read_property, adapter);
			propertyDialog.show();
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 文件重命名对话框
	 */
	private AlertDialog renameDialog;
	/**
	 * 新建文件夹对话框
	 */
	private AlertDialog newFolderDialog;

	boolean willExit = false;

	/**
	 * 是否已经回退到了根目录
	 * 
	 * @return
	 */
	boolean isRoot() {
		return currFolder.equals("/");
	}

	/**
	 * 返回上一级文件夹
	 */
	private void goBack() {
		if (isRoot()) {
			willExit = true;
			ViewEffect.showToast(mContext,
					R.string.toast_press_one_more_to_exit);
			return;
		}
		willExit = false;
		File file = new File(currFolder);
		File parentFile = file.getParentFile();
		mData.getFileItems().clear();
		QueryData(parentFile);
		refreshData();
		currFolder = parentFile.getAbsolutePath().equals("/") ? "/"
				: parentFile.getAbsolutePath() + "/";
	}

	/**
	 * 接收并处理各种操作的结果消息
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// case RefreshData.LOAD_APK_ICON_FINISHED:
			// refreshData();
			// return;
			case FileOperationThreadManager.NEWFOLDER_FAILED:
				newFolderDialog.dismiss();
				handleFailed(msg);
				break;
			case FileOperationThreadManager.NEWFOLDER_SUCCEED:
				newFolderDialog.dismiss();
				handleSucceed(msg);

				Bundle bb = msg.getData();
				if (bb != null) {
					FileItem fileItem = new FileItem();
					String newName = bb
							.getString(FileOperationThreadManager.KEY_NEW_NAME);
					String newPath = bb
							.getString(FileOperationThreadManager.KEY_NEW_PATH);
					fileItem.setFileName(newName);
					fileItem.setFilePath(newPath);
					fileItem.setDirectory(true);
					fileItem.setFileSize(-1);
					fileItem.setExtraName("folder");
					fileItem.setIconId(preResource.getBitMap("folder"));

					// 将新建成功的文件夹放入到 显示队列中
					List<FileItemForOperation> fileItems = mData.getFileItems();
					FileItemForOperation operationFile = new FileItemForOperation();
					operationFile.setFileItem(fileItem);
					mData.insertAt(fileItems.size(), operationFile);
					refreshData();
				}
				ivEmptyFolder.setVisibility(View.GONE);
				break;
			case FileOperationThreadManager.RENAME_FAILED:
				renameDialog.dismiss();
				handleFailed(msg);
				break;
			case FileOperationThreadManager.RENAME_SUCCEED:
				renameDialog.dismiss();
				handleSucceed(msg);
				break;
			case FileOperationThreadManager.GETTOTALNUM_COMPLETED:
				ProgressBar del_progress1 = (ProgressBar) operationProgressDialog
						.findViewById(R.id.progressBar);
				TextView del_tvNum1 = (TextView) operationProgressDialog
						.findViewById(R.id.tvNumber);
				TextView del_tvPercent1 = (TextView) operationProgressDialog
						.findViewById(R.id.tvPercent);
				del_progress1.setProgress(0);
				del_tvPercent1.setText(0 + "%");
				del_tvNum1.setText(0 + "/" + msg.arg1);
				break;
			case FileOperationThreadManager.GETTOTALNUM_ERROR:
				operationProgressDialog.dismiss();
				ViewEffect.showToast(mContext,
						R.string.toast_getfilenumber_error);
				break;
			case FileOperationThreadManager.DELETE_COMPLETED:
				handleSucceed(msg);
				QueryData(new File(currFolder));
				operationProgressDialog.dismiss();
				break;
			case FileOperationThreadManager.DELETE_PROGRESS_CHANGE:
				ProgressBar del_progress = (ProgressBar) operationProgressDialog
						.findViewById(R.id.progressBar);
				TextView del_tvNum = (TextView) operationProgressDialog
						.findViewById(R.id.tvNumber);
				TextView del_tvPercent = (TextView) operationProgressDialog
						.findViewById(R.id.tvPercent);
				del_progress.setProgress(msg.arg1);
				del_tvPercent.setText(msg.arg1 + "%");
				String[] del_old = del_tvNum.getText().toString().split("/");
				del_tvNum.setText(msg.arg2 + "/" + del_old[1]);
				break;
			case FileOperationThreadManager.DELETE_FAILED:
				operationProgressDialog.dismiss();
				handleFailed(msg);
				break;
			case FileOperationThreadManager.DELETE_CANCEL:
				ViewEffect.showToast(mContext,
						R.string.toast_operation_canceled);
				break;
			case FileOperationThreadManager.GETSIZE_COMPLETED:
				if (operationProgressDialog != null) {
					TextView tv = (TextView) operationProgressDialog
							.findViewById(R.id.tvNumber);
					tv.setText("0/" + msg.arg1);
				}
				if (pasteThreadManager != null) {
					pasteThreadManager.beginPaste(CopyOperation.UNKOWN);
				}
				break;
			case FileOperationThreadManager.PASTE_FAILED:
				operationProgressDialog.dismiss();
				pasteThreadManager = null;
				handleFailed(msg);
				break;
			case FileOperationThreadManager.PASTE_COMPLETED:
				operationProgressDialog.dismiss();
				handleSucceed(msg);
				QueryData(new File(currFolder));
				break;
			case FileOperationThreadManager.PASTE_PROGRESS_CHANGE:
				ProgressBar paste_progress = (ProgressBar) operationProgressDialog
						.findViewById(R.id.progressBar);
				TextView paste_tvNum = (TextView) operationProgressDialog
						.findViewById(R.id.tvNumber);
				TextView paste_tvPercent = (TextView) operationProgressDialog
						.findViewById(R.id.tvPercent);
				paste_progress.setProgress(msg.arg1);
				if (msg.arg2 != 0) {
					paste_tvNum.setText(msg.arg2 + "");
				}
				Bundle bundle = (Bundle) msg.obj;

				if (bundle != null) {
					paste_tvNum.setText(bundle.getString("currPos"));
					paste_tvPercent.setText(bundle.getString("percentage"));
				}
				break;
			case FileOperationThreadManager.PASTE_PAUSE:
				handlePaused(msg);
				break;
			case FileOperationThreadManager.PASTE_CANCEL:
				QueryData(new File(currFolder));
				ViewEffect.showToast(mContext,
						R.string.toast_operation_canceled);
				operationProgressDialog.dismiss();
				pasteThreadManager = null;
				break;
			// case FileOperationThreadManager.LOADCAPACITY:
			// mCapacityAdapater.notifyDataSetChanged();
			// return;
			// case FileOperationThreadManager.LOADCAPACITYOK:
			// if (msg.arg1 == rand) {
			// mCapacityAdapater.notifyDataSetChanged();
			// View view = capacityDialog.findViewById(R.id.loading);
			// view.setVisibility(View.GONE);
			// }
			// return;
			default:
				break;
			}
			/**
			 * 操作结束后不重置application，可以多次粘贴(剪切除外)
			 */
			if (msg.what == FileOperationThreadManager.PASTE_CANCEL
					|| msg.what == FileOperationThreadManager.PASTE_COMPLETED
					|| msg.what == FileOperationThreadManager.PASTE_SUCCEED
					|| msg.what == FileOperationThreadManager.PASTE_FAILED
					|| msg.what == FileOperationThreadManager.DELETE_COMPLETED
					|| msg.what == FileOperationThreadManager.DELETE_FAILED) {
				if (mFileManager.getFilesFor() == FilesFor.CUT
						|| mFileManager.getFilesFor() == FilesFor.DELETE)
					mFileManager.resetDataForOperation();
				if (backgroundOperation) {
					backgroundOperation = false;
					setMood(R.drawable.smallicon,
							R.string.operating_background_complete);
				}
			}
			finishOperation();
		}
	};

	/**
	 * 操作完成之后 回到初始状态
	 */
	public void finishOperation() {
		isOperating = false;
		// item = null;
		// showCurLoc();
		// // toogleImageButton();
		// toggleOperatingView(false, false);
		// drawPopup(false);
		// setFocus();
	}

	private void handleSucceed(Message msg) {
		Bundle b = msg.getData();
		String currName = "";
		if (b != null) {
			currName = b.getString(FileOperationThreadManager.KEY_CURR_NAME);
			if (currName == null)
				currName = "";
		}
		switch (msg.what) {
		case FileOperationThreadManager.PASTE_COMPLETED:
			ViewEffect.showToast(mContext, R.string.toast_paste_complete);
			break;
		case FileOperationThreadManager.DELETE_COMPLETED:
			ViewEffect.showToast(mContext,
					formatStr(R.string.toast_delete_complete, currName));
			break;
		case FileOperationThreadManager.NEWFOLDER_SUCCEED:
			ViewEffect.showToast(mContext, R.string.toast_new_folder_succeed);
			break;
		case FileOperationThreadManager.RENAME_SUCCEED:
			ViewEffect.showToast(mContext, R.string.toast_rename_succeed);
			break;
		default:
			break;
		}
	}

	/**
	 * handle operation failed
	 * 
	 * @param msg
	 */
	private void handleFailed(Message msg) {
		Bundle b = msg.getData();
		String currName = "";
		if (b != null) {
			currName = b.getString(FileOperationThreadManager.KEY_CURR_NAME);
			if (currName == null)
				currName = "";
		}
		switch (msg.arg1) {
		case FileOperationThreadManager.FAILED_REASON_UNKOWN:
			ViewEffect.showToast(mContext, R.string.toast_operation_failed);
			break;
		case FileOperationThreadManager.FAILED_REASON_INVALNAME:
			ViewEffect.showToast(mContext, R.string.toast_inval_filename);
			break;
		case FileOperationThreadManager.FAILED_REASON_FROM_FILE_NOT_EXIST:
			ViewEffect.showToast(mContext, R.string.toast_file_not_find);
			break;
		case FileOperationThreadManager.FAILED_REASON_READ_ONLY_FILE_SYSTEM:
			ViewEffect.showToast(mContext,
					formatStr(R.string.toast_read_only_file_system, currName));
			break;
		case FileOperationThreadManager.FAILED_REASON_FOLDER_HAS_EXIST:
			ViewEffect.showToast(mContext,
					R.string.toast_rename_or_new_folder_failed_folder_exist);
			break;
		case FileOperationThreadManager.FAILED_REASON_FOLDER_LIMIT:
			ViewEffect.showToast(mContext, R.string.toast_folder_limit);
			break;
		case FileOperationThreadManager.FAILED_REASON_SAME_FOLDER:
			ViewEffect.showToast(mContext,
					R.string.toast_cont_move_in_same_folder);
			break;
		case FileOperationThreadManager.FAILED_REASON_NO_SPACE_LEFT:
			ViewEffect.showToast(mContext, R.string.toast_no_space_left);
			break;
		default:
			break;
		}
	}

	private String formatStr(int resId, String str) {
		String res = mContext.getText(resId).toString();
		return String.format(res, str);
	}

	/**
	 * handle operation paused
	 * 
	 * @param msg
	 */
	private void handlePaused(Message msg) {
		switch (msg.arg1) {
		case FileOperationThreadManager.PAUSE_REASON_TO_FOLDER_HAS_EXIST:
			showChooseOperationDialog();
			break;
		case FileOperationThreadManager.PAUSE_REASON_CANNOT_COVER:
			ViewEffect.showToast(mContext, R.string.toast_cant_cover);
			showChooseOperationDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 是否全选了
	 */
	private boolean selectedAll = false;

	/**
	 * 切换多选模式/单击打开模式
	 */
	private void toggleOperating() {
		toggleOperatingView();
		SelectNothing();
		// 选择了文件，但是没有下任何命令
		if (mFileManager.getFilesFor() == FilesFor.UNKOWN)
			mFileManager.resetDataForOperation();
	}

	private void toggleOperatingView() {
		isOperating = !isOperating;
		if (!isOperating) {
			ViewEffect.cancelToast();
			btn_operating.setImageResource(R.drawable.toolbar_single);
		} else {
			ViewEffect.showToast(mContext, R.string.toast_multi_operating);
			btn_operating.setImageResource(R.drawable.toolbar_multi);
		}
	}

	private void toggleOperatingView(boolean b) {
		isOperating = b;
		if (!isOperating) {
			btn_operating.setImageResource(R.drawable.toolbar_single);
		} else
			btn_operating.setImageResource(R.drawable.toolbar_multi);
	}

	@Override
	public boolean onLongClick(View v) {
		if (v.getId() == btn_operating.getId()) { // 切换单击和多选
			if (!selectedAll) {
				SelectAll();
			} else {
				SelectNothing();
			}
			toggleOperatingView(selectedAll);
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == btn_operating.getId()) { // 切换单击和多选
			toggleOperating();
		} else if (v.getId() == btn_home.getId()) {
			willExit = false;
			currFolder = externalStorageDirectory.getParentFile()
					.getAbsolutePath();
			QueryData(externalStorageDirectory.getParentFile());
		} else if (v.getId() == btn_uplevel.getId()) { // 返回上级目录
			if (willExit && isRoot()) {
				ViewEffect.cancelToast();
				if (pickPath) {
					((Activity) mContext).setResult(Activity.RESULT_CANCELED);
				}
			} else
				goBack();
		} else if (v.getId() == btn_newFolder.getId()) { // 新建文件夹
			if (!currFolderCanOperate(false))
				return;
			newFolderDialog = ViewEffect.newFolderDialog(mContext,
					R.string.title_newFolder, new CustomListener() {
						@Override
						public void onListener() {
							EditText et = (EditText) newFolderDialog
									.findViewById(R.id.rename);
							String newFolder = et.getText().toString();
							FileOperationThreadManager manager = new FileOperationThreadManager(
									mHandler);
							manager.newFolder(currFolder, newFolder);
						}
					}, new CustomListener() {
						@Override
						public void onListener() {
							newFolderDialog.dismiss();
						}
					});

			newFolderDialog.show();
		} else if (v.getId() == btn_paste.getId()) { // do paste
			if (!currFolderCanOperate(false)) {
				return;
			}
			if (!mFileManager.canOperation()) {
				ViewEffect.showToast(mContext, R.string.toast_no_file_to_paste);
				return;
			}
			List<FileItemForOperation> list = mFileManager
					.getDataForOperation().getFileItems();
			showOperationProgressDialog(R.string.title_copying, list.size(),
					true);
			createPasteThread(list);
			pasteThreadManager.setFilesFor(mFileManager.getFilesFor());
		}

		else if (v.getId() == btn_toggle.getId()) { // do toggle view
			toggleMode();
			toggleViewMode();
		}

	}

	/**
	 * 复制文件用的Manager
	 */
	FileOperationThreadManager pasteThreadManager;

	private void createPasteThread(List<FileItemForOperation> list) {
		if (pasteThreadManager == null) {
			pasteThreadManager = new FileOperationThreadManager(list,
					currFolder, mHandler, mFileManager.getFilesFor());
		} else {
			pasteThreadManager.setToFolder(currFolder);
			pasteThreadManager.setOperatingFiles(list);
		}
	}

	/**
	 * 删除确认对话框
	 */
	AlertDialog comfirDialog;

	/**
	 * 出现同名文件时和用户交互的对话框
	 */
	AlertDialog chooseOperationDialog;

	private void showChooseOperationDialog() {
		chooseOperationDialog = ViewEffect.createTheDialog(mContext,
				R.string.title_has_same_file, new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						operationProgressDialog.dismiss();
					}
				}, new android.widget.RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.cover:
							pasteThreadManager.beginPaste(CopyOperation.COVER);
							break;
						case R.id.jump:
							pasteThreadManager.beginPaste(CopyOperation.JUMP);
							break;
						case R.id.append2:
							pasteThreadManager
									.beginPaste(CopyOperation.APPEND2);
							break;
						case R.id.cancel:
							ViewEffect.showToast(mContext,
									R.string.toast_operation_canceled);
							operationProgressDialog.dismiss();
							break;
						default:
							break;
						}
						chooseOperationDialog.dismiss();
					}

				}, new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						pasteThreadManager.setDoitAsSame(isChecked);
					}
				});
		chooseOperationDialog.show();
	}

	/**
	 * 处理进度条
	 */
	AlertDialog operationProgressDialog;

	private void showOperationProgressDialog(int titleId, int count,
			boolean reCreat) {
		if (operationProgressDialog == null || reCreat) {
			operationProgressDialog = ViewEffect.createCustProgressDialog(
					mContext, titleId, count, hideOperationListener,
					negativeListener, cancelListener);
		}
		operationProgressDialog.show();
	}

	/**
	 * 隐藏处理进度条
	 */
	private CustomListener hideOperationListener = new CustomListener() {
		@Override
		public void onListener() {
			backgroundOperation = true;
			setMood(R.drawable.smallicon, R.string.operating_background);
		}
	};
	/**
	 * 取消处理
	 */
	private CustomListener negativeListener = new CustomListener() {
		@Override
		public void onListener() {
			pasteThreadManager.setCanceled(true);
		}
	};
	private OnCancelListener cancelListener = new OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			pasteThreadManager.setCanceled(true);
		}
	};
	PendingIntent contentIntent;

	private PendingIntent makeMoodIntent() {
		if (contentIntent == null)
			contentIntent = PendingIntent.getActivity(
					mContext,
					0,
					new Intent(mContext, FileBrowser.class).setFlags(
							Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(
							KEY_PATH, currFolder),
					PendingIntent.FLAG_UPDATE_CURRENT);
		return contentIntent;
	}

	private NotificationManager nm;

	private void setMood(int moodId, int contentId) {
		String notiContent = mContext.getText(contentId).toString();
		Notification notification = new Notification(moodId, notiContent,
				System.currentTimeMillis());
		notification.setLatestEventInfo(mContext,
				mContext.getText(R.string.app_name).toString(), notiContent,
				makeMoodIntent());
		nm.notify(R.layout.file_browser, notification);
	}

	/**
	 * 全选
	 */
	private void SelectAll() {
		selectedAll = true;
		for (FileItemForOperation fileItem : mData.getFileItems()) {
			fileItem.setSelectState(FileItemForOperation.SELECT_STATE_SEL);
		}
		refreshData();
	}

	/**
	 * 全不选
	 */
	private void SelectNothing() {
		selectedAll = false;
		for (FileItemForOperation fileItem : mData.getFileItems()) {
			fileItem.setSelectState(FileItemForOperation.SELECT_STATE_NOR);
		}
		refreshData();
	}

	@Override
	public void whichOperation(FilesFor filesFor, int size) {
		if (filesFor == FilesFor.COPY || filesFor == FilesFor.CUT) {
			if (filesFor == FilesFor.COPY) {
				ViewEffect.showToastLongTime(mContext,
						formatStr(R.string.intent_to_copy, "" + size));
			}
			if (filesFor == FilesFor.CUT)
				ViewEffect.showToastLongTime(mContext,
						formatStr(R.string.intent_to_cut, "" + size));
			btn_paste.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.toolbar_item_bg_highlight));
			btn_paste.setImageResource(R.drawable.toolbar_paste_enable);

		}
		if (filesFor == FilesFor.UNKOWN) {
			nm.cancelAll();
			btn_paste.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.toolbar_item_bg_unenable));
			btn_paste.setImageResource(R.drawable.toolbar_paste_unenable);
		}
	}

	@Override
	public void queryFinished() {
		selectedAll = false;
		if (mData.getFileItems().size() == 0) {
			ivEmptyFolder.setVisibility(View.VISIBLE);
		} else {
			ivEmptyFolder.setVisibility(View.GONE);
		}
		toggleBtnEnable(true);
	}

	@Override
	public void queryMatched() {
		refreshData();
	}

	@Override
	public boolean onBackPressed() {
		if (willExit && isRoot()) {
			ViewEffect.cancelToast();
			return false;
		}
		goBack();
		return true;
	}
}
