package org.zirco.ui.runnables;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;

import org.zirco.R;
import org.zirco.utils.ApplicationUtils;
import org.zirco.utils.IOUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.Browser;
import android.util.Log;

/**
 * Runnable to export history and bookmarks to an XML file.
 */
public class XmlHistoryBookmarksExporter implements Runnable {
	
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private String mFileName;
	private Cursor mCursor;	
	
	private File mFile;
	private String mErrorMessage = null;
	
	/**
	 * Constructor.
	 * @param context The current context.
	 * @param fileName The output file.
	 * @param cursor The cursor to history and bookmarks.
	 * @param progressDialog The progress dialog shown during export.
	 */
	public XmlHistoryBookmarksExporter(Context context, String fileName, Cursor cursor, ProgressDialog progressDialog) {
		mContext = context;
		mFileName = fileName;
		mCursor = cursor;
		mProgressDialog = progressDialog;
	}

	@Override
	public void run() {
		try {
			
			mFile = new File(IOUtils.getBookmarksExportFolder(), mFileName);				
			FileWriter writer = new FileWriter(mFile);

			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			writer.write("<itemlist>\n");
			
			if (mCursor.moveToFirst()) {
				
				int titleIndex = mCursor.getColumnIndex(Browser.BookmarkColumns.TITLE);
				int urlIndex = mCursor.getColumnIndex(Browser.BookmarkColumns.URL);
				int visitsIndex = mCursor.getColumnIndex(Browser.BookmarkColumns.VISITS);
				int dateIndex = mCursor.getColumnIndex(Browser.BookmarkColumns.DATE);
				int createdIndex = mCursor.getColumnIndex(Browser.BookmarkColumns.CREATED);
				int bookmarkIndex = mCursor.getColumnIndex(Browser.BookmarkColumns.BOOKMARK);
				
				while (!mCursor.isAfterLast()) {
					
					writer.write("<item>\n");
					
					String title = mCursor.getString(titleIndex);
					writer.write(String.format("<title>%s</title>\n", title != null ? URLEncoder.encode(title) : ""));
					
					String url = mCursor.getString(urlIndex);
					writer.write(String.format("<url>%s</url>\n", url != null ? URLEncoder.encode(url) : ""));
					
					writer.write(String.format("<created>%s</created>\n", mCursor.getLong(createdIndex)));
					writer.write(String.format("<visits>%s</visits>\n", mCursor.getInt(visitsIndex)));
					
					writer.write(String.format("<date>%s</date>\n", mCursor.getLong(dateIndex)));
					writer.write(String.format("<bookmark>%s</bookmark>\n", mCursor.getInt(bookmarkIndex)));
					
					writer.write("</item>\n");
					
					mCursor.moveToNext();
				}				
			}
			
			writer.write("</itemlist>\n");

			writer.flush();
			writer.close();
			
		} catch (IOException e1) {
			Log.w("Bookmark export failed", e1.toString());				
			mErrorMessage = e1.toString();
		}
		
		mHandler.sendEmptyMessage(0);
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			
			if (mContext != null) {
				if (mErrorMessage == null) {
					ApplicationUtils.showOkDialog(mContext,
							android.R.drawable.ic_dialog_info,
							mContext.getResources().getString(R.string.Commons_HistoryBookmarksExportSDCardDoneTitle),
							String.format(mContext.getResources().getString(R.string.Commons_HistoryBookmarksExportSDCardDoneMessage), mFile.getAbsolutePath()));
				} else {
					ApplicationUtils.showOkDialog(mContext,
							android.R.drawable.ic_dialog_alert,
							mContext.getResources().getString(R.string.Commons_HistoryBookmarksExportSDCardFailedTitle),
							String.format(mContext.getResources().getString(R.string.Commons_HistoryBookmarksFailedMessage), mErrorMessage));
				}
			}
		}
	};

}
