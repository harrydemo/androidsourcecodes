package org.music.tools;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.app.music.R;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LocalMusicListAdapter extends BaseAdapter {
	private Context mcontext;// 上下文
	private Cursor mcursor;// 游标

	public LocalMusicListAdapter(Context c, Cursor cur) {
		mcontext = c;
		mcursor = cur;
	}

	@Override
	public int getCount() {

		return mcursor.getCount();// 得到结果集中有多少记录
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = LayoutInflater.from(mcontext).inflate(
				R.layout.music_list_item, null);

		mcursor.moveToPosition(position);// 将游标移动到相应位置
		ImageView images = (ImageView) convertView.findViewById(R.id.listitem);
		// 这是歌名歌手旁边显示专辑缩略图
		Bitmap bm = getArtwork(mcontext, mcursor.getInt(3),
				mcursor.getInt(mcursor
						.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)), true);
		images.setImageBitmap(bm);

		TextView music_name = (TextView) convertView
				.findViewById(R.id.musicname);
		music_name.setText(mcursor.getString(0));
		TextView music_singer = (TextView) convertView
				.findViewById(R.id.singer);
		music_singer.setText(mcursor.getString(2));
		TextView music_time = (TextView) convertView.findViewById(R.id.time);
		music_time.setText(toTime(mcursor.getInt(1)));

		return convertView;
	}

	/**
	 * 时间的转换
	 */
	public String toTime(int time) {

		time /= 1000;
		int minute = time / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}

	/**
	 * 这边是获得专辑缩略图三个方法,computeSampleSize是构造缩略图，
	 * getArtwork和getArtworkFromFile是分别从图片上大小判断，从而得到合适的缩略图
	 * 说白了就是用BitmapFactory和IO操作。说是说那么简单，但做起来十分难啊。果断参考人家的啊
	 */
	public static Bitmap getArtwork(Context context, long song_id,
			long album_id, boolean allowdefault) {

		if (album_id < 0) {
			// This is something that is not in the database, so get the album
			// art directly
			// from the file.
			if (song_id >= 0) {
				Bitmap bm = getArtworkFromFile(context, song_id, -1);
				if (bm != null) {
					return bm;
				}
			}
			if (allowdefault) {
				return getDefaultArtwork(context);
			}
			return null;
		}
		ContentResolver res = context.getContentResolver();
		Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
		if (uri != null) {
			InputStream in = null;
			try {
				in = res.openInputStream(uri);
				BitmapFactory.Options options = new BitmapFactory.Options();
				// 进行大小判断
				options.inSampleSize = 1;

				options.inJustDecodeBounds = true;

				BitmapFactory.decodeStream(in, null, options);
				// 缩放比例调用computeSampleSize方法
				options.inSampleSize = computeSampleSize(options, 30);
				// 我们得到了缩放的比例，现在开始正式读入BitMap数据
				options.inJustDecodeBounds = false;
				options.inDither = false;
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				in = res.openInputStream(uri);
				return BitmapFactory.decodeStream(in, null, options);
			} catch (FileNotFoundException ex) {
				// The album art thumbnail does not actually exist. Maybe the
				// user deleted it, or
				// maybe it never existed to begin with.
				Bitmap bm = getArtworkFromFile(context, song_id, album_id);
				if (bm != null) {
					if (bm.getConfig() == null) {
						bm = bm.copy(Bitmap.Config.RGB_565, false);
						if (bm == null && allowdefault) {
							return getDefaultArtwork(context);
						}
					}
				} else if (allowdefault) {
					bm = getDefaultArtwork(context);
				}
				return bm;
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException ex) {
				}
			}
		}

		return null;
	}

	private static Bitmap getArtworkFromFile(Context context, long songid,
			long albumid) {
		Bitmap bm = null;

		if (albumid < 0 && songid < 0) {
			throw new IllegalArgumentException(
					"Must specify an album or a song id");
		}
		try {

			BitmapFactory.Options options = new BitmapFactory.Options();

			FileDescriptor fd = null;
			if (albumid < 0) {
				Uri uri = Uri.parse("content://media/external/audio/media/"
						+ songid + "/albumart");
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					fd = pfd.getFileDescriptor();
					// bm = BitmapFactory.decodeFileDescriptor(fd,null,options);
				}
			} else {
				Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					fd = pfd.getFileDescriptor();
					// bm = BitmapFactory.decodeFileDescriptor(fd,null,options);
				}
			}

			options.inSampleSize = 1;
			// 只进行大小判断
			options.inJustDecodeBounds = true;
			// 调用此方法得到options得到图片的大小
			BitmapFactory.decodeFileDescriptor(fd, null, options);
			// 我们的目标是在800pixel的画面上显示。
			// 所以需要调用computeSampleSize得到图片缩放的比例
			options.inSampleSize = 500;
			// 我们得到了缩放的比例，现在开始正式读入BitMap数据
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			// 根据options参数，减少所需要的内存
			bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
		} catch (FileNotFoundException ex) {

		}

		return bm;
	}

	static int computeSampleSize(BitmapFactory.Options options, int target) {
		int w = options.outWidth;
		int h = options.outHeight;
		int candidateW = w / target;
		int candidateH = h / target;
		int candidate = Math.max(candidateW, candidateH);
		if (candidate == 0)
			return 1;
		if (candidate > 1) {
			if ((w > target) && (w / candidate) < target)
				candidate -= 1;
		}
		if (candidate > 1) {
			if ((h > target) && (h / candidate) < target)
				candidate -= 1;
		}

		return candidate;
	}

	private static Bitmap getDefaultArtwork(Context context) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		return BitmapFactory.decodeStream(context.getResources()
				.openRawResource(R.drawable.music), null, opts);
	}

	private static final Uri sArtworkUri = Uri
			.parse("content://media/external/audio/albumart");
	// private static final BitmapFactory.Options sBitmapOptions = new
	// BitmapFactory.Options();
	// private static Bitmap mCachedBit = null;
}
